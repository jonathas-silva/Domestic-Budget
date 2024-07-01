package com.example.domesticbudget.model

import java.io.Serializable

data class Categoria(
    val idCategoria: Int,
    val nome: String,
    val valor: Double,
    val periodo: String
) : Serializable
