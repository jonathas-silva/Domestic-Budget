package com.example.domesticbudget.model

import java.io.Serializable

data class Gasto(
    val idGasto: Int,
    val valor: Double,
    val descricao: String,
    val categoria: Int,
    val data: String
    ): Serializable
