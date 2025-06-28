package quebec.artm.breweryco.domain.breweries.model

enum class BreweryType(val id: String) {
    MICRO("micro"),
    NANO("nano"),
    REGIONAL("regional"),
    BREWPUB("brewpub"),
    LARGE("large"),
    PLANNING("planning"),
    BAR("bar"),
    CONTRACT("contract"),
    PROPRIETOR("proprietor"),
    CLOSED("closed");

    companion object {
        fun fromId(id: String): BreweryType {
            return BreweryType.entries.firstOrNull { it.id == id } ?: CLOSED
        }
    }
}