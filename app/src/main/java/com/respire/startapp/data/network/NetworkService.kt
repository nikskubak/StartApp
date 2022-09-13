package com.respire.startapp.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.respire.startapp.data.database.Record
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface NetworkService {

    @get:GET(".")
    val getEntities: Call<Record>

    companion object Factory {

        private var authRetrofitService: NetworkService? = null

        fun getAuthRetrofitService(baseUrl: String): NetworkService? {
            if (authRetrofitService == null) {
                val client = initOkHttpClient()
                val converter = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
                val retrofit = getRetrofit(client, converter, baseUrl)
                authRetrofitService = retrofit.create(NetworkService::class.java)
            }
            return authRetrofitService
        }

        private fun initOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = getHttpBuilder(interceptor)
            return builder.build()
        }

        private fun getRetrofit(client: OkHttpClient, converter: Gson, baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(converter))
                .build()
        }

        private fun getHttpBuilder(interceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
        }
    }
}
