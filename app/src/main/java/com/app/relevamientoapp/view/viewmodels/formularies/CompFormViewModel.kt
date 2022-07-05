package com.app.relevamientoapp.view.viewmodels.formularies


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.entity.FormularyEntity
import com.app.relevamientoapp.data.entity.SurveyEntity
import com.app.relevamientoapp.data.models.FormularyScreenModel
import com.app.relevamientoapp.data.repository.FormularyDbRepo
import com.app.relevamientoapp.data.repository.SurveyDbRepo
import com.app.relevamientoapp.utilities.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompFormViewModel @Inject constructor(
    val surveyDao: SurveyDbRepo,
    val formularyDao: FormularyDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val formulariesList =
        mutableListOf(
            FormularyScreenModel(
                Constants.SIDEWALKS_CONDITION,
                R.drawable.estadocalzada,
                "Estado de la calzada",
                "Describe el estado aparente de la calzada vehicular del acceso.",
                0f,
                listOf(
                    "Estado aparente excelente",
                    "Estado aparente Muy bueno",
                    "Estado aparente bueno",
                    "Estado aparente regular",
                    "Estado aparente malo",
                    "No existe"
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.DEMARCATION,
                R.drawable.demarcacion,
                "Demarcación.",
                "Describe el estado de presencia de la demarcación del piso.",
                0f,
                listOf(
                    "Demarcación existente, nueva o en estado excelente",
                    "Demarcación existente y en buen estado",
                    "Demarcación existente en estado regular",
                    "Demarcación existente, legible",
                    "Demarcación desgastada o insuficiente",
                    "No existe demarcación"
                //TODO("ACENTOS")
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.VERTICAL_SIGNALS,
                R.drawable.roadsigns,
                "Señalización",
                "Describe el estado y presencia de señalización vertical.",
                0f,
                listOf(
                    "Señalización existente, nueva o en estado excelente",
                    "Señalización existente y en buen estado",
                    "Señalización existente en estado regular",
                    "Señalización existente, legible",
                    "Señalización desgastada o insuficiente",
                    "No existe Señalización"
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.VELOCITY_SIGN,
                R.drawable.speedlimit,
                "Gestión de velocidad",
                "Describe si existen medidas de gestión de velocidad en el acceso.",
                0f,
                listOf(
                    "Existe algun elemento o dispositivo de control o gestion de la velocidad maxima",
                    "La velocidad del tramo está claramente definida y señalizada",
                    "La velocidad del tramo no está definida pero existen elementos de control de velocidad",
                    "Existen elementos de reducción de velocidad, aunque no completamente señalizados",
                    "Existen elementos de reducción de velocidad, no señalizados ",
                    "No existe definición de velocidad máxima de operación.",
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.PARKINGS,
                R.drawable.parking,
                "Estacionamiento",
                "Describe la existencia y regulacion del Estacionamiento del acceso.",
                0f,
                listOf(
                    "Existe control de estacionamientos por la autoridad acorde a señalización",
                    "El estacionamiento esta¡ definido, regulado y señalizado",
                    "El estacionamiento es esporádico, aunque no permitido",
                    "El estacionamiento ha invadido aceras u otros accesos importantes",
                    "Existe estacionamiento en doble fila o detenciones abruptas",
                    "El estacionamiento genera interrupciones o situaciones de riesgo en calzada y/o acera"
                ).asReversed()
            ),


        )
    val openDialog = mutableStateOf(false)
    var currentSurvey : SurveyEntity? = null
    val selectedFormulariesLIst = mutableListOf<FormularyEntity>()


    private val _state : MutableState<FormularyScreenState> = mutableStateOf(FormularyScreenState())
    val state = _state

    val userFormularyList : MutableList<FormularyScreenModel> = mutableListOf()

    val mapOfFormularies = mapOf(
        Constants.SIDEWALKS_CONDITION to formulariesList[0] ,
        Constants.DEMARCATION to formulariesList[1] ,
        Constants.VERTICAL_SIGNALS to formulariesList[2],
        Constants.VELOCITY_SIGN to formulariesList[3],
        Constants.PARKINGS to formulariesList[4] ,
    )

    val CURRENTSURVEY_ID = mutableStateOf(0L)
    val formulariesSelected: MutableList<String> = mutableListOf()

    init {
        savedStateHandle.get<String>(Constants.SURVEY)?.let { surveyId ->
            CURRENTSURVEY_ID.value = surveyId.toLong()
            getFormularyWithSurvey()
        }
    }

    private fun getFormularyWithSurvey(){
        viewModelScope.launch {
            val _currentSurvey = surveyDao.getSurveyById(CURRENTSURVEY_ID.value)
            currentSurvey = _currentSurvey
            val list : List<String?> = Constants.Converters().restoreList(_currentSurvey.selectedFormularies)!!
            for (i in list){
                mapOfFormularies[i]?.let { userFormularyList.add(it) }
            }

        }
    }

    fun insertCurrentFormulary(formulary: FormularyEntity){
        viewModelScope.launch {
            try {
                formularyDao.insertFormulary(formulary)

            }catch (e : Exception){
                _state.value = _state.value.copy(errorMsg = e.localizedMessage)
            }

        }
    }

    fun returnIndexAddition(item:  FormularyScreenModel): Int{
        val listLastIndex = userFormularyList.lastIndex
        val index = userFormularyList.indexOf(item)
        if (index >= listLastIndex){
            openDialog.value = true
        }
        return index + 1
    }

    fun returnIndexSubstraction(item:  FormularyScreenModel ): Int{
        val listLastIndex = userFormularyList.lastIndex
        val index = userFormularyList.indexOf(item)
        if (index <= 0){
            return 0
        }
        return index - 1
    }

}