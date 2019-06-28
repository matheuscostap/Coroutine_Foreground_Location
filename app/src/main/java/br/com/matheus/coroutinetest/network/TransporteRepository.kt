package br.com.matheus.coroutinetest.network

import br.com.matheus.coroutinetest.model.Parada

interface TransporteRepository {
    suspend fun retornarParadas(): List<Parada>
}


class TransporteRepositoryImpl: TransporteRepository {

    private val api = TransporteRetrofitObj.api

    override suspend fun retornarParadas(): List<Parada>{
        val ret = api.retornarParadas().body()
        return if (ret.isNullOrEmpty()){
            emptyList()
        }else{
            ret
        }
    }

}