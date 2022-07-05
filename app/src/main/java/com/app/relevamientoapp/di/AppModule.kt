package com.app.relevamientoapp.di


import android.content.Context
import com.app.relevamientoapp.data.dao.ElementDao
import com.app.relevamientoapp.data.dao.FormularyDao
import com.app.relevamientoapp.data.dao.RoomSurveyDb
import com.app.relevamientoapp.data.dao.SurveyDao
import com.app.relevamientoapp.data.repository.ElementDbRepo
import com.app.relevamientoapp.data.repository.FormularyDbRepo
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideSurveyDb(
        @ApplicationContext app: Context
    ) =  RoomSurveyDb.invoke(app)
     // The reason we can construct a database for the repo


    @Provides
    @Singleton
    fun surveyDao(db: RoomSurveyDb): SurveyDao = db.surveyDao()


    @Provides
    @Singleton
    fun elementDao(db: RoomSurveyDb): ElementDao = db.elementDao()


    @Provides
    @Singleton
    fun formularyDao(db: RoomSurveyDb): FormularyDao = db.formularyDao()




//    @Provides
//    @Singleton
//    fun provideUseCases(surveysRepoImpl: SurveysRepoImpl): SurveyUseCases {
//        return SurveyUseCases(
//            locationUpdatesUseCase = LocationUpdatesUseCase()
//        )
//    }
}
