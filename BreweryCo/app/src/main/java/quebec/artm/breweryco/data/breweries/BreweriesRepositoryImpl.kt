package quebec.artm.breweryco.data.breweries

import quebec.artm.breweryco.data.breweries.datasources.RemoteBreweriesDataSource
import quebec.artm.breweryco.data.breweries.models.BreweryDto
import quebec.artm.breweryco.domain.breweries.BreweriesRepository
import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.model.BreweryType
import javax.inject.Inject

class BreweriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteBreweriesDataSource,
) : BreweriesRepository {
    override suspend fun getBreweries(): Result<List<Brewery>> = runCatching {
        remoteDataSource.getBreweries(1, 100).mapNotNull {
            try {
                it.toDomain()
            } catch (_: Exception) {
                null
            }
        }
    }

    private fun BreweryDto.toDomain(): Brewery {
        return Brewery(
            id = this.id!!,
            name = this.name!!,
            address1 = this.address1!!,
            type = BreweryType.fromId(this.breweryType!!),
            latitude = this.latitude!!,
            longitude = this.longitude!!,
            phone = this.phone,
        )
    }
}