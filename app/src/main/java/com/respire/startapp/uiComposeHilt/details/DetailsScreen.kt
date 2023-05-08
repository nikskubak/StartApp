package com.respire.startapp.uiComposeHilt.details

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.respire.startapp.domain.models.Model
import com.respire.startapp.uiComposeHilt.MainComposeHiltViewModel
import com.respire.startapp.uiComposeHilt.theme.Typography
import com.respire.startapp.uiComposeHilt.theme.White

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(viewModel: MainComposeHiltViewModel, itemId: String?, onBack: () -> Boolean) {

    Log.e("itemId", "$itemId")
    val item : Model? = viewModel.modelsUiState.collectAsState().value.getOrNull()?.find { it.id == itemId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = item?.name.orEmpty(),
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
                    .wrapContentHeight()
                    .padding(paddingValues = it)
            ) {
                GlideImage(
                    model = item?.imageUrl,
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth()
                        .align(CenterHorizontally)
                        .wrapContentHeight(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Inside
                )
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                    text = item?.description.orEmpty(),
                    style = Typography.bodyLarge
                )
        }
    }
}