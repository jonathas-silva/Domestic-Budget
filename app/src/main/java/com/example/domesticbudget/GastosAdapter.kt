package com.example.domesticbudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domesticbudget.model.Gasto

//

class GastosAdapter(

) : RecyclerView.Adapter<GastosAdapter.GastosViewHolder>() {

    private var listaDeGastos : List<Gasto> = emptyList()

    fun recarregarListaDeGastos(lista: List<Gasto>){
        this.listaDeGastos = lista
        notifyDataSetChanged()
    }

    inner class GastosViewHolder(
        private val itemView: View
    ) : ViewHolder(itemView) {
        val descricao: TextView = itemView.findViewById(R.id.textDescricao)
        val valor: TextView = itemView.findViewById(R.id.textValor)
        val data: TextView = itemView.findViewById(R.id.textData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(
            R.layout.item_gastos,
            parent,
            false
        )
        return GastosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaDeGastos.size
    }

    override fun onBindViewHolder(holder: GastosViewHolder, position: Int) {
        val gasto = listaDeGastos[position]

        //belíssimo código para formatar a exibição do número float no RV
        fun Double.format(): String {
            return String.format("%.2f", this).replace('.', ',')
        }

        holder.descricao.text = gasto.descricao
        holder.valor.text = "R$ ${gasto.valor.format()}"
        holder.data.text = gasto.data

    }

}