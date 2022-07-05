package com.app.relevamientoapp.data.dao

import android.content.Context
import androidx.room.*
import com.app.relevamientoapp.data.entity.SurveyEntity
import com.app.relevamientoapp.data.entity.FormularyEntity
import com.app.relevamientoapp.data.entity.ElementEntity
import com.app.relevamientoapp.utilities.common.Constants

@Database(
    entities = [SurveyEntity::class, ElementEntity::class, FormularyEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(Constants.Converters::class)
abstract class RoomSurveyDb: RoomDatabase() {

    abstract fun surveyDao() : SurveyDao
    abstract fun elementDao() : ElementDao
    abstract fun formularyDao() : FormularyDao

    companion object {
        @Volatile
        private var instance : RoomSurveyDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, RoomSurveyDb::class.java, "AppSurveyDB.db").build()
    }
}