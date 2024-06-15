package com.example.domesticbudget.database

import com.example.domesticbudget.model.Categoria

interface ICategoriasDAO {
    fun salvar(categoria: Categoria): Boolean
    fun atualizar(categoria: Categoria):Boolean
    fun remover(idCategoria: Int): Boolean
    fun listar(): List<Categoria>
}