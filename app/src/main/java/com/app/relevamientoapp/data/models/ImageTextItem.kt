package com.app.relevamientoapp.data.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.relevamientoapp.R
import com.app.relevamientoapp.utilities.common.Resource

data class ImageTextItem(
    val imageResource: Int,
    val imageText: String,
    val isSelected: Boolean = false
)

data class ImageTextItemSelectable(
    val imageResource: Int,
    val imageText: String,
    val nameId : String,
    val isSelected: MutableState<Boolean>
) {
    fun toggle() {
        isSelected.value = !isSelected.value
    }

    fun select() {
        isSelected.value = true
    }

    fun unSelect() {
        isSelected.value = false
    }
}

@Composable
fun ListItemsImg(model: ImageTextItemSelectable, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(vertical = 6.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = modifier
                .width(90.dp)
                .height(130.dp)
        ) {
            Checkbox(
                checked = model.isSelected.value,
                onCheckedChange = {model.toggle()},
                modifier = modifier.padding(start = 80.dp, bottom = 106.dp)
            )
            Column() {
                Image(
                    painter = painterResource(id = model.imageResource),
                    contentDescription = "walk",
                    modifier
                        .size(80.dp)
                        .clickable {
                            model.toggle()
                        }
                )
                Row(modifier.width(90.dp)) {
                    Text(
                        text = model.imageText,
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