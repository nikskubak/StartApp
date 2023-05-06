package com.respire.startapp.uiComposeHilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        MainHiltScreen(navController)
                    }
                    composable("list_item_details") {
                        val parentEntry = remember(it) {
                            navController.getBackStackEntry("list")
                        }
                        //use already created instance of MainComposeHiltViewModel
                        val parentViewModel: MainComposeHiltViewModel = hiltViewModel(parentEntry)
                        DetailsScreen(parentViewModel) { navController.popBackStack() }
                    }
//                    composable("profile") {
//                        ProfileScreen { navController.popBackStack("home", false) }
//                    }
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