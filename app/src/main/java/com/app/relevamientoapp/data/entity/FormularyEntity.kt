package com.app.relevamientoapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "formulary", indices = [Index("formulary_id"), Index("survey_parent_id")], foreignKeys = [
        ForeignKey(entity = SurveyEntity::class, parentColumns = ["survey_id"] , childColumns = ["survey_parent_id"] )
    ]
)
data class FormularyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "formulary_id")
    val formularyId: Long = 0,

    @ColumnInfo(name = "survey_parent_id")
    val surveyParentId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "comment")
    val comment: String,

)