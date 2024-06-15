package com.example.domesticbudget.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.domesticbudget.model.Categoria

class CategoriaDAO(context: Context) : ICategoriasDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase

    override fun salvar(categoria: Categoria): Boolean {

        val conteudos = ContentValues()
        conteudos.put(DatabaseHelper.COLUNA_CATEGORIAS_NOME, categoria.nome)

        try {
            escrita.insert(
                DatabaseHelper.NOME_TABELA_CATEGORIAS,
                null,
                conteudos
            )
            Log.i("info_db", "Sucesso ao salvar categoria!")
        } catch (e: Exception) {
            Log.e("info_db", "Erro ao salvar categoria!!")
            return false
        }
        return true
    }

    override fun atualizar(categoria: Categoria): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(idCategoria: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun listar(): List<Categoria> {
        TODO("Not yet implemented")
    }
}