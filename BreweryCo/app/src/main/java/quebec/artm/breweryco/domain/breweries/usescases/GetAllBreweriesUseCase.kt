package quebec.artm.breweryco.domain.breweries.usescases

import quebec.artm.breweryco.domain.breweries.BreweriesRepository
import quebec.artm.breweryco.domain.breweries.model.Brewery
import javax.inject.Inject

class GetAllBreweriesUseCase @Inject constructor(
    private val repository: BreweriesRepository
) {
    suspend operator fun invoke(): Result<List<Brewery>> = repository.getBreweries()
}