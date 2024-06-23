package com.example.domesticbudget.database

import com.example.domesticbudget.model.Gasto

interface IGastoDAO {

    fun salvar(gasto: Gasto): Boolean

    fun atualizar(gasto: Gasto): Boolean

    fun deletar(indice: Int): Boolean

    fun listar(): List<Gasto>

}