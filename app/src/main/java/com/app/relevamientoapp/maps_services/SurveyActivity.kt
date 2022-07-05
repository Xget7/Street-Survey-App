package com.app.relevamientoapp.maps_services

import SurveyNav
import android.Manifest.*
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.relevamientoapp.R
import com.app.relevamientoapp.databinding.ActivitySurveyBinding
import com.app.relevamientoapp.ui.theme.RelevamientoAppTheme
import com.app.relevamientoapp.utilities.common.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val TAG = "SurveyActivity"

@AndroidEntryPoint
class SurveyActivity : AppCompatActivity(){

    private var foregroundOnlyLocationServiceBound: Boolean = false
    private var foregroundLocationService : ForegroundLocationService? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySurveyBinding
    private lateinit var marker: Marker

    private val REQUEST_CODE_LOCATION_PERMISSION = 1


    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            foregroundLocationService = binder.service
            foregroundOnlyLocationServiceBound = true

        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundLocationService = null
            foregroundOnlyLocationServiceBound = false
            Log.d(TAG,"FOREGROUND FALSE")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        binding.composeScreen.setContent {
            RelevamientoAppTheme {
                SurveyNav()
            }
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext, permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }else {
            startLocationService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.size > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::mMap.isInitialized) return
    }


    override fun onDestroy() {
        super.onDestroy()
        stopLocationService()
    }

    private fun isLocationServiceRunning(): Boolean{
        val actManager: ActivityManager? = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        if (actManager != null){
            for (service in actManager.getRunningServices(Integer.MAX_VALUE)){
                if (ForegroundLocationService::class.java.name.equals(service.service.className)){
                    if (service.foreground){
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startLocationService(){
        if (!isLocationServiceRunning()){
            val intent = Intent(applicationContext, ForegroundLocationService::class.java)
            intent.action = Constants.ACTION_START_LOCATION_SERVICE
            startService(intent)
            Toast.makeText(this,"Proceso de localizacion iniciado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopLocationService(){
        if (isLocationServiceRunning()){
            val intent = Intent(applicationContext, ForegroundLocationService::class.java)
            intent.action = Constants.ACTION_STOP_LOCATION_SERVICE
            stopService(intent)

        }
    }



    ////////////////////////////////////////////////////////////////////////////////////




}