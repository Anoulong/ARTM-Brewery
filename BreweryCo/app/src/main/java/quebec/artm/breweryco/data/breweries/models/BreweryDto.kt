package quebec.artm.breweryco.data.breweries.models


import com.google.gson.annotations.SerializedName

data class BreweryDto(
    @SerializedName("address_1")
    val address1: String?,
    @SerializedName("address_2")
    val address2: Any?,
    @SerializedName("address_3")
    val address3: Any?,
    @SerializedName("brewery_type")
    val breweryType: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("postal_code")
    val postalCode: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("state_province")
    val stateProvince: String?,
    @SerializedName("street")
    val street: String?,
    @SerializedName("website_url")
    val websiteUrl: String?
)