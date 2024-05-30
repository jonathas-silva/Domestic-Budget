package com.example.domesticbudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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

        rvCategorias = view.findViewById(R.id.recyclerCategorias)
        btnNovaCategoria = view.findViewById(R.id.btnNovaCategoria)
        
        btnNovaCategoria.setOnClickListener {
            Toast.makeText(activity, "clicado", Toast.LENGTH_SHORT).show()
        }
        
        return view

    }

}
