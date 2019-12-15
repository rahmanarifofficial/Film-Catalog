package com.rahmanarifofficial.filmcatalog.model

import com.google.gson.annotations.SerializedName

data class ResponseSearchFilm(
    @SerializedName("Search")
    var search : List<Film>? = null,

    @SerializedName("Response")
    var response: Boolean = false,

    @SerializedName("totalResults")
    var totalResults: Int? = null,

    @SerializedName("Error")
    var error: String? = null
)