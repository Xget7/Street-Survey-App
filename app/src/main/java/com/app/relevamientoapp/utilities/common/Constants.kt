package com.app.relevamientoapp.utilities.common

import android.content.Context
import android.location.Location
import androidx.core.content.edit
import androidx.room.TypeConverter
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.models.ImageTextItem
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken




object Constants {

    const val LOCATION_SERVICE_ID = 175
    const val ACTION_START_LOCATION_SERVICE = "startLocationService"
    const val ACTION_STOP_LOCATION_SERVICE = "stopLocationService"
    const val SURVEY = "surveyId"
    //FORMULARIES IDS
    val SIDEWALKS = "sidewalks"
    val INFRAESTRUCTURE = "infrastructure"
    val ACCESSIBILITY = "accessibility"
    val OBSTACLES = "obstacles"
    val CIRCULATION_COMFORT = "comfort"

    //FORMULARIES IDS COMPLEMENTARY
    val SIDEWALKS_CONDITION = "sidewalks_condition"
    val DEMARCATION = "demarcation"
    val VERTICAL_SIGNALS = "vertical_signals"
    val VELOCITY_SIGN = "velocity"
    val PARKINGS = "parkings"
    internal object SharedPreferenceUtil {

        fun Location?.toText(): String {
            return if (this != null) {
                "($latitude, $longitude)"
            } else {
                "Unknown location"
            }
        }


        const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"

        /**
         * Returns true if requesting location updates, otherwise returns false.
         *
         * @param context The [Context].
         */
        fun getLocationTrackingPref(context: Context): Boolean =
            context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getBoolean(KEY_FOREGROUND_ENABLED, false)

        /**
         * Stores the location updates state in SharedPreferences.
         * @param requestingLocationUpdates The location updates state.
         */
        fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
            context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE).edit {
                putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
            }
    }

    val ImageAndTextitems = listOf(
        ImageTextItem(R.drawable.store, "Comercio"),
        ImageTextItem(R.drawable.signage, "Vehiculo"),
        ImageTextItem(R.drawable.luzdelacalle, "Poste"),
        ImageTextItem(R.drawable.arbol, "Arbol o vegetacion"),
        ImageTextItem(R.drawable.ejerciciopuntual, "Escalas"),
        ImageTextItem(R.drawable.obstaculos, "Obstaculo"),
        ImageTextItem(R.drawable.mistake, "Desnivel"),
        ImageTextItem(R.drawable.rampa, "Rampas"),
        ImageTextItem(R.drawable.road, "Ancho Insuficiente"),
        ImageTextItem(R.drawable.pedestriannotcross, "Fin de acera"),
        ImageTextItem(R.drawable.pedestriancrossing, "Reinicio de acera"),
        ImageTextItem(R.drawable.roadbarrier, "Acera en mal estado"),
    )
class Converters {
        @TypeConverter
        fun restoreList(listOfString: String?): List<String?>? {
            return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
        }

        @TypeConverter
        fun saveList(listOfString: List<String?>?): String? {
            return Gson().toJson(listOfString)
        }
}



}