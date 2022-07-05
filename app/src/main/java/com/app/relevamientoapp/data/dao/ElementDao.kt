package com.app.relevamientoapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.relevamientoapp.data.entity.ElementEntity
import com.app.relevamientoapp.data.entity.SurveyEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ElementDao {

    @Insert
    suspend fun insertElement(elementEntity: ElementEntity)

    @Query( "SELECT * FROM elements")
    fun getAllElements(): Flow<List<ElementEntity>>

    @Query("SELECT * FROM elements WHERE survey_parent_id =:surveyId")
    fun getElementsFromSurvey(surveyId : Long) : Flow<List<ElementEntity>>

}