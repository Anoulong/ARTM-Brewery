package quebec.artm.breweryco.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    companion object {
        @Provides
        @Singleton
        fun provideRetrofitBuilder(): Retrofit {
            val client = OkHttpClient()
                .newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            return Retrofit
                .Builder()
                .client(client)
                .baseUrl("https://api.openbrewerydb.org/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}