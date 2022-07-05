package com.app.relevamientoapp.view.screens.formularyScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.models.ListItemsImg
import com.app.relevamientoapp.view.viewmodels.formularies.CompFormSelectViewModel

@Composable
fun CompFormSelectionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CompFormSelectViewModel = hiltViewModel()
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
                    painter = painterResource(id = R.drawable.relevamientovehicular),
                    contentDescription = "Vehicle Logo",
                    modifier.size(60.dp)
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = "Relevamiento de infrastructura vehicular",
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = modifier.height(10.dp))
            Text(
                text = "Formulario de valoracion",
                fontStyle = FontStyle(R.font.abeezee),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(20.dp))


            Column(
                modifier
                    .fillMaxWidth()
                    .height(150.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(200.dp),
                ) {
                    items(viewModel.list) {
                        ListItemsImg(it)
                    }

                }

            }

            if (viewModel.anyItemSelected(viewModel.list)) {
                Column(modifier.clickable {
                    val selectedForms = viewModel.list.filter { it.isSelected.value }
                    val filteredFormsIds = selectedForms.map { it.nameId }.toList()
                    Log.d("FilteredFOrmsIds", filteredFormsIds.toString())
                    viewModel.insertSelectedFormularies(
                        filteredFormsIds,navController
                    )
                }) {
                    Image(
                        modifier = modifier
                            .size(200.dp)
                            .padding(top = 50.dp),
                        painter = painterResource(id = R.drawable.letter),
                        contentDescription = "Init  button"
                    )
                    Text(
                        text = "Iniciar valoracion del tramo",
                        fontStyle = FontStyle(R.font.abeezee),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "Selecione los criterios a valorar",
                    fontStyle = FontStyle(R.font.abeezee),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.tap),
                    contentDescription = "Touch button"
                )

            }


        }

        //ERROR DIALOG TODO("")
    }

}