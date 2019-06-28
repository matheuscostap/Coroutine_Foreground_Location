package br.com.matheus.coroutinetest.network

import br.com.matheus.coroutinetest.model.GeocodingResult

interface GeocodingRepository {
    suspend fun retornarEndereco(formato: String, lat: String, lon: String) : GeocodingResult?
}


class GeocodingRepositoryImpl: GeocodingRepository{

    private val api = GeocodingRetrofitObj.api

    override suspend fun retornarEndereco(formato: String, lat: String, lon: String): GeocodingResult? {
        return api.retornarEndereco(formato,lat,lon).body()
    }

}
