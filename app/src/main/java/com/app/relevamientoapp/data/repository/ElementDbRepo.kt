package com.app.relevamientoapp.data.repository

import com.app.relevamientoapp.data.dao.ElementDao
import com.app.relevamientoapp.data.dao.SurveyDao
import com.app.relevamientoapp.data.entity.ElementEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElementDbRepo @Inject constructor(
    private val dao: ElementDao
) {

     suspend fun insertElement(elementEntity: ElementEntity) {
        dao.insertElement(elementEntity)
    }

     fun getAllElements(): Flow<List<ElementEntity>> {
        return dao.getAllElements()
    }

    fun getElementsFromSurvey(surveyId : Long) : Flow<List<ElementEntity>>{
        return dao.getElementsFromSurvey(surveyId)
    }


}