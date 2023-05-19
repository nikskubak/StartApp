package ua.lifecell.startapp.ui.screens.main

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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.lifecell.startapp.domain.models.Model
import ua.lifecell.startapp.ui.theme.Typography
import ua.lifecell.startapp.ui.theme.White

const val ROUTE = "MainHiltScreen"

@Composable
fun MainHiltScreen(
    onModelClick: (String, String) -> Unit
) {
    val viewModel = hiltViewModel<MainComposeHiltViewModel>()
    val models by viewModel.modelsUiState.collectAsState()

    MainHiltUI(models, onModelClick)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainHiltUI(models: List<Model>, onModelClick: (String, String) -> Unit) {
    LazyColumn {
        items(models.size) { index ->
            val entity = models[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(4.dp)
                    .clickable {
                        onModelClick(entity.id, entity.name.orEmpty())
                    },
                colors = CardDefaults.cardColors(
                    containerColor = White,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
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
                                .padding(bottom = 8.dp),
                            style = Typography.titleLarge
                        )
                        Text(
                            text = entity.description.orEmpty(),
                            style = Typography.bodyLarge,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainHiltUI(
        listOf(
            Model("0", "Model1", "Description1"),
            Model("1", "Model2", "Description2"),
        )
    ) {_,_ ->}
}