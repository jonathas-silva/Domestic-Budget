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
        conteudos.put(DatabaseHelper.COLUNA_CATEGORIAS_VALOR, categoria.valor)
        conteudos.put(DatabaseHelper.COLUNA_CATEGORIAS_PERIODO, categoria.periodo)


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

        val args = arrayOf(categoria.idCategoria.toString())

        val conteudo = ContentValues()
        conteudo.put(DatabaseHelper.COLUNA_CATEGORIAS_NOME, categoria.nome)
        conteudo.put(DatabaseHelper.COLUNA_CATEGORIAS_VALOR, categoria.valor)
        conteudo.put(DatabaseHelper.COLUNA_CATEGORIAS_PERIODO, categoria.periodo)

        try {
            escrita.update(
                DatabaseHelper.NOME_TABELA_CATEGORIAS,
                conteudo,
                "${DatabaseHelper.COLUNA_CATEGORIAS_ID} = ?",
                args
            )
        } catch (e: Exception) {
            Log.e("info_db", "Erro ao atualizar categoria!!")
            return false
        }


        return true
    }

    override fun remover(idCategoria: Int): Boolean {

        val args = arrayOf(idCategoria.toString())

        try {
            escrita.delete(
                DatabaseHelper.NOME_TABELA_CATEGORIAS,
                "${DatabaseHelper.COLUNA_CATEGORIAS_ID} = ?",
                args
            )

            Log.i("info_db", "Sucesso ao deletar categoria!")
        } catch (e: Exception) {
            Log.e("info_db", "Erro ao deletar categoria!")

            return false
        }
        return true
    }

    override fun listar(): List<Categoria> {

        val listaCategorias = mutableListOf<Categoria>()

        val sql = "SELECT * FROM ${DatabaseHelper.NOME_TABELA_CATEGORIAS}"

        val cursor = leitura.rawQuery(sql, null)

        //indices da tabela
        val indiceId = cursor.getColumnIndex(DatabaseHelper.COLUNA_CATEGORIAS_ID)
        val nomeId = cursor.getColumnIndex(DatabaseHelper.COLUNA_CATEGORIAS_NOME)
        val valorId = cursor.getColumnIndex(DatabaseHelper.COLUNA_CATEGORIAS_VALOR)
        val periodoId = cursor.getColumnIndex(DatabaseHelper.COLUNA_CATEGORIAS_PERIODO)


        while (cursor.moveToNext()) {
            val idCategoria = cursor.getInt(indiceId)
            val descricao = cursor.getString(nomeId)
            val valor = cursor.getDouble(valorId)
            val periodo = cursor.getString(periodoId)
            listaCategorias.add(
                Categoria(idCategoria, descricao, valor, periodo)
            )
        }

        cursor.close()
        return listaCategorias

    }

}