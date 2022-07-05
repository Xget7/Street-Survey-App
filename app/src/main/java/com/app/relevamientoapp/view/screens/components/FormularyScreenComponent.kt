package com.app.relevamientoapp.view.screens.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.models.FormularyScreenModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun FormularyScreenComponent(
    formulary: FormularyScreenModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var rating by rememberSaveable {
        mutableStateOf(0F)
    }



    Box(modifier = modifier
        .width(370.dp)
        .height(700.dp)
        .padding(4.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                ) {

                    ImageText(
                        imageResource = formulary.descriptionPhoto,
                        imageText = formulary.descriptionPhotoText
                    )
                    Text(
                        text = formulary.title,
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = "Valoración",
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(110.dp))
                Text(
                    text = "Criterio de valoración",
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            LazyColumn(
                modifier.height(300.dp)
            ) {
                itemsIndexed(items = formulary.evaluation_criteria) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp)
                    ) {
                        Text(
                            text = "$index",
                            fontStyle = FontStyle(R.font.abeezee),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center

                        )
                        Spacer(modifier = Modifier.width(120.dp))

                        Text(
                            modifier = modifier.width(190.dp),
                            text = item,
                            fontStyle = FontStyle(R.font.abeezee),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            RatingBar(
                value = rating,
                config = RatingBarConfig()
                    .style(RatingBarStyle.HighLighted)
                    .size(70.dp),
                onValueChange = {
                    rating = it
                },
                onRatingChanged = {
                     rating = it
                    Log.d("Value:" ,"current rating changed ${it}" )
                }
            )

            //the worst function in the world
            when (rating) {
                0F -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = formulary.evaluation_criteria[0],
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                1F -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = formulary.evaluation_criteria[1],
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                2F -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = formulary.evaluation_criteria[2],
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                3F -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = formulary.evaluation_criteria[3],
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                4F -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = formulary.evaluation_criteria[4],
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                5F -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = formulary.evaluation_criteria[5],
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

            }

            Column(modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                Row(horizontalArrangement = Arrangement.spacedBy(120.dp)) {
                    Box(
                        modifier
                            .clickable {
                                onBack()
                            }
                            .height(40.dp)) {
                        Row() {
                            Image(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back"
                            )
                            Spacer(modifier = modifier.width(10.dp))
                            Text(
                                text = "Atras",
                                fontStyle = FontStyle(R.font.abeezee),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                    }


                    Box(
                        modifier
                            .clickable {
                                onNext()
                                formulary.rating = rating
                            }
                            .height(40.dp)) {
                        Row() {
                            Text(
                                text = "Siguiente Criterio",
                                fontStyle = FontStyle(R.font.abeezee),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,

                                )
                            Spacer(modifier = modifier.width(10.dp))

                            Image(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "back"
                            )

                        }

                    }

                }
            }

        }

    }


}
