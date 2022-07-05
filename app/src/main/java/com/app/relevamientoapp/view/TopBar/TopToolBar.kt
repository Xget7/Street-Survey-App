package com.app.relevamientoapp.view.TopBar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.relevamientoapp.ui.theme.RelevamientoAppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopToolBar(
    navController: NavController,
    drawerState: BottomDrawerState = BottomDrawerState(BottomDrawerValue.Closed)
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.onSecondary,
    ){
        Row(Modifier.fillMaxWidth()) {
            IconButton(onClick = { /*TODO("Open")*/ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Open Drawable")
            }

            IconButton(onClick = { /*TODO("Navigate to HOome")*/ }) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewTop() {
    val prevNav = rememberNavController()
    RelevamientoAppTheme() {
        TopToolBar(prevNav)
    }
}