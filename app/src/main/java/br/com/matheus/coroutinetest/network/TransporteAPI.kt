package br.com.matheus.coroutinetest.network

import br.com.matheus.coroutinetest.model.Parada
import retrofit2.Response
import retrofit2.http.GET

interface TransporteAPI {

    @GET("php/facades/process.php?a=tp")
    suspend fun retornarParadas(): Response<List<Parada>>
}