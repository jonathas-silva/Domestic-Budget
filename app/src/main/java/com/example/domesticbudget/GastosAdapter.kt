package com.example.domesticbudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

//

class GastosAdapter(
    private val lista: List<Gasto>
) : RecyclerView.Adapter<GastosAdapter.GastosViewHolder>() {

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
        return lista.size
    }

    override fun onBindViewHolder(holder: GastosViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}