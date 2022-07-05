package com.app.relevamientoapp.view.viewmodels.formularies

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormularyScreenViewModel @Inject constructor(
    val surveyDao: SurveyDbRepo,
    val formularyDao: FormularyDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val selectedFormulariesLIst = mutableListOf<FormularyEntity>()

    //TODO("PAsar a consts")
    val formulariesList =
        mutableListOf(
            FormularyScreenModel(
                Constants.SIDEWALKS,
                R.drawable.walk,
                "Continuidad de aceras.",
                "Describe la continuidad de la infraestructura destinada a peatones.",
                0f,
                listOf(
                    "Continuidad en el 100% de la acera",
                    "Continuidad hasta el 75% de la longitud",
                    "Continuidad al 50% de la longitud",
                    "Continuidad al 25% de la longitud",
                    "Continuidad menos del 25% de la longitud",
                    "No existe acera"
                ).asReversed()

            ),
            FormularyScreenModel(
                Constants.OBSTACLES,
                R.drawable.obstaculos,
                "Obstáculos en aceras.",
                "Describe la presencia de obstáculos en las aceras,andenes o espacios para peatones del Acceso.",
                0f,
                listOf(
                    "No existen elementos de obstrucción a la circulación",
                    "Existen postes, árboles o elementos similares a los extremos de la acera que no obstruyen la circulación",
                    "Existen elementos que obligan el cambio de dirección depeatones en aceras",
                    "Existe ocupación de acera por estacionamiento ",
                    "Existe ocupación de aceras por comercio",
                    "No es posible la circulación debido a obstáculos o no existeacera"
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.CIRCULATION_COMFORT,
                R.drawable.comodidadcirculacion,
                "Comodidad de circulacion.",
                "Describe la comodidad de la circulacion de la acera, anden o espacio ya sea por superficie o estado.",
                0f,
                listOf(
                    "Las aceras tienen una superficie regular que facilita la circulación",
                    "Existen tramos irregulares que no inciden en la comodidad general",
                    "Al menos la mitad de la longitud presenta condiciones mínimas dcirculación peatonal",
                    "La acera no presenta condiciones de comodidad por mal estado.",
                    "La acera es de material afirmado (tierra)",
                    "No existe acera o presenta irregularidades de dificil circulación",
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.INFRAESTRUCTURE,
                R.drawable.arbol,
                "Estado de acera",
                "Describe el estado aparente de la acera o acceso.",
                0f,
                listOf(
                    "Estado aparente excelente",
                    "Estado aparente muy bueno",
                    "Estado aparente bueno",
                    "Estado aparente regular",
                    "Estado aparente malo",
                    "No existe"
                ).asReversed()
            ),
            FormularyScreenModel(
                Constants.ACCESSIBILITY,
                R.drawable.discapacitado,
                "Accecibiliad universal",
                "Describe el grado de accesibilidad universal del tramo evaluado.",
                0f,
                listOf(
                    "El tramo tiene baldozas podotáctiles, libres de obstáculos, ancho suficiente para una silla de ruedas y rampas protegidas al inicio y final del tramo.",
                    "El tramo tiene baldozas podotáctiles, libres de obstáculos, y el ancho mínimo suficiente para una silla de ruedas en gran parte del tramo,cuenta con rampas al inicio y final",
                    "El tramo tiene baldozas podotáctiles, libres de obstáculos, y el ancho mínimo suficiente para una silla de ruedas en al menos el 50% deltramo, cuenta con rampas al inicio y final\n",
                    "El tramo tiene el ancho mínimo para una silla de ruedas en toda su extensión, existen algunos obstáculos y rampas en inicio y fin",
                    "El tramo sólo cuenta con rampas a inicio y fin",
                    "No existe ninguna facilidad de accesibilidad universal"
                ).asReversed()
            )

        )

    val currentRatings = mutableListOf<MutableState<Float>>()


    private var currentSurvey : SurveyEntity? = null

    val openDialog = mutableStateOf(false)


    private val _state : MutableState<FormularyScreenState> = mutableStateOf(FormularyScreenState())
    val state = _state

    val userFormularyList : MutableList<FormularyScreenModel> = mutableListOf()
    private val mapOfFormularies = mapOf(
         Constants.SIDEWALKS to formulariesList[0] ,
         Constants.OBSTACLES to formulariesList[1] ,
        Constants.CIRCULATION_COMFORT to formulariesList[2],
         Constants.INFRAESTRUCTURE to formulariesList[3],
        Constants.ACCESSIBILITY to formulariesList[4] ,
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