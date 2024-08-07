package com.example.domesticbudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.database.GastoDAO
import com.example.domesticbudget.model.Gasto
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.sql.Array

class GastosFragment : Fragment() {

    private lateinit var rvGastos: RecyclerView
    private lateinit var menuCategorias: Spinner

    private var listaDeGastos: List<Gasto> = emptyList()
    private var gastosAdapter: GastosAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gastos, container, false)

        rvGastos = view.findViewById(R.id.recyclerGastos)
        menuCategorias = view.findViewById(R.id.spinnerCategorias)

        //definindo o adapter
        gastosAdapter = GastosAdapter { gasto: Gasto ->
            atualizarGasto(gasto)
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

        //POVOANDO DINÂMICAMENTE A LISTA DE CATEGORIAS
        val listaItens = arrayListOf("Todos","Todes","Alimentação")
        val categoriaDAO = CategoriaDAO(requireContext())
        val listaDeCategorias = categoriaDAO.listar()
        val listaDeNomesDeCategorias = arrayListOf<String>()

        listaDeNomesDeCategorias.add("Todos")
        listaDeCategorias.forEach {
            categoria -> listaDeNomesDeCategorias.add(categoria.nome)
        }


        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            listaDeNomesDeCategorias
        )

        menuCategorias.setAdapter(adapter)

        return view

    }

    private fun atualizarGasto(gasto: Gasto) {
        //Vamos criar um alert builder personalizado

        //Criando o alert builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Editar gasto")

        //definindo o layout customizado
        val customLayout: View = layoutInflater.inflate(R.layout.custom_alert_gastos_edt, null)
        builder.setView(customLayout)


        //povoando a dropdown de categorias de categorias
        val categoria = customLayout.findViewById<AutoCompleteTextView>(R.id.inputEditarCategoria)

        val categoriaDAO = CategoriaDAO(requireContext())
        val listaDeCategorias = categoriaDAO.listar()
        val listaNomesCategorias = ArrayList<String>()
        val listaDeIndices = ArrayList<Int>()
        listaDeCategorias.forEach { cat ->
            listaNomesCategorias.add(cat.nome)
            listaDeIndices.add(cat.idCategoria)
        }

        //recuperando as entradas dos editTexts
        val valorEditado = customLayout.findViewById<TextInputEditText>(R.id.InputValorEditGastos)
        val descricaoEditado = customLayout.findViewById<TextInputEditText>(R.id.InputDescricaoEditGastos)

        valorEditado.setText(gasto.valor.toString())
        descricaoEditado.setText(gasto.descricao)
        val numCategoria = listaDeIndices.indexOf(gasto.categoria)
        categoria.setText(listaNomesCategorias[numCategoria], false)

        //Adicionando um botão
        builder.setPositiveButton("Atualizar") { _, _ ->

            val indiceRelativoCategoria = listaNomesCategorias.indexOf(categoria.text.toString())
            val indiceRealCategoria = listaDeIndices[indiceRelativoCategoria]

            //Para simplificação não teremos opção de alterar a data, a priori
            //Caso seja necessário, adicionaremos essa opção depois
            val gastoEditado = Gasto(
                gasto.idGasto,
                valorEditado.text.toString().toDouble(),
                descricaoEditado.text.toString(),
                indiceRealCategoria,
                gasto.data
            )

            atualizarGastoEditado(gastoEditado)

        }
        builder.setNegativeButton("Cancelar"){ _, _ ->}

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item, listaNomesCategorias
        )
        categoria.setAdapter(adapter)

        //mandando os dados para o fragment de origem


        //criando e mostrando o alert dialog
        val dialog = builder.create()
        dialog.show()

    }

    private fun atualizarGastoEditado(gastoEditado: Gasto) {
        val gastoDAO = GastoDAO(requireContext())
        if (gastoDAO.atualizar(gastoEditado)){
            Toast.makeText(requireContext(), "Gasto atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            atualizarRVGastos()
        }else{
            Toast.makeText(requireContext(), "Erro ao atualizar gasto!", Toast.LENGTH_SHORT).show()
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