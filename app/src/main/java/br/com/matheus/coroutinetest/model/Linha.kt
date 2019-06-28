package br.com.matheus.coroutinetest.model

import java.io.Serializable

class Linha(
    val idLinha: String,
    val codigoLinha: String,
    val nomeLinha: String
): Serializable