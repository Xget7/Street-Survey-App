package com.app.relevamientoapp.view.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.relevamientoapp.R

@Composable
fun ImageText(
    imageResource: Int,
    imageText: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box() {
        Column() {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "walk",
                modifier
                    .size(80.dp)
                    .clickable {
                        if (onClick != null) {
                            onClick()
                        }
                    }
            )

            Row(modifier.width(90.dp)) {
                Text(
                    text = imageText,
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}


@Composable
fun ImageTextSelectable(
    imageResource: Int,
    imageText: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false
) {

    val checked = remember {
        mutableStateOf(isSelected)
    }

    Box(modifier = modifier.clickable {
        checked.value = !checked.value
        if (onClick != null) {
            onClick()
        }
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Box(
                modifier = modifier
                    .width(90.dp)
                    .height(130.dp)
            ) {
                Checkbox(checked = checked.value, onCheckedChange = {},modifier = modifier.padding(start = 80.dp, bottom = 106.dp))
                Column() {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "walk",
                        modifier
                            .size(80.dp)
                            .clickable {
                                checked.value = !checked.value
                                if (onClick != null) {
                                    onClick()
                                }
                            }
                    )
                    Row(modifier.width(90.dp)) {
                        Text(
                            text = imageText,
                            fontStyle = FontStyle(R.font.abeezee),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }



        }

    }
}

@Preview
@Composable
fun COmponentPrev() {
    ImageTextSelectable(
        imageResource = R.drawable.pedestriannotcross,
        imageText = "Cruce de personas",

        )
}

