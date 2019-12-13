package com.rahmanarifofficial.filmcatalog.model

import com.google.gson.annotations.SerializedName

data class ResponseSearchFilm(
    @SerializedName("Search")
    var search : List<Film>? = null,

    @SerializedName("Response")
    var response: Boolean = false
)