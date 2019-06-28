package br.com.matheus.coroutinetest.model

import java.io.Serializable

class Parada(
    val codigo: String,
    val latitude: Double,
    val longitude: Double,
    val terminal: String,
    val linhas: List<Linha>
): Serializable