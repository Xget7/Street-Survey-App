package com.app.relevamientoapp.data.models

data class FormularyScreenModel(
    val formularyName : String,
    val descriptionPhoto : Int,
    val descriptionPhotoText : String,
    val title : String,
    var rating : Float = 0f,
    val evaluation_criteria : List<String>
)
