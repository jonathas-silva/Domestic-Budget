package com.example.domesticbudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domesticbudget.model.Categoria

class CategoriasAdapter(
    //private val lista: List<Categoria>
) : Adapter<CategoriasAdapter.CategoriasViewHolder>() {

    private var listaCategorias: List<Categoria> = emptyList()
    fun recarregarLista(lista: List<Categoria>) {
        this.listaCategorias = lista
        notifyDataSetChanged()
    }

    inner class CategoriasViewHolder(
        private val itemView: View
    ) : ViewHolder(itemView) {

        val nomeCategoria: TextView = itemView.findViewById(R.id.nomeCategoria)
        val valorCategoria: TextView = itemView.findViewById(R.id.valorCategoria)
        val periodo: TextView = itemView.findViewById(R.id.txtPeriodoOrcamento)
        val valorRestante: TextView = itemView.findViewById(R.id.txtValorRestante)

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
        return listaCategorias.size
    }

    //onBindViewHolder é chamado na vinculação dos dados para a visualização
    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {

        //Aqui estamos atribuindo a posição da lista, que inicialmente é uma string
        //para a variável nome. Em seguida vamos inserir esse valor na textView do itemCategoria que inflamos
        val categoria = listaCategorias[position]

        fun Double.format(): String {
            return String.format("%.2f", this).replace('.', ',')
        }

        //Esse valor gasto está estático, mas será calculado dinamicamente posteriormente
        val valorGasto = 200.00
        val restante = categoria.valor - valorGasto

        holder.nomeCategoria.text = categoria.nome
        holder.valorCategoria.text = "R$ ${categoria.valor.format()}"
        holder.periodo.text = categoria.periodo
        holder.valorRestante.text = "Restam R$ ${restante.format()}"
    }

}