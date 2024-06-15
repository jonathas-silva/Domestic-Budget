package com.example.domesticbudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domesticbudget.model.Categoria

class CategoriasAdapter (
    private val lista: List<Categoria>
): Adapter<CategoriasAdapter.CategoriasViewHolder>() {

    inner class CategoriasViewHolder(
        private val itemView: View
    ): ViewHolder(itemView){

        val nomeCategoria: TextView = itemView.findViewById(R.id.nomeCategoria)
        val valorCategoria: TextView = itemView.findViewById(R.id.valorCategoria)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {

        val layoutInflater = LayoutInflater.from(
            parent.context
        )

        val itemView = layoutInflater.inflate(
            R.layout.item_categorias,
            parent,
            false
        )

        //precisamos retornar a conversão do nosso layout como uma view
        //No nosso caso vamos utilizar o layoutInflater para fazer esse processo
        return CategoriasViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    //onBindViewHolder é chamado na vinculação dos dados para a visualização
    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {

        //Aqui estamos atribuindo a posição da lista, que inicialmente é uma string
        //para a variável nome. Em seguida vamos inserir esse valor na textView do itemCategoria que inflamos
        val categoria = lista[position]
        holder.nomeCategoria.text = categoria.nome
        holder.valorCategoria.text = categoria.valor
    }

}