package ua.lifecell.startapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.lifecell.startapp.R
import ua.lifecell.startapp.ui.screens.Screen
import ua.lifecell.startapp.ui.screens.details.DetailsScreen
import ua.lifecell.startapp.ui.screens.main.MainHiltScreen
import ua.lifecell.startapp.ui.theme.StartAppTheme
import ua.lifecell.startapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartAppTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                var titleText by remember { mutableStateOf("") }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = titleText,
                                    color = Color.Black
                                )
                            },
                            backgroundColor = White,
                            elevation = 0.dp,
                            navigationIcon = {
                                if (backStackEntry.value?.destination?.route != Screen.MainScreen.ROUTE) {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(
                                            Icons.Filled.ArrowBack,
                                            null
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.ROUTE,
                        modifier = Modifier.padding(it)
                    ) {
                        composable(Screen.MainScreen.ROUTE) {
                            titleText = context.getString(R.string.main_toolbar_title)
                            MainHiltScreen(
                                onModelClick = { id, name ->
                                    navController.navigate(Screen.DetailsScreen.getRouteWithParams(id, name))
                                }
                            )
                        }
                        composable(
                            Screen.DetailsScreen.ROUTE_WITH_PARAMS,
                            arguments = listOf(
                                navArgument(Screen.DetailsScreen.ITEM_ID) {
                                    type = NavType.StringType
                                },
                                navArgument(Screen.DetailsScreen.ITEM_NAME) {
                                    type = NavType.StringType
                                }
                            )
                        ) { entry ->
                            titleText = entry.arguments?.getString(Screen.DetailsScreen.ITEM_NAME) ?: ""
                            DetailsScreen ()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StartAppTheme {

    }
}