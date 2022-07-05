package com.app.relevamientoapp.view.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Video
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoCapture.withOutput
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.beeline.gpx.Gpx
import co.beeline.gpx.Metadata
import co.beeline.gpx.Route
import co.beeline.gpx.RoutePoint
import com.app.relevamientoapp.app.RelevamientoApp
import com.app.relevamientoapp.data.entity.ElementEntity
import com.app.relevamientoapp.data.repository.ElementDbRepo
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.utilities.common.Constants.SURVEY
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import kotlin.collections.List

@HiltViewModel
class RecordScreenViewModel @Inject constructor(
    private val elementDao: ElementDbRepo,
    private val surveyDao: SurveyDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var recording: Recording? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private lateinit var cameraExecutor: ExecutorService
    val stopRecording = mutableStateOf(false)
    val resumeRecording = mutableStateOf(false)
    val CURRENTSURVEYID = mutableStateOf(0L)

    private val _state = mutableStateOf(RecordState())
    val state = _state


    init {
        savedStateHandle.get<String>(SURVEY)?.let { surveyId ->
            CURRENTSURVEYID.value = surveyId.toLong()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    private fun getElementsToParse(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            val route: MutableList<RoutePoint> = mutableListOf()
            elementDao.getElementsFromSurvey(CURRENTSURVEYID.value).collect {
                for (i in it) {
                    route.add(RoutePoint(lat = i.latitude, lon = i.longitude, name = i.name))
                }
                parseGfx(context, route)
            }
        }
    }

    private fun parseGfx(context: Context, routePoints: List<RoutePoint>) {
        CoroutineScope(Dispatchers.Default).launch {
            _state.value = _state.value.copy(loading = true)
            try {
                Log.d("GPX", "Creating gpx")
                val gpx = Gpx(
                    creator = "RideBeeline",
                    metadata = Metadata(name = "Example"),
                    routes = Observable.fromArray(
                        (Route(
                            points = Observable.fromIterable(routePoints)
                        ))
                    )
                )

                val extDir: String = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath ?: context.filesDir.absolutePath
                val outputStream = File(extDir, "Relevamientos-Gpx")
                if (!outputStream.exists()) {
                    outputStream.mkdir();
                }
                val writer: Single<Writer> = gpx.writeTo(
                    File(
                        outputStream,
                        "relevamiento${CURRENTSURVEYID.value}.gpx"
                    ).writer()
                )

                val singleObserver = object : SingleObserver<Writer> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("onSubscribe", d.toString())
                    }

                    override fun onSuccess(t: Writer) {
                        Log.d("onSucces", outputStream.absolutePath.toString())
                        _state.value = _state.value.copy(loading = false, isSuccessGpxSaved = true)
                    }

                    override fun onError(e: Throwable) {
                        _state.value =
                            _state.value.copy(
                                loading = false,
                                errorMsg = e.localizedMessage?.toString()
                            )
                    }

                }
                writer.subscribeWith(singleObserver)
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(loading = false, errorMsg = e.localizedMessage?.toString())
            }


        }
    }

    fun insertElement(element: ElementEntity, context: Context) {
        viewModelScope.launch {
            elementDao.insertElement(element)
        }
        Toast.makeText(
            context,
            "${element.name} insertado en: Lat:${element.latitude} Lng: ${element.longitude}",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun startCamera(context: Context, lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        CoroutineScope(Dispatchers.Default).launch {
            _state.value = _state.value.copy(loading = true)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                val recorder = Recorder.Builder()
                    .setQualitySelector(QualitySelector.from(Quality.HD))
                    .build()
                videoCapture = withOutput(recorder)
                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()
                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, videoCapture
                    )
                    _state.value = _state.value.copy(cameraStarted = true, loading = false)
                } catch (exc: Exception) {
                    _state.value =
                        _state.value.copy(
                            loading = false,
                            errorMsg = exc.localizedMessage?.toString()
                        )
                    Log.e("TAG", "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(context))
        }

    }

    @SuppressLint("RestrictedApi")
    fun captureVideo(context: Context, nav: NavController) {
        val videoCapture = this.videoCapture ?: return
        val curRecording = recording
        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            recording = null
            return
        }
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                "Relevamiento $name,ID:${CURRENTSURVEYID.value}"
            )
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(Video.Media.RELATIVE_PATH, "Movies/Relevamientos-Videos")
            }
        }
        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        recording = videoCapture.output
            .prepareRecording(context, mediaStoreOutputOptions)
            .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        _state.value = _state.value.copy(isRecording = true, isPaused = false)
                    }
                    is VideoRecordEvent.Resume -> {
                        _state.value = _state.value.copy(
                            isRecording = false,
                            isPaused = true
                        )
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            CoroutineScope(Dispatchers.Default).launch {
                                getElementsToParse(context)
                                parseVideoUri(recordEvent.outputResults.outputUri)
                            }
                            _state.value = _state.value.copy(
                                isRecording = false,
                                isSuccessVideoRecorded = true,
                                isPaused = false,
                                loading = false
                            )
                            val msg = "Video guardado con exito en :  " +
                                    "${recordEvent.outputResults.outputUri}"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                                .show()
                            nav.navigate(Destinations.SurveyEndScreen.passSurveyId(CURRENTSURVEYID.value.toString()))
                        } else {
                            recording?.close()
                            recording = null
                            _state.value = _state.value.copy(
                                errorMsg = recordEvent.error.toString(),
                                isRecording = false
                            )
                        }
                    }
                }
            }
    }

    fun stopVideo() {
        recording?.stop()
    }

    fun onDismiss() {
        viewModelScope.launch {
            Log.d("ERRRRRRRRRRRROR", _state.value.errorMsg!!)
            _state.value = _state.value.copy(errorMsg = null)
        }
    }

    private fun parseVideoUri(uri: Uri) {
        viewModelScope.launch {
            try {
                surveyDao.updateVideoUri(CURRENTSURVEYID.value, uri)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMsg = e.message.toString(),
                    loading = false
                )
            }

        }
    }


}