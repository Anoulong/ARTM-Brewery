package quebec.artm.breweryco.presentation.breweries.screens.landing

import android.util.Log
import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.networking.ApiResult
import quebec.artm.breweryco.domain.breweries.usescases.GetAllBreweriesUseCase
import quebec.artm.breweryco.presentation.breweries.screens.landing.models.BreweriesScreenViewModelState
import quebec.artm.breweryco.presentation.breweries.screens.landing.models.BreweryUiData
import javax.inject.Inject

@HiltViewModel
class BreweriesScreenViewModel @Inject constructor(
    private val getAllBreweriesUseCase: GetAllBreweriesUseCase
) : ViewModel() {

    private val _loadingStateFlow = MutableStateFlow<Boolean>(false)
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    private val _infiniteScrollInProgressStateFlow = MutableStateFlow<Boolean>(false)
    val infiniteScrollInProgressStateFlow = _infiniteScrollInProgressStateFlow.asStateFlow()

    private var currentPage = 1
    private val pageSize = 100

    private val _state = MutableStateFlow(BreweriesScreenViewModelState())
    val state: StateFlow<BreweriesScreenViewModelState> = _state
        .onStart {
            _loadingStateFlow.value = true
            onCollectingStarted() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BreweriesScreenViewModelState()
        )

    private fun onCollectingStarted() {
        viewModelScope.launch {
            _loadingStateFlow.value = true
            getAllBreweriesUseCase.invoke(currentPage, pageSize).collectLatest {  result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                       // delay(5000) //to display shimmering loading
                        _loadingStateFlow.value = false
                        val remoteData = result.data as List<Brewery>
                        onBreweriesFetched(remoteData)
                    }

                    is ApiResult.Offline<*> -> {
                        _loadingStateFlow.value = false
                    }

                    is ApiResult.Error -> {
                        _loadingStateFlow.value = false
                    }

                    ApiResult.Loading -> {
                        _loadingStateFlow.value = true
                        //placeholder data for shimmer effect
                        _state.value = BreweriesScreenViewModelState(breweries = List(20) { listOf(BreweryUiData(key = it.toString(), name = "")) }.flatten())

                    }
                }

            }
        }
    }

    private fun onBreweriesFetched(breweries: List<Brewery>) {
        _state.update { brewState ->
            brewState.copy(
                breweries = _state.value.breweries.filter { b -> b.name.isNotBlank() } + breweries.map { brew -> brew.toUiModel() },
            )
        }
        _loadingStateFlow.value = false
        _infiniteScrollInProgressStateFlow.value = false
    }

    fun onLoadMoreTriggered() {
        currentPage++
        viewModelScope.launch {
            _infiniteScrollInProgressStateFlow.value = true
//            delay(1000)//to display infinite loading
            getAllBreweriesUseCase.invoke(currentPage, pageSize).collectLatest {  result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        _infiniteScrollInProgressStateFlow.value = false
                        val remoteData = result.data as List<Brewery>
                        onBreweriesFetched(remoteData)
                    }

                    is ApiResult.Offline<*> -> {
                        _infiniteScrollInProgressStateFlow.value = false
                    }

                    is ApiResult.Error -> {
                        _infiniteScrollInProgressStateFlow.value = false
                    }

                    ApiResult.Loading -> {
                        _infiniteScrollInProgressStateFlow.value = true
                    }
                }

            }
        }
    }


}

private fun Brewery.toUiModel(): BreweryUiData = BreweryUiData(
    key = id,
    name = name,
)
