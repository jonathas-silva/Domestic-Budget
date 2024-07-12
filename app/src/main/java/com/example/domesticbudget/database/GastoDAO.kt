package com.example.domesticbudget.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.domesticbudget.model.Gasto

class GastoDAO(context: Context) : IGastoDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase

    override fun salvar(gasto: Gasto): Boolean {
        val conteudos = ContentValues()
        conteudos.put(DatabaseHelper.COLUNA_GASTOS_VALOR, gasto.valor)
        conteudos.put(DatabaseHelper.COLUNA_GASTOS_DESCRICAO, gasto.descricao)
        conteudos.put(DatabaseHelper.COLUNA_GASTOS_CATEGORIA, gasto.categoria)
        conteudos.put(DatabaseHelper.COLUNA_GASTOS_DATA, gasto.data)


        try {
            escrita.insert(
                DatabaseHelper.NOME_TABELA_GASTOS,
                null,
                conteudos
            )
            Log.i("info_db", "Sucesso ao salvar gasto!")
        } catch (e: Exception) {
            Log.e("info_db", "Erro ao salvar gasto!!")
            return false
        }
        return true

    }

    override fun atualizar(gasto: Gasto): Boolean {

        val args = arrayOf(gasto.idGasto.toString())

        val conteudo = ContentValues()
        conteudo.put(DatabaseHelper.COLUNA_GASTOS_VALOR, gasto.valor)
        conteudo.put(DatabaseHelper.COLUNA_GASTOS_DESCRICAO, gasto.descricao)
        conteudo.put(DatabaseHelper.COLUNA_GASTOS_CATEGORIA, gasto.categoria)
        conteudo.put(DatabaseHelper.COLUNA_GASTOS_DATA, gasto.data)

        try {
                escrita.update(
                    DatabaseHelper.NOME_TABELA_GASTOS,
                    conteudo,
                    "${DatabaseHelper.COLUNA_GASTOS_ID} = ?",
                    args
                )
         }catch (e: Exception){
            Log.e("info_db", "Erro ao atualizar!")
            return false
        }
        return true
    }

    override fun deletar(indice: Int): Boolean {

        //Argumentos para deletar na tabela
        val args = arrayOf(indice.toString())

        try {
            escrita.delete(
                DatabaseHelper.NOME_TABELA_GASTOS,
                "${DatabaseHelper.COLUNA_GASTOS_ID} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao excluir gasto $indice")
        } catch (e: Exception) {
            Log.e("info_db", "Erro ao excluir gasto")
            return false
        }
        return true
    }

    override fun listar(): List<Gasto> {

        val listaDeGastos = mutableListOf<Gasto>()

        val sql = "SELECT * FROM ${DatabaseHelper.NOME_TABELA_GASTOS}"

        val cursor = leitura.rawQuery(sql, null)

        //indices da tabela
        val indiceId = cursor.getColumnIndex(DatabaseHelper.COLUNA_GASTOS_ID)
        val valorId = cursor.getColumnIndex(DatabaseHelper.COLUNA_GASTOS_VALOR)
        val descricaoId = cursor.getColumnIndex(DatabaseHelper.COLUNA_GASTOS_DESCRICAO)
        val categoriaId = cursor.getColumnIndex(DatabaseHelper.COLUNA_GASTOS_CATEGORIA)
        val dataId = cursor.getColumnIndex(DatabaseHelper.COLUNA_GASTOS_DATA)


        while (cursor.moveToNext()) {
            val idGasto = cursor.getInt(indiceId)
            val valor = cursor.getDouble(valorId)
            val descricao = cursor.getString(descricaoId)
            val categoria = cursor.getInt(categoriaId)
            val data = cursor.getString(dataId)
            listaDeGastos.add(
                Gasto(idGasto, valor, descricao, categoria, data)
            )
        }

        cursor.close()
        return listaDeGastos

    }
}