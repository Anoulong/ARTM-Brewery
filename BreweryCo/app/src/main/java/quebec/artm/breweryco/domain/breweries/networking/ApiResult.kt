package quebec.artm.breweryco.domain.breweries.networking

import android.os.Parcelable

sealed class ApiResult {
    class Success<T>(val data: T) : ApiResult()

    class Error(val error: Throwable) : ApiResult()

    class Offline<T>(val error: Throwable?, val cachedData: T) : ApiResult()

    object Loading : ApiResult()
}