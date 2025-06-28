@file:OptIn(ExperimentalFoundationApi::class)

package quebec.artm.breweryco.presentation.breweries.screens.landing

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import quebec.artm.breweryco.extension.placeholder
import quebec.artm.breweryco.presentation.breweries.screens.landing.models.BreweryUiData
import quebec.artm.breweryco.presentation.common.dialog.ErrorDialog

@Composable
fun BreweriesScreen(vm: BreweriesScreenViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val infiniteScrollInProgressState =
        vm.infiniteScrollInProgressStateFlow.collectAsStateWithLifecycle(initialValue = false)
    val isLoading = vm.loadingStateFlow.collectAsStateWithLifecycle(initialValue = false)
    val error = vm.errorStateFlow.collectAsStateWithLifecycle(initialValue =  null)
    var shouldShowErrorDialog by remember { mutableStateOf(false) }

    shouldShowErrorDialog = error.value != null

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

    if(shouldShowErrorDialog){
        ErrorDialog(message =  error.value.orEmpty(), onCancelClicked = {
            shouldShowErrorDialog = false
            vm.clearError()
        })
    }
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
                    .combinedClickable(onLongClick = {
                        Log.d("BreweriesScreen", "onLongClick BREWERY[$index]: $brewery")
                    }, onClick = {
                        Log.d("BreweriesScreen", "onClick BREWERY[$index]: $brewery")
                    })

            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .placeholder(isLoading = isLoading),
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