package quebec.artm.breweryco.domain.breweries.model

data class Brewery(
    val id: String,
    val name: String,
    val address1: String,
    val type: BreweryType,
    val latitude: Double,
    val longitude: Double,
    val phone: String?
)