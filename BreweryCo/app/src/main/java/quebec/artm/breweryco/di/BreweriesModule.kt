package quebec.artm.breweryco.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import quebec.artm.breweryco.data.breweries.BreweriesRepository
import quebec.artm.breweryco.data.breweries.BreweriesRepositoryImpl
import quebec.artm.breweryco.data.breweries.datasources.RemoteBreweriesDataSource
import quebec.artm.breweryco.domain.breweries.networking.NetworkConnectivityManager
import quebec.artm.breweryco.domain.breweries.networking.NetworkConnectivityManagerImpl
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BreweriesModule {
    @Binds
    abstract fun BreweriesRepository(impl: BreweriesRepositoryImpl): BreweriesRepository

    companion object {
        @Singleton
        @Provides
        fun provideRemoteDataSource(
            retrofit: Retrofit
        ): RemoteBreweriesDataSource {
            return retrofit.create(RemoteBreweriesDataSource::class.java)
        }

        @Provides
        @Singleton
        internal fun provideNetworkConnectivityService(
            @ApplicationContext applicationContext: Context,
        ): NetworkConnectivityManager {
            return NetworkConnectivityManagerImpl(applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        }

    }
}