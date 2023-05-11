package com.respire.startapp.uiComposeHilt.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.respire.startapp.uiComposeHilt.theme.Typography
import com.respire.startapp.uiComposeHilt.theme.White
import com.respire.startapp.uiComposeHilt.theme.Yellow

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(onBack: () -> Boolean) {

    val viewModel = hiltViewModel<DetailsViewModel>()

    val model by viewModel.modelUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = model?.name.orEmpty(),
                        color = Color.Black
                    )
                },
                backgroundColor = White,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues = it)
        ) {
            GlideImage(
                model = model?.imageUrl,
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentWidth()
                    .align(CenterHorizontally)
                    .wrapContentHeight(),
                contentDescription = "Image",
                contentScale = ContentScale.Inside
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                text = model?.description.orEmpty(),
                style = Typography.bodyLarge
            )
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(CenterHorizontally)
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Yellow, contentColor = White),
                shape = CutCornerShape(8.dp),
                onClick = {
                    viewModel.openAppInGooglePlay(model?.marketId)
                }) {
                Image(
                    Icons.Default.PlayArrow,
                    contentDescription = "Google Play",
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Google Play", Modifier.padding(start = 10.dp))
            }
        }
    }
}