package com.app.relevamientoapp.data.dao

import android.net.Uri
import androidx.room.*
import com.app.relevamientoapp.data.entity.SurveyEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow


@Dao
interface SurveyDao {
    //------------------Methods-------------------------\\
    @Insert
    fun insertSurvey(surveyEntity: SurveyEntity): Long

    @Query("SELECT * FROM surveys")
    fun getAllSurveys(): Flow<List<SurveyEntity>>


    @Query("UPDATE surveys SET end_latitude = :lat,end_longitude =:lng WHERE survey_id = :id ")
    suspend fun updateLatAndLong(id: Long, lat: Double, lng: Double)

    @Query("SELECT * FROM surveys WHERE survey_id = :id")
    suspend fun getSurveyById(id: Long): SurveyEntity

    @Query("UPDATE surveys SET video_uri = :videoUri WHERE survey_id = :id ")
    suspend fun updateVideoUri(id: Long, videoUri: String)


    @Query("UPDATE surveys SET selected_formularies= :selectedFormularies WHERE survey_id = :id ")
    suspend fun updateSelectedFormularies(id: Long, selectedFormularies: List<String>)



}