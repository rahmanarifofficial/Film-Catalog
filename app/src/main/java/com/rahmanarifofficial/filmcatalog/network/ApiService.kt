package com.rahmanarifofficial.filmcatalog.network

import androidx.lifecycle.LiveData
import com.rahmanarifofficial.filmcatalog.BuildConfig
import com.rahmanarifofficial.filmcatalog.model.Film
import com.rahmanarifofficial.filmcatalog.model.ResponseSearchFilm
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(".")
    fun searchFilm(
        @Query("apikey") apikey: String,
        @Query("s") keyword: String
    ): Call<ResponseSearchFilm>

    @GET(".")
    fun getDetailFilm(
        @Query("apikey") apikey: String,
        @Query("i") id: String
    ): Call<Film>
}