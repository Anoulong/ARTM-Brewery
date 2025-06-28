package quebec.artm.breweryco.domain.breweries.usescases

import kotlinx.coroutines.flow.Flow
import quebec.artm.breweryco.data.breweries.BreweriesRepository
import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.networking.ApiResult
import javax.inject.Inject

class GetAllBreweriesUseCase @Inject constructor(
    private val repository: BreweriesRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Flow<ApiResult> = repository.getBreweries(page, size)
}