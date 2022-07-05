package com.app.relevamientoapp.domain.camerax

import android.Manifest
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.camera.video.VideoRecordEvent
import com.app.relevamientoapp.R
import java.io.File
import java.util.*

/**
 * A helper extended function to get the name(string) for the VideoRecordEvent.
 */
fun VideoRecordEvent.getNameString() : String {
    return when (this) {
        is VideoRecordEvent.Status -> "Status"
        is VideoRecordEvent.Start -> "Started"
        is VideoRecordEvent.Finalize-> "Finalized"
        is VideoRecordEvent.Pause -> "Paused"
        is VideoRecordEvent.Resume -> "Resumed"
        else -> throw IllegalArgumentException("Unknown VideoRecordEvent: $this")
    }
}

object CamConfig {
    private const val FILENAME = "yyyy_MM_dd_HH_mm_ss"
    private const val VIDEO_EXTENSION = ".mp4"

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = appContext.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else appContext.filesDir
    }

    fun createFile(baseFolder: File, format: String, extension: String) =
        File(baseFolder, SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension)
}
