package quebec.artm.breweryco.presentation.breweries.screens.landing

import android.util.Log
import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.usescases.GetAllBreweriesUseCase
import quebec.artm.breweryco.presentation.breweries.screens.landing.models.BreweriesScreenViewModelState
import quebec.artm.breweryco.presentation.breweries.screens.landing.models.BreweryUiData
import javax.inject.Inject

@HiltViewModel
class BreweriesScreenViewModel @Inject constructor(
    private val getAllBreweriesUseCase: GetAllBreweriesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BreweriesScreenViewModelState())
    val state: StateFlow<BreweriesScreenViewModelState> = _state
        .onStart { onCollectingStarted() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BreweriesScreenViewModelState()
        )

    private fun onCollectingStarted() {
        viewModelScope.launch {
            getAllBreweriesUseCase.invoke().fold(
                onSuccess = ::onBreweriesFetched,
                onFailure = ::onError
            )
        }
    }

    private fun onBreweriesFetched(breweries: List<Brewery>) {
        _state.update {
            it.copy(
                breweries = breweries.map { it.toUiModel() },
            )
        }
    }

    private fun onError(throwable: Throwable) {
    }

}

private fun Brewery.toUiModel(): BreweryUiData = BreweryUiData(
    key = id,
    name = name,
)
