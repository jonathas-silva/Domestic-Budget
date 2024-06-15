package com.example.domesticbudget.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, NOME_BANCO_DADOS, null, VERSAO) {


    companion object {
        const val NOME_BANCO_DADOS = "orcamentos.db"
        const val VERSAO = 1
        const val NOME_TABELA_CATEGORIAS = "categorias"
        const val NOME_TABELA_GASTOS = "gastos"
        const val NOME_TABELA_ORCAMENTOS = "orcamentos"

        //colunas da tabela categorias
        const val COLUNA_CATEGORIAS_ID = "id"
        const val COLUNA_CATEGORIAS_NOME = "nome"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql =   "CREATE TABLE $NOME_TABELA_CATEGORIAS (\n" +
                    "$COLUNA_CATEGORIAS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "$COLUNA_CATEGORIAS_NOME VARCHAR(100) NOT NULL);"

        try {
            db?.execSQL(sql)
            Log.i("info_db", "Sucesso ao criar tabela categorias!")
        }catch (e: Exception){
            Log.e("info_db", "Erro ao criar tabela categorias!!")
        }


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}