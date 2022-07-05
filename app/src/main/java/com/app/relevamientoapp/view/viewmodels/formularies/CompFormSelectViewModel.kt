package com.app.relevamientoapp.view.viewmodels.formularies


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.models.ImageTextItemSelectable
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.utilities.common.Constants
import com.app.relevamientoapp.utilities.common.Constants.ACCESSIBILITY
import com.app.relevamientoapp.utilities.common.Constants.CIRCULATION_COMFORT
import com.app.relevamientoapp.utilities.common.Constants.DEMARCATION
import com.app.relevamientoapp.utilities.common.Constants.INFRAESTRUCTURE
import com.app.relevamientoapp.utilities.common.Constants.OBSTACLES
import com.app.relevamientoapp.utilities.common.Constants.PARKINGS
import com.app.relevamientoapp.utilities.common.Constants.SIDEWALKS
import com.app.relevamientoapp.utilities.common.Constants.SIDEWALKS_CONDITION
import com.app.relevamientoapp.utilities.common.Constants.VELOCITY_SIGN
import com.app.relevamientoapp.utilities.common.Constants.VERTICAL_SIGNALS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompFormSelectViewModel @Inject constructor(
    val surveyDao: SurveyDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val CURRENTSURVEY_ID = mutableStateOf(0L)
    val list = mutableListOf<ImageTextItemSelectable>(
        ImageTextItemSelectable(
            R.drawable.estadocalzada,
            "Estado de calzada",
            SIDEWALKS_CONDITION,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.demarcacion,
            "Demarcacion",
            DEMARCATION,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.roadsigns,
            "Señales verticales",
            VERTICAL_SIGNALS,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.parking,
            "Estacionamientos",
            PARKINGS,
            mutableStateOf(false)
        ),
        ImageTextItemSelectable(
            R.drawable.speedlimit,
            "Gestion de velocidad",
            VELOCITY_SIGN,
            mutableStateOf(false)
        )
    )
    private val _state : MutableState<CompFormState> = mutableStateOf(CompFormState())
    val state = _state

    init {
        savedStateHandle.get<String>(Constants.SURVEY)?.let { surveyId ->
            CURRENTSURVEY_ID.value = surveyId.toLong()
        }
    }


    fun insertSelectedFormularies(selectedFormularies: List<String>, navController: NavController) {
        viewModelScope.launch {
            try {
                surveyDao.updateOrPlaceFormulariesList(CURRENTSURVEY_ID.value, selectedFormularies)
                navController.navigate(Destinations.ComplementaryFormularyScreen.passSurveyId(CURRENTSURVEY_ID.value.toString()))
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMsg = e.message)
                Log.d("Exceptiom", e.message.toString())
            }
        }
    }


    fun anyItemSelected(list: List<ImageTextItemSelectable>): Boolean {
        return !list.none { it.isSelected.value }
    }
}