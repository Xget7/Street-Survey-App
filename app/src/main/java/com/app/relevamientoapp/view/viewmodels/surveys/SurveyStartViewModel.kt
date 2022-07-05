package com.app.relevamientoapp.view.viewmodels.surveys

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.relevamientoapp.data.entity.SurveyEntity
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import com.app.relevamientoapp.maps_services.Broadcast
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyStartViewModel @Inject constructor(
    val dao: SurveyDbRepo,
) : ViewModel() {

    val currentLatLng = mutableStateOf(LatLng(0.0, 0.0))
    val state = mutableStateOf(SurveyStarScreenState())


    fun collectLocationBroadcast(context: Context) {
        viewModelScope.launch {
            Broadcast(context).callBack.collect {
                currentLatLng.value = LatLng(it.latitude, it.longitude)
            }
        }
    }




    @SuppressLint("CheckResult")
    fun insertSurvey(navController: NavController) {
        val survey = SurveyEntity(
            startLatitude = currentLatLng.value.latitude,
            startLongitude = currentLatLng.value.longitude,
            endLongitude = 0.0,
            endLatitude = 0.0,
            selectedFormularies = "[]"
        )
        viewModelScope.launch {
            dao.insertSurvey(survey)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(returnId: Long) {

                        navController.navigate(
                            Destinations.RecordScreen.passSurveyId(
                                returnId.toString()
                            )
                        )
                        state.value = state.value.copy(isLoading = false, isSuccess = true)
                    }

                    override fun onError(e: Throwable) {
                        state.value = state.value.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMsg = e.localizedMessage
                        )
                    }

                })
        }
    }

    fun onDismiss(){
        viewModelScope.launch {
            state.value = state.value.copy(errorMsg = null)
        }
    }
}