import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.view.screens.MainScreen
import com.app.relevamientoapp.view.screens.SplashScreen
import com.app.relevamientoapp.view.screens.formularyScreens.CompFormSelectionScreen
import com.app.relevamientoapp.view.screens.formularyScreens.ComplementaryFormularyScreen
import com.app.relevamientoapp.view.screens.formularyScreens.FormularyScreen
import com.app.relevamientoapp.view.screens.formularyScreens.FormularySelectionScreen
import com.app.relevamientoapp.view.screens.graph.GraphFinishScreen
import com.app.relevamientoapp.view.screens.surveyActivity.RecordScreen
import com.app.relevamientoapp.view.screens.surveyActivity.SurveyEndScreen
import com.app.relevamientoapp.view.screens.surveyActivity.SurveyStartScreen
import com.app.relevamientoapp.view.screens.surveyIntro.SurveyPedestrianScreen
import com.app.relevamientoapp.view.screens.surveyIntro.SurveyVehicularScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SurveyNav() {

    val mainNavController = rememberAnimatedNavController()

    AnimatedNavHost(navController = mainNavController, startDestination = Destinations.SurveyStartScreen.route){

        //-----------------SPLASH SCREEN-----------------\\
        composable(
            route = Destinations.SurveyStartScreen.route,
        ) {
            SurveyStartScreen(mainNavController)
        }

        composable(
            route = Destinations.RecordScreen.route,
        ) {
            RecordScreen(mainNavController)
        }

        composable(
            route = Destinations.SurveyEndScreen.route,
        ) {
            SurveyEndScreen(mainNavController)
        }
        composable(
            route = Destinations.FormularySelectionScreen.route,
        ) {
            FormularySelectionScreen(mainNavController)
        }
        composable(
            route = Destinations.FormularyScreen.route,
        ) {
            FormularyScreen(navController = mainNavController)
        }

        composable(
            route = Destinations.ComplementaryFormularyScreen.route,
        ) {
            ComplementaryFormularyScreen(navController = mainNavController)
        }
        composable(
            route = Destinations.ComplementaryFormularySelectionScreen.route,
        ) {
            CompFormSelectionScreen(navController = mainNavController)
        }
        composable(
            route = Destinations.GraphFinishScreen.route,
        ) {
            GraphFinishScreen(navController = mainNavController)
        }
    }
}