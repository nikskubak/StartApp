package com.respire.startapp.uiCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.respire.startapp.App
import com.respire.startapp.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen() {
    val viewModel: MainComposeViewModel = daggerViewModel { handle ->
        App.component.getMainComposeViewModelFactory().create(handle)
    }
    viewModel.getModels()
    val models by viewModel.modelsUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.main_toolbar_title)) },
            )
        }
    ) {

        LazyColumn(modifier = Modifier.padding(it)) {
            models.getOrNull()?.let { list ->
                items(list.size) { index ->
                    val entity = list[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(4.dp)
                            .clickable {
                                viewModel.openAppInGooglePlay(entity.marketId)
                            }
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        )
                    ) {
                        Row(modifier = Modifier.wrapContentHeight()) {
                            GlideImage(
                                model = entity.imageUrl,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .width(80.dp)
                                    .align(CenterVertically)
                                    .height(80.dp),
                                contentDescription = "Image",
                                contentScale = ContentScale.Inside
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .align(CenterVertically)
                                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                            ) {
                                Text(
                                    text = entity.name.orEmpty(),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                )
                                Text(text = entity.description.orEmpty())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}