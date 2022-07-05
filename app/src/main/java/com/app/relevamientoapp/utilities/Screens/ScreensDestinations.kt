package com.app.relevamientoapp.utilities.Screens

import com.app.relevamientoapp.utilities.common.Constants.SURVEY

sealed class Destinations(val route : String){
    object SplashScreen : Destinations("splash_screen")
    object MainScreen : Destinations("main_screen")
    object SurveyVehicleScreen : Destinations("survey_vehicle_screen")
    object SurveyPedestrianScreen : Destinations("survey_pedestrian_screen")



    object SurveyStartScreen : Destinations("survey_start_screen")

    object RecordScreen : Destinations("record_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "record_screen/$surveyId"
        }
    }
    object SurveyEndScreen : Destinations("survey_end_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "survey_end_screen/$surveyId"
        }
    }
    object FormularySelectionScreen : Destinations("formulary_selection_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "formulary_selection_screen/$surveyId"
        }
    }
    object FormularyScreen : Destinations("formulary_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "formulary_screen/$surveyId"
        }
    }
    object ComplementaryFormularySelectionScreen : Destinations("complementary_formulary_selection_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "complementary_formulary_selection_screen/$surveyId"
        }
    }
    object ComplementaryFormularyScreen : Destinations("complementary_formulary_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "complementary_formulary_screen/$surveyId"
        }
    }
    object GraphFinishScreen : Destinations("graph_finish_screen/{$SURVEY}"){
        fun passSurveyId(surveyId : String) : String{
            return "graph_finish_screen/$surveyId"
        }
    }
}