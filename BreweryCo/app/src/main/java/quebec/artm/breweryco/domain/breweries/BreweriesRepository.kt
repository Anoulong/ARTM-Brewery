package quebec.artm.breweryco.domain.breweries

import quebec.artm.breweryco.domain.breweries.model.Brewery

interface BreweriesRepository {
    suspend fun getBreweries(): Result<List<Brewery>>
}