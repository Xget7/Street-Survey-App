package com.app.relevamientoapp.view.viewmodels.surveys

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import com.app.relevamientoapp.maps_services.Broadcast
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.utilities.common.Constants
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyEndViewModel @Inject constructor(
    val dao: SurveyDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentLatLng = mutableStateOf(LatLng(0.0, 0.0))
    val state = mutableStateOf(SurveyStarScreenState())
    val CURRENTSURVEY_ID = mutableStateOf(0L)
    val CURRENTSURVEY_LAT_LNG = mutableStateOf(LatLng(0.0, 0.0))


    init {
        savedStateHandle.get<String>(Constants.SURVEY)?.let { surveyId ->
            CURRENTSURVEY_ID.value = surveyId.toLong()
            if (CURRENTSURVEY_ID.value != 0L) {
                getSurveyStartLatLng()
            }
        }

    }

    fun collectLocationBroadcast(context: Context) {
        viewModelScope.launch {
            Broadcast(context).callBack.collect {
                currentLatLng.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    private fun getSurveyStartLatLng() {
        viewModelScope.launch {
            try {
                val survey = dao.getSurveyById(CURRENTSURVEY_ID.value)
                CURRENTSURVEY_LAT_LNG.value = LatLng(survey.startLatitude, survey.startLongitude)

            } catch (e: Exception) {
                state.value = state.value.copy(errorMsg = e.message)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun updateSurveyLatLng(navController: NavController) {
        viewModelScope.launch {
            try {
                dao.updateLatAndLong(
                    CURRENTSURVEY_ID.value,
                    currentLatLng.value.latitude,
                    currentLatLng.value.longitude
                )
                Log.d("Updated survey: ", "View in app isnpection")
                state.value = state.value.copy(isSuccess = true)
                delay(1500)
                navController.navigate(
                    Destinations.FormularySelectionScreen.passSurveyId(
                        CURRENTSURVEY_ID.value.toString()
                    )
                )
            } catch (e: Exception) {
                state.value = state.value.copy(errorMsg = e.message)
            }


        }

    }
}