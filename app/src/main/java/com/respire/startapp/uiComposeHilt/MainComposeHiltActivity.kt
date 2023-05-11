package com.respire.startapp.uiComposeHilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.respire.startapp.uiComposeHilt.details.DetailsScreen
import com.respire.startapp.uiComposeHilt.theme.StartAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainComposeHiltActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.MainScreen.ROUTE) {
                    composable(Screen.MainScreen.ROUTE) {
                        MainHiltScreen(navController)
                    }
                    composable(Screen.DetailsScreen.ROUTE_WITH_PARAMS,
                        arguments = listOf(navArgument(Screen.DetailsScreen.ITEM_ID) {
                            type = NavType.StringType
                        })
                    ) {
                        DetailsScreen {
                            navController.popBackStack()
                        }
                    }
                }
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