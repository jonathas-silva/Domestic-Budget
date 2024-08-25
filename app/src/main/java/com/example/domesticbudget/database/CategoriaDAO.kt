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

    /**Essa função remove um item do banco de dados pelo ID
    *
    *
    * @param idCategoria o ID da categoria a ser deletado
    *
    *@return `true` se conseguir deletar, `false` se não conseguir
     *
     * @author Jonathas Silva
    * */
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

    //Retorna o quanto foi gasto até agora na categoria especificada
    //Quando a leitura não encontra nada, retorna -1
    fun somarCategoria(categoriaId: Int): Double {

        val sql =
            "SELECT SUM(${DatabaseHelper.COLUNA_GASTOS_VALOR})  AS resultado FROM ${DatabaseHelper.NOME_TABELA_GASTOS} WHERE ${DatabaseHelper.COLUNA_GASTOS_CATEGORIA}=$categoriaId;\n"

        var resultado = -1.0

        val cursor = leitura.rawQuery(sql, null)

        /*É esperado apenas um resultado nessa consulta, que será um double.
        * Se o cursor estiver vazio, ou seja, se a query não tiver retornado nada,
        * o trecho de código abaixo não será executado.*/
        if (cursor.moveToFirst()) {
            resultado = cursor.getDouble(cursor.getColumnIndexOrThrow("resultado"))
        }

        cursor.close()
        return resultado
    }

    //Função que retorna a soma de todos os valores gastos para todas as categorias
    fun somarTodosGastos(): Double {

        val sql =
            "SELECT SUM(${DatabaseHelper.COLUNA_GASTOS_VALOR})  AS resultado FROM ${DatabaseHelper.NOME_TABELA_GASTOS};"

        var resultado = -1.0

        val cursor = leitura.rawQuery(sql, null)

        /*É esperado apenas um resultado nessa consulta, que será um double.
        * Se o cursor estiver vazio, ou seja, se a query não tiver retornado nada,
        * o trecho de código abaixo não será executado.*/
        if (cursor.moveToFirst()) {
            resultado = cursor.getDouble(cursor.getColumnIndexOrThrow("resultado"))
        }

        cursor.close()
        return resultado
    }

    fun somarTodosOrcamentos(): Double {

        val sql =
            "SELECT SUM(${DatabaseHelper.COLUNA_CATEGORIAS_VALOR})  AS resultado FROM ${DatabaseHelper.NOME_TABELA_CATEGORIAS};"

        var resultado = -1.0

        val cursor = leitura.rawQuery(sql, null)

        /*É esperado apenas um resultado nessa consulta, que será um double.
        * Se o cursor estiver vazio, ou seja, se a query não tiver retornado nada,
        * o trecho de código abaixo não será executado.*/
        if (cursor.moveToFirst()) {
            resultado = cursor.getDouble(cursor.getColumnIndexOrThrow("resultado"))
        }

        cursor.close()
        return resultado
    }

}