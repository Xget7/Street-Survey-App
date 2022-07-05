package com.app.relevamientoapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "surveys", indices = [Index("survey_id")])
data class SurveyEntity(

    @ColumnInfo(name = "video_uri")
    var videoUri: String = "",

    @ColumnInfo(name = "start_longitude")
    val startLongitude: Double,

    @ColumnInfo(name = "selected_formularies")
    val selectedFormularies : String,

    @ColumnInfo(name = "start_latitude")
    val startLatitude: Double,

    @ColumnInfo(name = "end_longitude")
    val endLongitude: Double,

    @ColumnInfo(name = "end_latitude")
    val endLatitude: Double,

    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "survey_id")
    var surveyId: Long = 0
}