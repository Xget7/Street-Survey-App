package com.app.relevamientoapp.view.screens

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.utilities.Screens.Destinations

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current


// If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {

        }
        else -> {
            BoxWithConstraints(
                modifier.padding(top = 30.dp)
            ) {

                Column(
                    modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Menu",
                            fontStyle = FontStyle(R.font.abeezee),
                            fontSize = 45.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold

                        )
                    }
                    Spacer(modifier = modifier.height(20.dp))
                    Card(
                        modifier.size(250.dp, height = 200.dp),
                        shape = RoundedCornerShape(20),
                        elevation = 3.dp,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pnnvase),
                            contentDescription = "Peatonal Infr Image",
                            modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Row(modifier.width(350.dp)) {
                        TextButton(onClick = {
                            navController.navigate(Destinations.SurveyPedestrianScreen.route)
                        }) {
                            Text(
                                text = "Relevamiento de infraestructura peatonal y vehicular",
                                fontStyle = FontStyle(R.font.abeezee),
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.onBackground

                            )
                        }
                    }
                }

            }
        }
    }


}