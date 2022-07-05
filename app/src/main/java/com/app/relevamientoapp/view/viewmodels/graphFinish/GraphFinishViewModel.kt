package com.app.relevamientoapp.view.viewmodels.graphFinish

import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.os.Environment.*
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.relevamientoapp.data.entity.FormularyEntity
import com.app.relevamientoapp.data.entity.SurveyEntity
import com.app.relevamientoapp.data.repository.FormularyDbRepo
import com.app.relevamientoapp.data.repository.SurveyDbRepo
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.hssf.util.HSSFColor.BLUE
import org.apache.poi.ss.usermodel.*
import java.io.File
import javax.inject.Inject


@HiltViewModel
class GraphFinishViewModel @Inject constructor(
    val surveyRepo: SurveyDbRepo,
    val formsRepo: FormularyDbRepo,
    savedStateHandle: SavedStateHandle

) : ViewModel() {
    private var cell: Cell? = null

    val labels = ArrayList<String>()
    val pedestrianAverage = mutableStateOf(0F)
    val vehicularAverage = mutableStateOf(0F)
    private val vehicularFormularyList: MutableList<FormularyEntity> = mutableListOf()
    private val pedestrianFormularyList: MutableList<FormularyEntity> = mutableListOf()
    private val currentSurvey: MutableState<SurveyEntity?> = mutableStateOf(null)
    val CURRENTSURVEY_ID = mutableStateOf(272L)
    val currentFormularies: MutableList<FormularyEntity> = mutableListOf()
    private val _state: MutableState<GraphFinishState> = mutableStateOf(GraphFinishState())
    val state = _state

    init {
        savedStateHandle.get<String>(Constants.SURVEY)?.let { surveyId ->
            CURRENTSURVEY_ID.value = surveyId.toLong()
            getSurveyFormularies()
            getSurvey()
        }
    }

    private fun getSurvey() {
        viewModelScope.launch {
            val _survey = surveyRepo.getSurveyById(CURRENTSURVEY_ID.value)
            currentSurvey.value = _survey
        }
    }

    private fun getSurveyFormularies() {
        viewModelScope.launch {
            formsRepo.getFormulariesById(CURRENTSURVEY_ID.value).collect {
                currentFormularies.addAll(it)
                makePedestrianAverage()
                makeVehicularFormsAverage()
                delay(1000)
                _state.value = _state.value.copy(isSuccessLoadCollections = true)
            }
        }
    }

    private fun makePedestrianAverage() {
        viewModelScope.launch {
            val filteredPedestrian =
                currentFormularies.filter {
                    it.name == SIDEWALKS ||
                            it.name == INFRAESTRUCTURE ||
                            it.name == ACCESSIBILITY ||
                            it.name == OBSTACLES ||
                            it.name == CIRCULATION_COMFORT
                }
            pedestrianFormularyList.addAll(filteredPedestrian)
            val filteredFormularies = filteredPedestrian.map { it.rating }
            val average = filteredFormularies.average()
            pedestrianAverage.value = average.toFloat()

        }
    }

    private fun makeVehicularFormsAverage() {
        viewModelScope.launch {
            val filteredVehicle =
                currentFormularies.filter {
                    it.name == SIDEWALKS_CONDITION ||
                            it.name == DEMARCATION ||
                            it.name == VERTICAL_SIGNALS ||
                            it.name == VELOCITY_SIGN ||
                            it.name == PARKINGS
                }

            vehicularFormularyList.addAll(filteredVehicle)
            val filteredFormularies = filteredVehicle.map { it.rating }
            val average = filteredFormularies.average()
            vehicularAverage.value = average.toFloat()

        }
    }

    fun initRadarChart(barChart: HorizontalBarChart) {

        //remove the description label text located at the lower right corner
        val description = Description()
        description.isEnabled = false
        barChart.description = description

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000)
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000)
        val xAxis: XAxis = barChart.xAxis
        //change the position of x-axis to the bottom
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        //set the horizontal distance of the grid line
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)


        val legend: Legend = barChart.legend
        //setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
        //setting the text size of the legend
        legend.textSize = 11f
        //setting the alignment of legend toward the chart
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        //setting the stacking direction of legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false)

    }

    val mapOfAcera = mapOf(
        SIDEWALKS to "Acera - Continuidad",
        OBSTACLES to "Acera - Obstaculos",
        CIRCULATION_COMFORT to "Acera - Comodidad",
        INFRAESTRUCTURE to "Acera - Infraestructura",
        ACCESSIBILITY to "Acera - Accesibiliad",
    )
    val mapOfCalzada = mapOf(
        SIDEWALKS_CONDITION to "Calzada - Infraestructura ",
        DEMARCATION to "Calzada - Demarcacion horizontal",
        VERTICAL_SIGNALS to "Calzada - Señalizacion vertical",
        VELOCITY_SIGN to "Calzada - Gestion de velocidad",
        PARKINGS to "Calzada -  Estacionamientos",
    )

    fun showRadarChart(chart: HorizontalBarChart) {
        val xAxis = chart.xAxis
        val entries = ArrayList<BarEntry>()

        val sets: ArrayList<IBarDataSet> = arrayListOf()


        Log.d("CURRENTFORMS", currentFormularies.toString())
        //input data

        //fit the data into a bar
        for (i in currentFormularies.indices) {
            Log.d("CurrentForms for loop", currentFormularies[i].toString())
            entries.add(BarEntry(i.toFloat(), currentFormularies[i].rating.toFloat()))

        }
        val calzadaMap = currentFormularies.map { list -> mapOfCalzada[list.name] }.toTypedArray()
        val aceraMap = currentFormularies.map { list -> mapOfAcera[list.name] }.toTypedArray()
        for (i in aceraMap) {
            if (i != null) {
                labels.add(i)
            }
        }
        for (i in calzadaMap) {
            if (i != null) {
                labels.add(i)
            }
        }

        val radarDataSet1 = BarDataSet(entries, "")
        radarDataSet1.formLineWidth = 1f
        radarDataSet1.setColors(Color.BLUE)


        sets.add(radarDataSet1)

        val radarData = BarData(radarDataSet1)
        radarData.setDrawValues(false)
        radarData.setValueTextSize(1f)
        radarData.setValueTextColor(Color.BLACK)

        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        chart.data = radarData
        chart.invalidate()
    }


    fun makeExcel(context: Context) {
        _state.value = _state.value.copy(isLoading = true)
        try {
            //get location make a funciton
            val geo = Geocoder(context).getFromLocation(
                currentSurvey.value!!.startLatitude,
                currentSurvey.value!!.startLongitude,
                1
            )

            val road_name = geo.map { it.thoroughfare.toString() }
            val access_name = "Acceso"//manual entry
            //Instantiate Excel workbook:
            val workbook = HSSFWorkbook()


            //Instantiate Excel worksheet:
            val sheet = workbook.createSheet()
//        //set style
//        val cellStyle = workbook.createCellStyle()
//        cellStyle.setAlignment(HorizontalAlignment.CENTER)

            val idColumn = 0
            val accessNameColumn = 1
            val roadNameColumn = 2
            //Row index specifies the row in the worksheet (starting at 0):
            val rowTitles = 0
            val rowContent = 1
            //Cell index specifies the column within the chosen row (starting at 0):

            //Write text value to cell located at ROW_NUMBER / COLUMN_NUMBER:
            val headingsRow = sheet.createRow(rowTitles)

            val contentRow = sheet.createRow(rowContent)


            //init
            for (i in labels.indices){
                val cell = headingsRow.createCell(i + 3)
                cell.setCellValue(labels[i])
            }

            cell = headingsRow.createCell(idColumn)
            cell?.setCellValue("Fid")

            cell = headingsRow.createCell(accessNameColumn)
            cell?.setCellValue("Nombre de acceso")

            cell = headingsRow.createCell(roadNameColumn)
            cell?.setCellValue("Nombre de via")



            cell = contentRow.createCell(idColumn)
            cell?.setCellValue(CURRENTSURVEY_ID.value.toDouble())

            cell =contentRow.createCell(accessNameColumn)
            cell?.setCellValue("Acceso 1 - derecha")

            cell = contentRow.createCell(roadNameColumn)
            cell?.setCellValue(road_name[0])



            //Asing survey
            for (i in currentFormularies.indices){
                val cell = contentRow.createCell(i+ 3)
                cell.setCellValue(currentFormularies[i].rating.toDouble())
            }

            val filepath = "Relevamiento${CURRENTSURVEY_ID.value}.xlsx"
            //Write file:
            val extDir: String = context.getExternalFilesDir(DIRECTORY_DOCUMENTS)?.absolutePath ?: context.filesDir.absolutePath
            val outputStream = File(extDir, "Relevamientos-excel")
            if(!outputStream.exists()){
                outputStream.mkdir();
            }

            try {
                val file = File(outputStream,filepath)
                file.createNewFile()
                workbook.write(file)
                _state.value = _state.value.copy(isSuccess = true)
                Toast.makeText(context,"Excel guardado satisfactoriamente en $extDir",Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.message?.let { Log.d("EXecption writeing", it) }
                _state.value = _state.value.copy(isSuccess = false, errorMsg = e.localizedMessage)
            }
            //Get reference to first sheet:
            workbook.getSheetAt(0)
            workbook.close()
            _state.value = _state.value.copy(isLoading = false)
        }catch (e: Exception){
            _state.value = _state.value.copy(isSuccess = false, errorMsg = e.localizedMessage)
        }

    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMsg = null
        )
    }



}

