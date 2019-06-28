package br.com.matheus.coroutinetest.network

import br.com.matheus.coroutinetest.model.GeocodingResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI {
    @GET("reverse")
    suspend fun retornarEndereco(
        @Query("format") formato: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<GeocodingResult>
}