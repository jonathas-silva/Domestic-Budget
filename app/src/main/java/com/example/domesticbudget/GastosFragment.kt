package com.example.domesticbudget

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domesticbudget.database.GastoDAO
import com.example.domesticbudget.model.Gasto

class GastosFragment : Fragment() {

    private lateinit var rvGastos: RecyclerView

    private var listaDeGastos: List<Gasto> = emptyList()
    private var gastosAdapter: GastosAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gastos, container, false)

        rvGastos = view.findViewById(R.id.recyclerGastos)

        //definindo o adapter
        gastosAdapter = GastosAdapter(requireContext())
        rvGastos.adapter = gastosAdapter
        rvGastos.layoutManager = LinearLayoutManager(activity)

        /*ATENÇÃO PARA O ERRO!
        * O procedimento correto, que pode resultar em um erro difícil de identificar caso não seja seguido, é:
        * 1. Declarar o gastoAdapter como do tipo GastoAdapter.
        * 2. No momento da definição do adapter instanciar esse gastoAdapter para que as chamadas à função recarregarListaDeGastos
        * sejam feitas corretamente
        * 3. Referenciar o adapter do RV como esse nosso gastoAdapter instanciado.*/


        return view
    }

    override fun onStart() {
        super.onStart()
        atualizarRVGastos()
    }

    private fun atualizarRVGastos() {
        val gastoDAO = GastoDAO(requireContext())
        listaDeGastos = gastoDAO.listar()
        gastosAdapter?.recarregarListaDeGastos(listaDeGastos)
    }

}