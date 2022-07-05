package com.app.relevamientoapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.relevamientoapp.data.entity.FormularyEntity
import com.app.relevamientoapp.data.models.FormularyScreenModel
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.utilities.common.Constants
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    private val vehicularFormularyList: MutableList<FormularyEntity> = mutableListOf()


    val mapOfAcera = mapOf(
        Constants.SIDEWALKS to "Acera - Continuidad",
        Constants.OBSTACLES to "Acera - Obstaculos",
        Constants.CIRCULATION_COMFORT to "Acera - Comodidad",
        Constants.INFRAESTRUCTURE to "Acera - Infraestructura",
        Constants.ACCESSIBILITY to "Acera - Accesibiliad",
    )

    private var index = 0


    @Test
    fun returnIndex() {
        vehicularFormularyList.add(
            FormularyEntity(
                1,
                1,
                Constants.SIDEWALKS,
                3,
                ""
            )
        )
        vehicularFormularyList.add(
            FormularyEntity(
                2,
                5,
                Constants.OBSTACLES,
                2,
                ""
            )
        )
        vehicularFormularyList.add(
            FormularyEntity(
                4,
                2,
                Constants.CIRCULATION_COMFORT,
                4,
                ""
            )
        )
        vehicularFormularyList.add(
            FormularyEntity(
                1,
                1,
                Constants.INFRAESTRUCTURE,
                1,
                ""
            )
        )

        val calzadaMap = vehicularFormularyList.map { list ->  mapOfAcera[list.name] }

        print(calzadaMap)
    }


}
