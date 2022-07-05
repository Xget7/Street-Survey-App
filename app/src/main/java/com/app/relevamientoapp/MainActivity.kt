package com.app.relevamientoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.app.relevamientoapp.data.entity.SurveyEntity
import com.app.relevamientoapp.ui.theme.RelevamientoAppTheme
import com.app.relevamientoapp.view.screens.MainNav
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            setContent {


                RelevamientoAppTheme {
                    // A surface container using the 'background' color from the theme
                    MainNav()
                }
            }
        }
    }



}

