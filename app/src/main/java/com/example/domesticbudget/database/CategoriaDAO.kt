package com.example.domesticbudget.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.WindowManager.InvalidDisplayException
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

        val listaCategorias = mutableListOf<Categoria>()

        val sql = "SELECT * FROM ${DatabaseHelper.NOME_TABELA_CATEGORIAS}"

        val cursor = leitura.rawQuery(sql, null)

        //indices da tabela
        val indiceId = cursor.getColumnIndex(DatabaseHelper.COLUNA_CATEGORIAS_ID)
        val nomeId = cursor.getColumnIndex(DatabaseHelper.COLUNA_CATEGORIAS_NOME)



        while (cursor.moveToNext()) {
            val idCategoria = cursor.getInt(indiceId)
            val descricao = cursor.getString(nomeId)
            listaCategorias.add(
                Categoria(idCategoria, descricao, "200")
            )
        }

        cursor.close()
        return listaCategorias

    }
}