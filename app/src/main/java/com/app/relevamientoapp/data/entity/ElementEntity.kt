package com.app.relevamientoapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "elements", indices = [Index("element_id"), Index("survey_parent_id")], foreignKeys = [
        ForeignKey(
            entity = SurveyEntity::class,
            parentColumns = ["survey_id"],
            childColumns = ["survey_parent_id"]
        )
    ]
)
data class ElementEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "element_id")
    val elementId: Long = 0,

    // FK
    @ColumnInfo(name = "survey_parent_id")
    val surveyParentId: Long,

    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
)