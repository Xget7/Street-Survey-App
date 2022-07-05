package com.app.relevamientoapp.view.screens.surveyActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.location.Location
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.app.relevamientoapp.R.*
import com.app.relevamientoapp.data.entity.ElementEntity
import com.app.relevamientoapp.maps_services.Broadcast
import com.app.relevamientoapp.utilities.common.Constants.ImageAndTextitems
import com.app.relevamientoapp.view.screens.components.ImageText
import com.app.relevamientoapp.view.viewmodels.RecordScreenViewModel

@Composable
fun RecordScreen(
    navController: NavController,
    viewModel: RecordScreenViewModel = hiltViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current


    SimpleCameraPreview(
        lifecycleOwner,
        context,
        navController
    )

}


@SuppressLint("UnsafeOptInUsageError", "RestrictedApi")
@Composable
fun SimpleCameraPreview(
    lifecycleOwner: LifecycleOwner,
    context: Context,
    navController: NavController,
    viewModel: RecordScreenViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    val latLngInit = remember {
        Broadcast(context).callBack
    }
    val currentLocation = latLngInit.collectAsState(initial = Location(""))
    val resumeRecord = remember {
        viewModel.resumeRecording
    }
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            BoxWithConstraints(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(10.dp)
                    .padding(top = 40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(240.dp)
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx)
                                viewModel.startCamera(ctx, lifecycleOwner, previewView)

                                previewView
                            },
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(240.dp),
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        Log.d(
                                            "CLicked",
                                            "isrecording? = ${viewModel.state.value.isRecording}"
                                        )
                                        if (viewModel.state.value.isRecording == false) {
                                            viewModel.captureVideo(context, navController)

                                        } else {
                                            viewModel.stopVideo()
                                        }
                                    },
                                painter = painterResource(id = if (viewModel.state.value.isRecording == false) drawable.initrecording else drawable.stoprecording),
                                contentDescription = "Init and Stop Video Icons",
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(20.dp))

                    if (currentLocation.value.longitude == 0.0 && currentLocation.value.latitude == 0.0) {
                        CircularProgressIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            columns = GridCells.Adaptive(60.dp),
                        ) {
                            items(ImageAndTextitems) {
                                ImageText(imageResource = it.imageResource, imageText = it.imageText) {

                                    val currentElement = ElementEntity(
                                        surveyParentId = viewModel.CURRENTSURVEYID.value,
                                        name = it.imageText,
                                        latitude = currentLocation.value.latitude,
                                        longitude = currentLocation.value.longitude
                                    )
                                    viewModel.insertElement(currentElement,context)
                                }
                            }

                        }
                    }
                    if (viewModel.state.value.errorMsg != null){
                        AlertDialog(
                            onDismissRequest = {
                                viewModel.onDismiss()
                            },
                            title = {
                                Text(text = "Error!")
                            },
                            text = {
                                Text(viewModel.state.value.errorMsg!!)
                            },
                            confirmButton = {
                                Button(onClick = {
                                    viewModel.onDismiss()
                                }){
                                    Text(text = "Ok")
                                }
                            }
                        )
                    }


                }
            }


        }
        Configuration.ORIENTATION_PORTRAIT ->{
            BoxWithConstraints(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(10.dp)
                    .padding(top = 40.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {


                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx)
                                viewModel.startCamera(ctx, lifecycleOwner, previewView)

                                previewView
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp),
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        Log.d(
                                            "CLicked",
                                            "isrecording? = ${viewModel.state.value.isRecording}"
                                        )
                                        if (viewModel.state.value.isRecording == false) {
                                            viewModel.captureVideo(context, navController)

                                        } else {
                                            viewModel.stopVideo()
                                        }
                                    },
                                painter = painterResource(id = if (viewModel.state.value.isRecording == false) drawable.initrecording else drawable.stoprecording),
                                contentDescription = "Init and Stop Video Icons",
                            )
                        }

                    }

                    if (currentLocation.value.longitude == 0.0 && currentLocation.value.latitude == 0.0) {
                        CircularProgressIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            columns = GridCells.Adaptive(50.dp),
                        ) {
                            items(ImageAndTextitems) {
                                ImageText(imageResource = it.imageResource, imageText = it.imageText) {

                                    val currentElement = ElementEntity(
                                        surveyParentId = viewModel.CURRENTSURVEYID.value,
                                        name = it.imageText,
                                        latitude = currentLocation.value.latitude,
                                        longitude = currentLocation.value.longitude
                                    )
                                    viewModel.insertElement(currentElement,context)
                                }
                            }

                        }
                    }
                    if (viewModel.state.value.errorMsg != null){
                        AlertDialog(
                            onDismissRequest = {
                                viewModel.onDismiss()
                            },
                            title = {
                                Text(text = "Error!")
                            },
                            text = {
                                Text(viewModel.state.value.errorMsg!!)
                            },
                            confirmButton = {
                                Button(onClick = {
                                    viewModel.onDismiss()
                                }){
                                    Text(text = "Ok")
                                }
                            }
                        )
                    }


                }
            }
        }

    }




}