package com.app.relevamientoapp.view.screens.graph
import android.content.Intent
import com.app.relevamientoapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.relevamientoapp.MainActivity
import com.app.relevamientoapp.view.EventDialog
import com.app.relevamientoapp.view.viewmodels.graphFinish.GraphFinishViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.RadarChart
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun GraphFinishScreen(
    navController: NavController,
    viewModel : GraphFinishViewModel = hiltViewModel()
) {
    val context = LocalContext.current


    if (viewModel.state.value.isSuccess == true && viewModel.state.value.errorMsg == null){
        BoxWithConstraints(
            Modifier
                .background(MaterialTheme.colors.background)
                .padding(30.dp)
                .padding(top = 40.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.height(200.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.exellogo), contentDescription = "Excel logo",Modifier.size(200.dp))
                    Image(painter = painterResource(id = R.drawable.checked), contentDescription = "Checked logo",Modifier.size(100.dp))
                }

                TextButton(onClick = {
                    context.startActivity(Intent(context, MainActivity::class.java))
                },modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Volver al menu principal", color = Color.Black, textAlign = TextAlign.Center, fontSize = 25.sp)
                }

            }
        }

    }else  {
        LazyColumn(
            Modifier
                .background(MaterialTheme.colors.background)
                .padding(10.dp)
                .padding(top = 40.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (viewModel.state.value.isSuccessLoadCollections == true) {
                        Text(text = "Realice un zoom para una mejor visualizacion.")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(700.dp)
                        ) {
                            AndroidView(
                                factory = { ctx ->
                                    val chart = HorizontalBarChart(ctx)

                                    viewModel.initRadarChart(chart)
                                    viewModel.showRadarChart(chart)

                                    chart
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(700.dp),
                            )
                        }
                    } else {
                        CircularProgressIndicator()
                    }


                    Text(text = "Valoracion promedio del tramo peatonal.")
                    Spacer(modifier = Modifier.height(5.dp))
                    RatingBar(
                        value = viewModel.pedestrianAverage.value,
                        config = RatingBarConfig()
                            .style(RatingBarStyle.HighLighted)
                            .size(70.dp),
                        onValueChange = {},
                        onRatingChanged = {}
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Valoracion promedio del tramo vehicular.")
                    RatingBar(
                        value = viewModel.vehicularAverage.value,
                        config = RatingBarConfig()
                            .style(RatingBarStyle.HighLighted)
                            .size(70.dp),
                        onValueChange = {},
                        onRatingChanged = {}
                    )

                    TextButton(onClick = {
                        viewModel.makeExcel(context = context)
                    }) {
                        Text(text = "Finalizar registro", color = Color.Black)
                    }

                }
            }


        }
        if (viewModel.state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel.state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() })
        }


    }
}