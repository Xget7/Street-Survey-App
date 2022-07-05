package com.app.relevamientoapp.view.screens

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.animation.OvershootInterpolator
import android.window.SplashScreen
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.app.relevamientoapp.utilities.Screens.Destinations
import kotlinx.coroutines.delay
import java.util.Collections.max
import kotlin.math.max

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    ) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1000)
        navController.navigate(Destinations.MainScreen.route)
    }

    BoxWithConstraints(modifier.fillMaxSize()) {
        val boxWithConstraintsScope = this

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center ,
        ) {
          CircularProgressIndicator()
        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(padding),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//
//        }
    }
}