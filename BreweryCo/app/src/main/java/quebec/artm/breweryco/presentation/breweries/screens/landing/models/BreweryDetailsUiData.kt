package quebec.artm.breweryco.presentation.breweries.screens.landing.models

import quebec.artm.breweryco.domain.breweries.model.Brewery
import quebec.artm.breweryco.domain.breweries.model.BreweryType

data class BreweryDetailsUiData(
    val key: String,
    val name: String,
    val address: String,
    val type: BreweryType,
    val website: String,
    val phone: String,
) {
    constructor(brewery: Brewery): this(
        key = brewery.id,
        name = brewery.name,
        address = brewery.address.orEmpty(),
        type = brewery.type ?: BreweryType.CLOSED,
        website = brewery.websiteUrl.orEmpty(),
        phone = brewery.phone.orEmpty()
    )
}
