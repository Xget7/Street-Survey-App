package com.app.relevamientoapp.data.repository

import com.app.relevamientoapp.data.dao.FormularyDao
import com.app.relevamientoapp.data.entity.FormularyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FormularyDbRepo @Inject constructor(
    private val dao: FormularyDao
)  {

     suspend fun insertFormulary(formularyEntity: FormularyEntity) {
        dao.insertFormulary(formularyEntity)
    }

     fun getAllFormularies(): Flow<List<FormularyEntity>> {
        return dao.getAllFormularies()
    }

    fun getFormulariesById(id: Long): Flow<List<FormularyEntity>> {
        return dao.getFormulariesById(id)
    }
}