package com.app.relevamientoapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.relevamientoapp.data.entity.FormularyEntity
import com.app.relevamientoapp.data.entity.SurveyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FormularyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulary(formularyEntity: FormularyEntity)

    @Query( "SELECT * FROM formulary")
    fun getAllFormularies(): Flow<List<FormularyEntity>>

    @Query( "SELECT * FROM formulary WHERE survey_parent_id = :id")
    fun getFormulariesById(id: Long): Flow<List<FormularyEntity>>

}