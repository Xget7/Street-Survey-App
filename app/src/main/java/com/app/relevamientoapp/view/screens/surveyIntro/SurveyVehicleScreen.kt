package com.app.relevamientoapp.view.screens.surveyIntro

import android.Manifest.permission
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.maps_services.SurveyActivity
import com.app.relevamientoapp.ui.theme.RelevamientoAppTheme
import com.app.relevamientoapp.view.screens.components.ImageText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import pub.devrel.easypermissions.EasyPermissions.hasPermissions


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SurveyVehicularScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val neededPermissions = arrayOf(
        permission.ACCESS_FINE_LOCATION,
        permission.CAMERA,
        permission.WRITE_EXTERNAL_STORAGE
    )



    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK)
            context.startActivity(Intent(context, SurveyActivity::class.java))
        else {

            Toast.makeText(
                context,
                "Nececitas aceptar los permisos para poder iniciar el relevamiento",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val permissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { maps ->
            val granted = maps.values.reduce { acc, next -> (acc && next) }
            if (granted) {
                // all permission granted
                checkLocationSetting(
                    context = context,
                    onDisabled = { intentSenderRequest ->
                        settingResultRequest.launch(intentSenderRequest)
                    },
                    onEnabled = {
                        context.startActivity(Intent(context, SurveyActivity::class.java))
                    }
                )
            } else {
                // Permission Denied: Do something
                Toast.makeText(
                    context,
                    "Nececitas aceptar los permisos para poder iniciar el relevamiento",
                    Toast.LENGTH_LONG
                ).show()
            }

        }


    BoxWithConstraints(
        modifier
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
            .padding(top = 35.dp)
    ) {
        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.relevamientovehicular),
                    contentDescription = "Intersection",
                    modifier.size(62.dp)
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = "Relevamiento de infrastructura vehicular",
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = modifier.height(5.dp))
            Text(
                text = "CRITERIOS EVALUADOS",
                fontStyle = FontStyle(R.font.abeezee),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(20.dp))

            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally)
            ) {
                ImageText(R.drawable.estadocalzada, "Estado de calzada")
                ImageText(R.drawable.demarcacion, "Demarcación")
                ImageText(R.drawable.roadsigns, "Señales verticales")
            }
            Spacer(modifier = modifier.height(20.dp))

            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(150.dp, Alignment.CenterHorizontally)
            ) {
                ImageText(R.drawable.speedlimit, "Gestion de velocidad")
                ImageText(R.drawable.parking, "Estacionamientos")
            }
            Spacer(modifier = modifier.height(20.dp))

            Text(
                text = "RELEVAMIENTO INDIRECTO DESDE ACERAS",
                fontStyle = FontStyle(R.font.abeezee),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(20.dp))

            Box(
                modifier.fillMaxSize(),
            ) {
                Column() {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .height(70.dp)
                    ) {

                        Image(
                            modifier = modifier
                                .clickable {
                                    when {
                                        hasPermissions(context, *neededPermissions) -> {
                                            // All permissions granted
                                            checkLocationSetting(
                                                context = context,
                                                onDisabled = { intentSenderRequest ->
                                                    settingResultRequest.launch(intentSenderRequest)
                                                },
                                                onEnabled = {
                                                    context.startActivity(Intent(context, SurveyActivity::class.java))

                                                }
                                            )
                                        }
                                        else -> {
                                            // Request permissions
                                            permissionsLauncher.launch(neededPermissions)
                                        }
                                    }


                                }
                                .size(80.dp),
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Play button",
                        )
                    }


                    Spacer(modifier = modifier.height(10.dp))

                    Text(
                        text = "INICIAR RELEVAMIENTO INTEGRAL ",
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

            }


        }


    }
    fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
}

fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun A() {
    val prevNav = rememberNavController()
    RelevamientoAppTheme() {
        SurveyVehicularScreen(prevNav)
    }
}