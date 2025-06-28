package quebec.artm.breweryco.data.breweries

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import quebec.artm.breweryco.data.breweries.datasources.RemoteBreweriesDataSource
import quebec.artm.breweryco.data.breweries.models.BreweryDto
import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.model.BreweryType
import quebec.artm.breweryco.domain.breweries.networking.ApiResult
import javax.inject.Inject

interface BreweriesRepository {
    suspend fun getBreweries(page: Int, size: Int): Flow<ApiResult>
}

class BreweriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteBreweriesDataSource,
) : BreweriesRepository {
    override suspend fun getBreweries(page: Int, size: Int): Flow<ApiResult> = flow {
        try {
            emit(ApiResult.Loading)
            val response = remoteDataSource.getBreweries(page, size).distinctBy { it.id }.map {
                it.toDomain()
            }
            emit(ApiResult.Success(response))

        } catch (error: Throwable){
            emit(ApiResult.Error(error))
        }
    }

    private fun BreweryDto.toDomain(): Brewery {
        return Brewery(
            id = this.id.orEmpty(),
            name = this.name.orEmpty(),
            address1 = this.address1,
            type = this.breweryType?.let { BreweryType.fromId(it) },
            latitude = this.latitude,
            longitude = this.longitude,
            phone = this.phone,
        )
    }
}