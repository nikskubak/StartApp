package ua.lifecell.startapp.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import ua.lifecell.startapp.ui.theme.Yellow

@Composable
fun DetailsScreen() {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val model by viewModel.modelUiState.collectAsState()

    DetailsUI(model) {
        viewModel.openAppInGooglePlay(model?.marketId)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsUI(model: Model?, onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        GlideImage(
            model = model?.imageUrl,
            modifier = Modifier
                .padding(16.dp)
                .width(100.dp)
                .height(100.dp)
                .align(CenterHorizontally),
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
                onButtonClick()
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

@Preview
@Composable
fun DetailsPreview() {
    DetailsUI(Model("0", "Model1", "Description1","")){}
}