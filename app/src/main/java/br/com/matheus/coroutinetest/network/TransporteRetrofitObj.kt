package br.com.matheus.coroutinetest.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TransporteRetrofitObj {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    val api: TransporteAPI = retrofit()
        .create(TransporteAPI::class.java)


    private fun retrofit(): Retrofit{
        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://www.poatransporte.com.br/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}