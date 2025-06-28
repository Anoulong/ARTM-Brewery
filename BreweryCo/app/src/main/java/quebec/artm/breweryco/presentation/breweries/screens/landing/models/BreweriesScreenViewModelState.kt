package quebec.artm.breweryco.presentation.breweries.screens.landing.models

import quebec.artm.breweryco.domain.breweries.model.Brewery

data class BreweriesScreenViewModelState(
    val breweries : List<BreweryUiData> = emptyList(),
)