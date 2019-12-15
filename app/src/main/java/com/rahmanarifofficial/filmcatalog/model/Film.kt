package com.rahmanarifofficial.filmcatalog.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Film(
    @SerializedName("imdbID")
    @Expose
    var imdbID: String? = null,
    @SerializedName("Title")
    @Expose
    var title: String? = null,
    @SerializedName("Year")
    @Expose
    var year: String? = null,
    @SerializedName("Released")
    @Expose
    var released: String? = null,
    @SerializedName("Genre")
    @Expose
    var genre: String? = null,
    @SerializedName("Runtime")
    @Expose
    var runtime: String? = null,
    @SerializedName("Director")
    @Expose
    var director: String? = null,
    @SerializedName("Writer")
    @Expose
    var writer: String? = null,
    @SerializedName("Plot")
    @Expose
    var plot: String? = null,
    @SerializedName("Languange")
    @Expose
    var languange: String? = null,
    @SerializedName("Country")
    @Expose
    var country: String? = null,
    @SerializedName("Awards")
    @Expose
    var awards: String? = null,
    @SerializedName("Poster")
    @Expose
    var poster: String? = null,
    @SerializedName("imdbRating")
    @Expose
    var rating: String? = null,
    @SerializedName("Production")
    @Expose
    var production: String? = null,
    @SerializedName("Type")
    @Expose
    var type: String? = null,
    @SerializedName("Response")
    @Expose
    var response: Boolean = false,
    @SerializedName("Error")
    @Expose
    var error: String? = null
)