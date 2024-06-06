package com.example.domesticbudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GastosFragment : Fragment() {

    private lateinit var rvGastos : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gastos, container, false)

        rvGastos = view.findViewById(R.id.recyclerGastos)

        val lista = listOf<Gasto>(
            Gasto("Potes no maravilhas do lar", 45.59,"02/02/2020"),
            Gasto("Xbox Series S", 2499.99,"02/02/2020"),
            Gasto("Jogo do tigrinho", 9999.99,"02/02/2020")
        )


        //definindo o adapter
        rvGastos.adapter = GastosAdapter(lista)
        rvGastos.layoutManager = LinearLayoutManager(activity)


        return view
    }

}