package com.app.relevamientoapp.view.viewmodels

data class RecordState(
    val loading : Boolean? = false,
    val isRecording : Boolean? = false,
    val isPaused : Boolean? = false,
    val cameraStarted : Boolean? = false,
    val isSuccessVideoRecorded : Boolean? = false,
    val isSuccessGpxSaved : Boolean? = false,
    val errorMsg: String? = null
)
