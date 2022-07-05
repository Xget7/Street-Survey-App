package com.app.relevamientoapp.view.screens.surveyActivity

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.R.*
import com.app.relevamientoapp.maps_services.Broadcast
import com.app.relevamientoapp.view.viewmodels.surveys.SurveyStartViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@Composable
fun SurveyStartScreen(
    navController: NavController,
    viewModel: SurveyStartViewModel = hiltViewModel()
) {
//    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)
    val locationCallback = remember {
        Broadcast(context).callBack
    }
    val currentLocation by locationCallback.collectAsState(initial = Location(""))
    var marker: Marker? = null


    viewModel.collectLocationBroadcast(
        context = context,
    )

    LaunchedEffect(true) {

    }

//TODO("MANAGE ERROR STATE AND LOADING")
    LazyColumn(
        Modifier
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
            .padding(top = 40.dp)
    ) {
        item {
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
                Text(
                    text = "Seleccione el punto de inicio de relevamiento",
                    fontStyle = FontStyle(font.abeezee),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                if (currentLocation.longitude == 0.0 && currentLocation.latitude == 0.0) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }

                } else {
                    AndroidView(
                        { mapView }, modifier = Modifier
                            .height(350.dp)
                            .width(300.dp)
                    )
                    { mapView ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val map = mapView.awaitMap()
                            map.uiSettings.isZoomControlsEnabled = true


                            val options = MarkerOptions()
                                .title("Punto de inicio")
                                .position(
                                    LatLng(
                                        currentLocation.latitude,
                                        currentLocation.longitude
                                    )
                                )


                            if (marker != null) {
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            currentLocation.latitude,
                                            currentLocation.longitude
                                        ), 16f
                                    )
                                )
                            } else {
                                marker = map.addMarker(options)
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            currentLocation.latitude,
                                            currentLocation.longitude
                                        ), 16f
                                    )
                                )
                            }

                        }

                    }
                }
            }



            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.clickable {
                    viewModel.insertSurvey(navController)
                }) {
                    Image(
                        painter = painterResource(id = drawable.recbutton),
                        contentDescription = "Record button",
                        Modifier.size(60.dp)
                    )
                    Text(
                        text = "Iniciar grabacion y relevamiento",
                        fontStyle = FontStyle(font.abeezee),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

            }
            if (viewModel.state.value.errorMsg != null) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.onDismiss()
                    },
                    title = {
                        Text(text = "Error!")
                    },
                    text = {
                        Text(viewModel.state.value.errorMsg!!)
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.onDismiss()
                        }) {
                            Text(text = "Ok")
                        }
                    }
                )
            }
        }


    }

}

@Composable
fun rememberMapViewWithLifecycle(
    context: Context,

    ): MapView {
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }