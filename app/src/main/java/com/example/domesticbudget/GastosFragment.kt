package com.example.domesticbudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domesticbudget.database.GastoDAO
import com.example.domesticbudget.model.Gasto
import com.google.android.material.snackbar.Snackbar

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
        gastosAdapter = GastosAdapter { id ->
            confirmarExclusao(id)
        }
        rvGastos.adapter = gastosAdapter
        rvGastos.layoutManager = LinearLayoutManager(activity)
        itemTouchHelper.attachToRecyclerView(rvGastos)

        /*ATENÇÃO PARA O ERRO!
        * O procedimento correto, que pode resultar em um erro difícil de identificar caso não seja seguido, é:
        * 1. Declarar o gastoAdapter como do tipo GastoAdapter.
        * 2. No momento da definição do adapter instanciar esse gastoAdapter para que as chamadas à função recarregarListaDeGastos
        * sejam feitas corretamente
        * 3. Referenciar o adapter do RV como esse nosso gastoAdapter instanciado.*/


        return view

    }


    private fun confirmarExclusao(id: Int) {

        val gastoDAO = GastoDAO(requireContext())
        if (gastoDAO.deletar(id)) {
            Toast.makeText(requireContext(), "Gasto deletado!", Toast.LENGTH_SHORT).show()
            atualizarRVGastos()
        } else {
            Toast.makeText(requireContext(), "Erro ao tentar deletar gasto!", Toast.LENGTH_SHORT)
                .show()
        }

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

    private val itemTouchHelper =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val gastoDAO = GastoDAO(requireContext())

                /*Estamos usando non-null assertion aqui por que temos certeza de que a variável
                * gastosAdapter sempre será instanciada na criação da view, e nesse ponto da execução
                * nunca será nula.
                * Podemos posteriormente mudar a forma como essa variável é instanciada.*/
                if (gastoDAO.deletar(gastosAdapter?.recuperarId(viewHolder.adapterPosition)!!)) {
                    view?.let {
                        Snackbar.make(
                            it.findViewById(R.id.recyclerGastos),
                            "Item deletado!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao tentar deletar gasto!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                //A RV será atualizada tanto se der quanto se não der certo para evitar problemas de renderização
                //da função onSwipea
                /*Para aproveitar a fluidez do material design da melhor maneira,
                 * optei por utilizar a notifyItemRemoved ao invés de utilizar notifySetDataChanged.*/
                val novalistaDeGastos = gastoDAO.listar()
                gastosAdapter?.recarregarListaPorDelecao(
                    novalistaDeGastos,
                    viewHolder.adapterPosition //viewHolder.adapterPosition reflete a posição do item no RV
                )
            }
        })

}