package quebec.artm.breweryco.domain.breweries.usescases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import quebec.artm.breweryco.data.breweries.BreweriesRepository
import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.model.BreweryType
import quebec.artm.breweryco.domain.breweries.networking.ApiResult

@DisplayName("GetAllBreweriesUseCase")
class GetAllBreweriesUseCaseTest {

    private lateinit var repository: BreweriesRepository
    private lateinit var useCase: GetAllBreweriesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetAllBreweriesUseCase(repository)
    }

    @Nested
    @DisplayName("When invoking with valid parameters")
    inner class ValidParameters {

        @Test
        @DisplayName("Should delegate to repository with correct parameters")
        fun shouldDelegateToRepositoryWithCorrectParameters() = runTest {
            // Given
            val page = 1
            val size = 50
            val expectedFlow = flowOf(ApiResult.Loading)
            coEvery { repository.getBreweries(page, size) } returns expectedFlow

            // When
            val result = useCase.invoke(page, size)

            // Then
            assertThat(result).isEqualTo(expectedFlow)
            coVerify(exactly = 1) { repository.getBreweries(page, size) }
        }

        @Test
        @DisplayName("Should return success result when repository returns success")
        fun shouldReturnSuccessResultWhenRepositoryReturnsSuccess() = runTest {
            // Given
            val page = 1
            val size = 20
            val breweries = listOf(
                Brewery(
                    id = "1",
                    name = "Test Brewery 1",
                    address = "123 Test St",
                    type = BreweryType.MICRO,
                    latitude = 40.7128,
                    longitude = -74.0060,
                    phone = "555-0123",
                    websiteUrl = "https://testbrewery1.com"
                ),
                Brewery(
                    id = "2",
                    name = "Test Brewery 2",
                    address = "456 Test Ave",
                    type = BreweryType.BREWPUB,
                    latitude = 34.0522,
                    longitude = -118.2437,
                    phone = "555-0456",
                    websiteUrl = "https://testbrewery2.com"
                )
            )
            val expectedResult = ApiResult.Success(breweries)
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isInstanceOf(ApiResult.Success::class.java)
            val successResult = result.first() as ApiResult.Success<*>
            @Suppress("UNCHECKED_CAST")
            val actualBreweries = successResult.data as List<Brewery>
            assertThat(actualBreweries).hasSize(2)
            assertThat(actualBreweries).containsExactlyElementsOf(breweries)
        }

        @Test
        @DisplayName("Should return error result when repository returns error")
        fun shouldReturnErrorResultWhenRepositoryReturnsError() = runTest {
            // Given
            val page = 1
            val size = 10
            val errorMessage = "Network error occurred"
            val exception = RuntimeException(errorMessage)
            val expectedResult = ApiResult.Error(exception)
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isInstanceOf(ApiResult.Error::class.java)
            val errorResult = result.first() as ApiResult.Error
            assertThat(errorResult.error).isEqualTo(exception)
            assertThat(errorResult.error.message).isEqualTo(errorMessage)
        }

        @Test
        @DisplayName("Should return offline result when repository returns offline")
        fun shouldReturnOfflineResultWhenRepositoryReturnsOffline() = runTest {
            // Given
            val page = 1
            val size = 15
            val cachedBreweries = listOf(
                Brewery(
                    id = "cached-1",
                    name = "Cached Brewery",
                    address = "Cached Address",
                    type = BreweryType.REGIONAL
                )
            )
            val offlineException = RuntimeException("No internet connection")
            val expectedResult = ApiResult.Offline(offlineException, cachedBreweries)
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isInstanceOf(ApiResult.Offline::class.java)
            val offlineResult = result.first() as ApiResult.Offline<*>
            assertThat(offlineResult.error).isEqualTo(offlineException)
            @Suppress("UNCHECKED_CAST")
            val actualCachedData = offlineResult.cachedData as List<Brewery>
            assertThat(actualCachedData).containsExactlyElementsOf(cachedBreweries)
        }

        @Test
        @DisplayName("Should return loading result when repository returns loading")
        fun shouldReturnLoadingResultWhenRepositoryReturnsLoading() = runTest {
            // Given
            val page = 1
            val size = 25
            val expectedResult = ApiResult.Loading
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isEqualTo(ApiResult.Loading)
        }

        @Test
        @DisplayName("Should handle multiple emissions from repository flow")
        fun shouldHandleMultipleEmissionsFromRepositoryFlow() = runTest {
            // Given
            val page = 2
            val size = 30
            val breweries = listOf(
                Brewery(id = "flow-1", name = "Flow Brewery 1"),
                Brewery(id = "flow-2", name = "Flow Brewery 2")
            )
            val repositoryFlow = flowOf(
                ApiResult.Loading,
                ApiResult.Success(breweries)
            )
            coEvery { repository.getBreweries(page, size) } returns repositoryFlow

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0]).isEqualTo(ApiResult.Loading)
            assertThat(result[1]).isInstanceOf(ApiResult.Success::class.java)
            val successResult = result[1] as ApiResult.Success<*>
            @Suppress("UNCHECKED_CAST")
            val actualBreweries = successResult.data as List<Brewery>
            assertThat(actualBreweries).containsExactlyElementsOf(breweries)
        }
    }

    @Nested
    @DisplayName("When invoking with edge case parameters")
    inner class EdgeCaseParameters {

        @Test
        @DisplayName("Should handle zero page parameter")
        fun shouldHandleZeroPageParameter() = runTest {
            // Given
            val page = 0
            val size = 10
            val expectedResult = ApiResult.Success(emptyList<Brewery>())
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            coVerify(exactly = 1) { repository.getBreweries(page, size) }
        }

        @Test
        @DisplayName("Should handle large page size")
        fun shouldHandleLargePageSize() = runTest {
            // Given
            val page = 1
            val size = 1000
            val expectedResult = ApiResult.Loading
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isEqualTo(ApiResult.Loading)
            coVerify(exactly = 1) { repository.getBreweries(page, size) }
        }

        @Test
        @DisplayName("Should handle negative parameters")
        fun shouldHandleNegativeParameters() = runTest {
            // Given
            val page = -1
            val size = -10
            val expectedResult = ApiResult.Error(IllegalArgumentException("Invalid parameters"))
            coEvery { repository.getBreweries(page, size) } returns flowOf(expectedResult)

            // When
            val result = useCase.invoke(page, size).toList()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result.first()).isInstanceOf(ApiResult.Error::class.java)
            coVerify(exactly = 1) { repository.getBreweries(page, size) }
        }
    }
} 