package com.app.relevamientoapp.data.repository

import android.net.Uri
import com.app.relevamientoapp.data.dao.SurveyDao
import com.app.relevamientoapp.data.entity.SurveyEntity
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Callable
import javax.inject.Inject
import kotlin.math.ln

class SurveyDbRepo @Inject constructor(
    private val dao: SurveyDao
) {

    fun insertSurvey(surveyEntity: SurveyEntity): Single<Long>? {
        return Single.fromCallable(
            Callable<Long> { dao.insertSurvey(surveyEntity) }
        )
    }

    fun getAllSurveys(): Flow<List<SurveyEntity>> {
        return dao.getAllSurveys()
    }

    suspend fun updateLatAndLong(id: Long, lat: Double, lng: Double) {
        dao.updateLatAndLong(id, lat, lng)
    }

    suspend fun updateVideoUri(id: Long, videUri: Uri) {
        dao.updateVideoUri(id, videUri.toString())
    }

    suspend fun updateOrPlaceFormulariesList(id: Long, formulariesToEvaluate: List<String>) {
        dao.updateSelectedFormularies(id, formulariesToEvaluate)
    }


    suspend fun getSurveyById(id: Long): SurveyEntity {
        return dao.getSurveyById(id)
    }


}