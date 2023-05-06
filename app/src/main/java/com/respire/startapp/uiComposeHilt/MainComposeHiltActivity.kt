package com.respire.startapp.uiComposeHilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.respire.startapp.uiCompose.theme.StartAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainComposeHiltActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartAppTheme {
                MainHiltScreen(viewModel = viewModel())
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StartAppTheme {
        Greeting("Android")
    }
}