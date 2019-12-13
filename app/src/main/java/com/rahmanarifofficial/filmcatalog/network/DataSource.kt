package com.rahmanarifofficial.filmcatalog.network

import com.rahmanarifofficial.filmcatalog.BuildConfig
import com.rahmanarifofficial.filmcatalog.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Aby Fajar(www.androidtinker.com)
 * abyfajar@gmail.com
 * on 26 November 2018
 */
class DataSource {
    companion object {

        var errorSessionCode = arrayOf("93", "91")

        val private: Retrofit by lazy {
            val client = OkHttpClient.Builder()
            client.connectTimeout(Constant.TIME_OUT_REQUEST.toLong(), TimeUnit.MILLISECONDS)
            client
                .readTimeout(Constant.TIME_OUT_REQUEST.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(Constant.TIME_OUT_REQUEST.toLong(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)

            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }

    }

    open class Private {
        companion object {
            val service: ApiService by lazy {
                private.create(ApiService::class.java)
            }
        }
    }
}