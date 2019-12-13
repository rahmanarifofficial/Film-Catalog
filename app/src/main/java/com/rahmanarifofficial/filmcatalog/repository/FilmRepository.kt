package com.rahmanarifofficial.filmcatalog.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rahmanarifofficial.filmcatalog.BuildConfig
import com.rahmanarifofficial.filmcatalog.model.ResponseSearchFilm
import com.rahmanarifofficial.filmcatalog.network.DataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmRepository {
    private val service = DataSource.Private.service

    fun getListFilm(keyword: String, page: Int): MutableLiveData<ResponseSearchFilm> {
        val listFilm = MutableLiveData<ResponseSearchFilm>()
        service.getListFilm(BuildConfig.OMDB_API_KEY, keyword, page)
            .enqueue(object : Callback<ResponseSearchFilm> {
                override fun onFailure(call: Call<ResponseSearchFilm>, t: Throwable) {
                    listFilm.value = null
                    Log.d("RESPONSE", t.message.toString())
                }

                override fun onResponse(
                    call: Call<ResponseSearchFilm>,
                    response: Response<ResponseSearchFilm>
                ) {
                    Log.d("RESPONSE", "${response.isSuccessful}")
                    if (response.isSuccessful()) {
                        listFilm.value = response.body()
                    }
                }

            })
        return listFilm
    }
}