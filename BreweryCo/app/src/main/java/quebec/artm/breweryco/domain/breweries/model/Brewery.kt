package quebec.artm.breweryco.domain.breweries.model

data class Brewery(
    val id: String,
    val name: String,
    val address1: String? = null,
    val type: BreweryType? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val phone: String? = null
)