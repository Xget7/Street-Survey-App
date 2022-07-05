package com.app.relevamientoapp.view.screens.surveyIntro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.ui.theme.RelevamientoAppTheme
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.view.screens.components.ImageText

@Composable
fun SurveyPedestrianScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {


    BoxWithConstraints(
        modifier
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
            .padding(top = 40.dp)
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
                    painter = painterResource(id = R.drawable.pedestriancrossing),
                    contentDescription = "Person",
                    modifier.size(60.dp)
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = "Relevamiento de infrastructura peatonal",
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = modifier.height(10.dp))
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
                //items
                ImageText(R.drawable.walk,"Continuidad de aceras")
                ImageText(R.drawable.signage,"Obstaculos en aceras")
                ImageText(R.drawable.comodidadcirculacion,"Comodidad de circulacion")
            }
            Spacer(modifier = modifier.height(20.dp))

            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(150.dp, Alignment.CenterHorizontally)
            ) {
                //items
                ImageText(R.drawable.infrastructura,"Estado de infraestructura")
                ImageText(R.drawable.discapacitado,"Accesibilidad universal")
            }
            Spacer(modifier = modifier.height(20.dp))

            Text(
                text = "RELEVAMIENTO DIRECTO DESDE ACERAS",
                fontStyle = FontStyle(R.font.abeezee),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )



        }

        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier.clickable {
                    navController.navigate(Destinations.MainScreen.route)
                }, verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = {
                        navController.navigate(Destinations.MainScreen.route)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowLeft , contentDescription = "Left arrow" , modifier.size(50.dp))
                    }
                    Spacer(modifier = modifier.width(5.dp))
                    Text(
                        text = "Atras",
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = modifier.width(110.dp))

                Row(modifier.clickable{
                    navController.navigate(Destinations.SurveyVehicleScreen.route)
                }, verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "Siguiente criterio",
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        navController.navigate(Destinations.SurveyVehicleScreen.route)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowRight , contentDescription = "Right arrow" , modifier.size(50.dp))

                    }
                }




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
        SurveyPedestrianScreen(prevNav)
    }
}