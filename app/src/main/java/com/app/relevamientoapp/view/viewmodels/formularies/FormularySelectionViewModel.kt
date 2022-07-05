package com.app.relevamientoapp.view.viewmodels.formularies

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.models.ImageTextItemSelectable
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import com.app.relevamientoapp.maps_services.Broadcast
import com.app.relevamientoapp.maps_services.ForegroundLocationService
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.utilities.common.Constants
import com.app.relevamientoapp.view.viewmodels.graphFinish.GraphFinishState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormularySelectionViewModel @Inject constructor(
    val surveyDao: SurveyDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val CURRENTSURVEY_ID = mutableStateOf(0L)
    val list = mutableListOf<ImageTextItemSelectable>(
        ImageTextItemSelectable(
            R.drawable.walk,
            "Continuidad de aceras",
            Constants.SIDEWALKS,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.signage,
            "Obstaculos en  aceras",
            Constants.OBSTACLES,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.comodidadcirculacion,
            "Comodidad de circulacion",
            Constants.CIRCULATION_COMFORT,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.infrastructura,
            "Estado de infraestructura",
            Constants.INFRAESTRUCTURE,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.discapacitado,
            "Accecibilidad universal",
            Constants.ACCESSIBILITY,
            mutableStateOf(false)
        )
    )


    init {
        savedStateHandle.get<String>(Constants.SURVEY)?.let { surveyId ->
            CURRENTSURVEY_ID.value = surveyId.toLong()
        }

    }

    fun insertSelectedFormularies(selectedFormularies: List<String>, navController: NavController) {
        viewModelScope.launch {
            try {
                surveyDao.updateOrPlaceFormulariesList(CURRENTSURVEY_ID.value, selectedFormularies)
                navController.navigate(Destinations.FormularyScreen.passSurveyId(CURRENTSURVEY_ID.value.toString()))
            } catch (e: Exception) {
                Log.d("Exceptiom", e.message.toString())
            }
        }
    }

    fun stopLocationService(context: Context){
        viewModelScope.launch {
            val intent = Intent(context.applicationContext, ForegroundLocationService::class.java)
            intent.action = Constants.ACTION_STOP_LOCATION_SERVICE
            context.stopService(intent)
        }

    }


    fun anyItemSelected(list: List<ImageTextItemSelectable>): Boolean {
        return !list.none { it.isSelected.value }
    }
}