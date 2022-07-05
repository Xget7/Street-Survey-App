package com.app.relevamientoapp.view.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.view.screens.surveyActivity.SurveyEndScreen
import com.app.relevamientoapp.view.screens.surveyIntro.SurveyPedestrianScreen
import com.app.relevamientoapp.view.screens.surveyIntro.SurveyVehicularScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNav() {

    val mainNavController = rememberAnimatedNavController()

    AnimatedNavHost(navController = mainNavController, startDestination = Destinations.SplashScreen.route){

        //-----------------SPLASH SCREEN-----------------\\
        composable(
            route = Destinations.SplashScreen.route,
        ) {
            SplashScreen(mainNavController)
        }


        composable(
            route = Destinations.MainScreen.route,
        ) {
            MainScreen(mainNavController)
        }
        composable(
            route = Destinations.SurveyPedestrianScreen.route,
        ) {
            SurveyPedestrianScreen(mainNavController)
        }

        composable(
            route = Destinations.SurveyVehicleScreen.route,
        ) {
            SurveyVehicularScreen(mainNavController)
        }


    }
}