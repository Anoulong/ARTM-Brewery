package quebec.artm.breweryco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import quebec.artm.breweryco.presentation.breweries.screens.landing.BreweriesScreen
import quebec.artm.breweryco.presentation.breweries.screens.landing.BreweriesScreenViewModel
import quebec.artm.breweryco.presentation.breweries.screens.landing.BreweryDetailsScreen
import quebec.artm.breweryco.presentation.navigation.Destinations
import quebec.artm.breweryco.ui.theme.BreweryCoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val breweriesScreenViewModel by viewModels<BreweriesScreenViewModel>()

    private var navController: NavHostController = NavHostController(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            BreweryCoTheme {
                navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = Destinations.Home, modifier = Modifier.padding(innerPadding)) {
                        composable<Destinations.Home> { BreweriesScreen(breweriesScreenViewModel, onBrewerySelected = {
                            navController.navigate(Destinations.Details)
                        }) }
                        composable<Destinations.Details> { BreweryDetailsScreen(breweriesScreenViewModel) }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BreweryCoTheme {
        Greeting("Android")
    }
}