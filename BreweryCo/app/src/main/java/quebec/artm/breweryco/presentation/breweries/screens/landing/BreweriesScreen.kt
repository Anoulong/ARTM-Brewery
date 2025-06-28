@file:OptIn(ExperimentalFoundationApi::class)

package quebec.artm.breweryco.presentation.breweries.screens.landing

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import quebec.artm.breweryco.presentation.breweries.screens.landing.models.BreweryUiData

@Composable
fun BreweriesScreen(vm: BreweriesScreenViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val infiniteScrollInProgressState =
        vm.infiniteScrollInProgressStateFlow.collectAsStateWithLifecycle(initialValue = false)
    val isLoading = vm.loadingStateFlow.collectAsStateWithLifecycle(initialValue = false)
    Log.d("BreweriesScreen", "BreweriesScreen: breweries total size = ${state.breweries.size}")
    BreweriesRender(
        modifier = Modifier.fillMaxWidth(),
        breweries = state.breweries,
        isLoading = isLoading.value,
        infiniteScrollInProgress = infiniteScrollInProgressState.value,
        onLoadMoreTriggered = {
            vm.onLoadMoreTriggered()
        }
    )
}

@Composable
private fun BreweriesRender(
    modifier: Modifier = Modifier,
    breweries: List<BreweryUiData>,
    isLoading: Boolean = false,
    infiniteScrollInProgress: Boolean = false,
    onLoadMoreTriggered: () -> Unit,
) {

    LazyColumn(modifier) {
        itemsIndexed(breweries, key = { _, item -> item.key }) { index, brewery ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    text = brewery.name
//                    text = "[$index]${brewery.name}"//all 100 breweries are displayed
                )
            }

            if (index < breweries.lastIndex) {
                HorizontalDivider()
            }

            //Load more data if the last item of the list is reached
            if (brewery.key == breweries.last().key && !infiniteScrollInProgress) {
                onLoadMoreTriggered()
            }
        }

        if (infiniteScrollInProgress) {
            item {
                Box(
                    modifier =
                    Modifier
                        .height(16.dp)
                        .fillMaxWidth(),
                ) {
                    CircularProgressIndicator(
                        modifier =
                        Modifier
                            .size(16.dp)
                            .align(
                                Alignment.Center,
                            ),
                    )
                }
            }
        }

    }
}