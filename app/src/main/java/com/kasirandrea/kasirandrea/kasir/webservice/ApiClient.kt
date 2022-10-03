package com.kasirandrea.kasirandrea.kasir.webservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null

        private fun getClient(): Retrofit {

            var opt = OkHttpClient.Builder().apply {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()

            retrofit = Retrofit.Builder().apply {
                client(opt)
                baseUrl(Constant.api_backend)
                addConverterFactory(GsonConverterFactory.create())
            }.build()
            retrofit!!

            return retrofit!!

        }

        fun instance() = getClient().create(ApiService::class.java)
    }

}
