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
        const val COLUNA_CATEGORIAS_VALOR = "valor"
        const val COLUNA_CATEGORIAS_PERIODO = "periodo"

        //colunas da tabela gastos
        const val COLUNA_GASTOS_ID = "id"
        const val COLUNA_GASTOS_VALOR = "valor"
        const val COLUNA_GASTOS_DESCRICAO = "descricao"
        const val COLUNA_GASTOS_CATEGORIA = "categoria"
        const val COLUNA_GASTOS_DATA = "data"
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        if (!(db?.isReadOnly)!!){
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val antigoSql = "CREATE TABLE $NOME_TABELA_CATEGORIAS (\n" +
                "$COLUNA_CATEGORIAS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "$COLUNA_CATEGORIAS_NOME VARCHAR(100) NOT NULL);"

        val sqlTabela1 = "CREATE TABLE $NOME_TABELA_CATEGORIAS (\n" +
                "$COLUNA_CATEGORIAS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "$COLUNA_CATEGORIAS_NOME VARCHAR(100) NOT NULL,\n" +
                "$COLUNA_CATEGORIAS_VALOR NUMERIC(1000000,2) NOT NULL,\n" +
                "$COLUNA_CATEGORIAS_PERIODO VARCHAR(100)\n" +
                "  );"

        val sqlTabela2 = "CREATE TABLE $NOME_TABELA_GASTOS (\n" +
                "$COLUNA_GASTOS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "$COLUNA_GASTOS_VALOR REAL(1000000,2) NOT NULL DEFAULT 0,\n" +
                "$COLUNA_GASTOS_DESCRICAO VARCHAR(300),\n" +
                "$COLUNA_GASTOS_CATEGORIA INTEGER NOT NULL,\n" +
                "$COLUNA_GASTOS_DATA VARCHAR(100),\n" +
                "FOREIGN KEY($COLUNA_GASTOS_CATEGORIA) REFERENCES $NOME_TABELA_CATEGORIAS($COLUNA_CATEGORIAS_ID));"

        /*Vamos fazer duas chamadas sequenciais para o execSQL, porque só podemos criar a tabela gastos
        * se conseguirmos criar a tabela categorias, pela relação de FK*/
        try {
            db?.execSQL(sqlTabela1)
            Log.i("info_db", "Sucesso ao criar tabela categorias!")

            try {
                db?.execSQL(sqlTabela2)
                Log.i("info_db", "Sucesso ao criar tabela gastos!")
            } catch (e: Exception) {
                Log.e("info_db", "Erro ao criar tabela gastos!!")
            }

        } catch (e: Exception) {
            Log.e("info_db", "Erro ao criar tabela categorias!!")
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}