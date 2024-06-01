package com.example.domesticbudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


//R.layout.fragment_categorias
class CategoriasFragment : Fragment() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var btnNovaCategoria: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.fragment_categorias,
            container,
            false
        )

        //LISTA APENAS PARA TESTE DO ADAPTER.
        //PARA PROD SERÁ UTILIZADO O BANCO DE DADOS, QUE SERÁ IMPLEMENTADO POSTERIORMENTE
        val lista = listOf<String>("Alimentação", "Rolê", "Casa", "Farmácia", "Combustível")


        rvCategorias = view.findViewById(R.id.recyclerCategorias)
        btnNovaCategoria = view.findViewById(R.id.btnNovaCategoria)

        rvCategorias.adapter = CategoriasAdapter(lista)
        rvCategorias.layoutManager = LinearLayoutManager(activity)

        btnNovaCategoria.setOnClickListener {
            Toast.makeText(activity, "clicado", Toast.LENGTH_SHORT).show()
        }
        
        return view

    }

}
