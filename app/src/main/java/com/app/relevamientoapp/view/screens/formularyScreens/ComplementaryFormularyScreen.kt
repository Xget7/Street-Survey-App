package com.app.relevamientoapp.view.screens.formularyScreens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.relevamientoapp.R
import com.app.relevamientoapp.data.entity.FormularyEntity
import com.app.relevamientoapp.utilities.Screens.Destinations
import com.app.relevamientoapp.view.screens.components.FormularyScreenComponent
import com.app.relevamientoapp.view.viewmodels.formularies.CompFormViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@SuppressLint("LogNotTimber")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ComplementaryFormularyScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CompFormViewModel = hiltViewModel()
) {

    val openDialog = remember { viewModel.openDialog }
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

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
                    contentDescription = "Person",
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
            if (viewModel.userFormularyList.isEmpty()){
                CircularProgressIndicator()
            }else{
                val pagerState = rememberPagerState(pageCount = viewModel.userFormularyList.size)

                //make with index vars automatic and not parsed manually
                HorizontalPager(state = pagerState) { index ->

                    when (index) {
                        0 -> {
                            FormularyScreenComponent(
                                formulary = viewModel.userFormularyList[0],
                                onNext = {
                                    scope.launch {
                                        viewModel.selectedFormulariesLIst.add(
                                            FormularyEntity(
                                                surveyParentId = viewModel.CURRENTSURVEY_ID.value,
                                                name = viewModel.userFormularyList[0].formularyName,
                                                rating = viewModel.userFormularyList[0].rating.toInt(),
                                                comment = ""
                                            )
                                        )
                                        viewModel.returnIndexAddition(viewModel.userFormularyList[0])
                                            .let {
                                                if (it >= pagerState.pageCount){
                                                    openDialog.value = true
                                                }else{
                                                    pagerState.animateScrollToPage(it)

                                                }

                                            }
                                        //navigate finish screen
                                    }
                                },
                                onBack = {
                                    scope.launch {
                                        if (viewModel.selectedFormulariesLIst.isNotEmpty()) {
                                            viewModel.selectedFormulariesLIst.removeLast()
                                        }
                                        viewModel.returnIndexSubstraction(viewModel.userFormularyList[0])
                                            .let {
                                                pagerState.animateScrollToPage(it)
                                            }
                                    }
                                },
                            )
                        }
                        1 -> {
                            FormularyScreenComponent(
                                formulary = viewModel.userFormularyList[1],
                                onNext = {
                                    scope.launch {
                                        viewModel.selectedFormulariesLIst.add(
                                            FormularyEntity(
                                                surveyParentId = viewModel.CURRENTSURVEY_ID.value,
                                                name = viewModel.userFormularyList[1].formularyName,
                                                rating = viewModel.userFormularyList[1].rating.toInt(),
                                                comment = ""
                                            )
                                        )
                                        viewModel.returnIndexAddition(viewModel.userFormularyList[1])
                                            .let {
                                                if (it >= pagerState.pageCount){
                                                    openDialog.value = true
                                                }else{
                                                    pagerState.animateScrollToPage(it)

                                                }

                                            }
                                        //navigate finish screen
                                    }
                                },
                                onBack = {
                                    scope.launch {
                                        if (viewModel.selectedFormulariesLIst.isNotEmpty()) {
                                            viewModel.selectedFormulariesLIst.removeLast()
                                        }
                                        viewModel.returnIndexSubstraction(viewModel.userFormularyList[1])
                                            .let {
                                                pagerState.animateScrollToPage(it)
                                            }
                                    }
                                },
                            )
                        }
                        2 -> {
                            FormularyScreenComponent(
                                formulary = viewModel.userFormularyList[2],
                                onNext = {
                                    scope.launch {
                                        viewModel.selectedFormulariesLIst.add(
                                            FormularyEntity(
                                                surveyParentId = viewModel.CURRENTSURVEY_ID.value,
                                                name = viewModel.userFormularyList[2].formularyName,
                                                rating = viewModel.userFormularyList[2].rating.toInt(),
                                                comment = ""
                                            )
                                        )
                                        viewModel.returnIndexAddition(viewModel.userFormularyList[2])
                                            .let {
                                                if (it >= pagerState.pageCount){
                                                    openDialog.value = true
                                                }else{
                                                    pagerState.animateScrollToPage(it)

                                                }

                                            }
                                        //navigate finish screen
                                    }
                                },
                                onBack = {
                                    scope.launch {
                                        if (viewModel.selectedFormulariesLIst.isNotEmpty()) {
                                            viewModel.selectedFormulariesLIst.removeLast()
                                        }
                                        viewModel.returnIndexSubstraction(viewModel.userFormularyList[2])
                                            .let {
                                                pagerState.animateScrollToPage(it)
                                            }
                                    }
                                },
                            )
                        }
                        3 -> {
                            FormularyScreenComponent(
                                formulary = viewModel.userFormularyList[3],
                                onNext = {
                                    scope.launch {
                                        viewModel.selectedFormulariesLIst.add(
                                            FormularyEntity(
                                                surveyParentId = viewModel.CURRENTSURVEY_ID.value,
                                                name = viewModel.userFormularyList[3].formularyName,
                                                rating = viewModel.userFormularyList[3].rating.toInt(),
                                                comment = ""
                                            )
                                        )
                                        viewModel.returnIndexAddition(viewModel.userFormularyList[3])
                                            .let {
                                                if (it >= pagerState.pageCount){
                                                    openDialog.value = true
                                                }else{
                                                    pagerState.animateScrollToPage(it)

                                                }

                                            }
                                        //navigate finish screen
                                    }
                                },
                                onBack = {
                                    scope.launch {
                                        if (viewModel.selectedFormulariesLIst.isNotEmpty()) {
                                            viewModel.selectedFormulariesLIst.removeLast()
                                        }
                                        viewModel.returnIndexSubstraction(viewModel.userFormularyList[3])
                                            .let {
                                                pagerState.animateScrollToPage(it)
                                            }
                                    }
                                },
                            )
                        }
                        4 -> {
                            FormularyScreenComponent(
                                formulary = viewModel.userFormularyList[4],
                                onNext = {
                                    scope.launch {
                                        viewModel.selectedFormulariesLIst.add(
                                            FormularyEntity(
                                                surveyParentId = viewModel.CURRENTSURVEY_ID.value,
                                                name = viewModel.userFormularyList[4].formularyName,
                                                rating = viewModel.userFormularyList[4].rating.toInt(),
                                                comment = ""
                                            )
                                        )
                                        viewModel.returnIndexAddition(viewModel.userFormularyList[4])
                                            .let {
                                                if (it >= pagerState.pageCount){
                                                    openDialog.value = true
                                                }else{
                                                    pagerState.animateScrollToPage(it)

                                                }

                                            }
                                        //navigate finish screen
                                    }
                                },
                                onBack = {
                                    scope.launch {
                                        if (viewModel.selectedFormulariesLIst.isNotEmpty()) {
                                            viewModel.selectedFormulariesLIst.removeLast()
                                        }
                                        viewModel.returnIndexSubstraction(viewModel.userFormularyList[4])
                                            .let {
                                                pagerState.animateScrollToPage(it)
                                            }
                                    }
                                },
                            )
                        }
                    }

                }
            }

            if (openDialog.value) {
                // below line is use to
                // display a alert dialog.
                AlertDialog(
                    //TODO(""CHANGE COLORS")
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    // below line is use to display title of our dialog
                    // box and we are setting text color to white.
                    title = {
                        Text(
                            text = "Finalizacion de formularios",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    },
                    // below line is use to display
                    // description to our alert dialog.
                    text = {
                        Text("Esta seguro que desea continuar?", fontSize = 16.sp)

                    },
                    // in below line we are displaying
                    // our confirm button.
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                for (form in viewModel.selectedFormulariesLIst){
                                    viewModel.insertCurrentFormulary(
                                        form
                                    )
                                }
                                Toast.makeText(
                                    localContext,
                                    "Informe guardado con exito.",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(
                                    Destinations.GraphFinishScreen.passSurveyId(
                                        viewModel.CURRENTSURVEY_ID.value.toString()
                                    )
                                )
                            }) {
                            Text("Si", style = TextStyle(color = Color.White))
                        }
                    },
                    // in below line we are displaying
                    // our dismiss button.
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("No", style = TextStyle(color = Color.White))
                        }
                    },
                    // below line is use to add background color to our alert dialog
                    backgroundColor = Color.Blue,
                    // below line is use to add content color for our alert dialog.
                    contentColor = Color.White
                )
            }
        }
    }

}
