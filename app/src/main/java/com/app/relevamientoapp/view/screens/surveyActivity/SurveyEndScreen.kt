package com.app.relevamientoapp.view.screens.surveyActivity

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.app.relevamientoapp.R.*
import com.app.relevamientoapp.maps_services.Broadcast
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.view.viewmodels.surveys.SurveyEndViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@Composable
fun SurveyEndScreen(
    navController: NavController,
    viewModel: SurveyEndViewModel = hiltViewModel()
) {
//    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)
    val locationCallback = remember {
        Broadcast(context).callBack
    }
    val currentLatLng by locationCallback.collectAsState(initial = Location(""))
    var marker: Marker? = null
    val customView = remember { LottieAnimationView(context) }

    viewModel.collectLocationBroadcast(
        context = context,
    )



    if (viewModel.state.value.isSuccess == true) {
        AndroidView(
            {
                customView
            },
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        ) { view ->
            with(view) {
                setAnimation(raw.sucess)
                playAnimation()
                foregroundGravity = Gravity.CENTER

            }
        }
        LaunchedEffect(true ){
            delay(1200)
            navController.navigate(
                Destinations.FormularySelectionScreen.passSurveyId(
                    viewModel.CURRENTSURVEY_ID.value.toString()
                )
            )
        }

    } else {
        BoxWithConstraints(
            Modifier
                .background(MaterialTheme.colors.background)
                .padding(10.dp)
                .padding(top = 40.dp)
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = drawable.pedestriancrossing),
                        contentDescription = "Person",
                        Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Relevamiento de infrastructura peatonal",
                        fontStyle = FontStyle(font.abeezee),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
                Spacer(modifier = Modifier.height(10.dp))

                if (currentLatLng.longitude == 0.0 && currentLatLng.latitude == 0.0) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "Seleccione el punto de fin de relevamiento",
                        fontStyle = FontStyle(font.abeezee),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    AndroidView(
                        { mapView }, modifier = Modifier
                            .height(350.dp)
                            .width(300.dp)
                    ) { mapView ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val map = mapView.awaitMap()
                            map.uiSettings.isZoomControlsEnabled = true


                            val options = MarkerOptions()
                                .title("Punto de finalizacion")
                                .position(LatLng(currentLatLng.latitude, currentLatLng.longitude))



                            if (marker != null) {
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLng(
                                        LatLng(
                                            currentLatLng.latitude,
                                            currentLatLng.longitude
                                        )
                                    )
                                )

                            } else {
                                marker = map.addMarker(options)
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            currentLatLng.latitude,
                                            currentLatLng.longitude
                                        ), 16f
                                    )
                                )
                                map.addPolygon(
                                    PolygonOptions()
                                        .add(
                                            LatLng(currentLatLng.latitude, currentLatLng.longitude),
                                            LatLng(
                                                viewModel.CURRENTSURVEY_LAT_LNG.value.latitude,
                                                viewModel.CURRENTSURVEY_LAT_LNG.value.longitude
                                            )
                                        )
                                        .strokeColor(Color.RED)
                                        .fillColor(Color.BLUE)
                                )
                            }


                        }

                    }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(modifier = Modifier.clickable {
                            viewModel.updateSurveyLatLng(navController)
                        }) {
                            Image(
                                painter = painterResource(id = drawable.checked),
                                contentDescription = "Record button",
                                Modifier.size(60.dp)
                            )
                            Text(
                                text = "Confirmar fin de relevamiento",
                                fontStyle = FontStyle(font.abeezee),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }

            }
        }
    }
}




