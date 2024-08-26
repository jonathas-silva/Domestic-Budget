package com.example.domesticbudget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domesticbudget.Utilidades.addCurrencyMask
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.database.GastoDAO
import com.example.domesticbudget.model.Categoria
import com.example.domesticbudget.model.Gasto
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

class GastosFragment : Fragment() {

    private lateinit var rvGastos: RecyclerView
    private lateinit var menuCategorias: Spinner
    private lateinit var progressBar: ProgressBar

    private lateinit var porcentagemGastos: TextView
    private lateinit var valorTotalOrcamento: TextView
    private lateinit var valorTotalGasto: TextView


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
        progressBar = view.findViewById(R.id.progressBar)

        porcentagemGastos = view.findViewById(R.id.txtPorcentagem)
        valorTotalOrcamento = view.findViewById(R.id.txtValorTotalOrcamento)
        valorTotalGasto = view.findViewById(R.id.txtValorTotalGasto)


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
        val descricaoEditado =
            customLayout.findViewById<TextInputEditText>(R.id.InputDescricaoEditGastos)

        valorEditado.addCurrencyMask()

        valorEditado.setText(
            NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(gasto.valor)
        )
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
                Utilidades.limpadorDeFormatacao(valorEditado.text.toString()),
                descricaoEditado.text.toString(),
                indiceRealCategoria,
                gasto.data
            )

            atualizarGastoEditado(gastoEditado)

            //Depois de atualizado o gasto editado, vamos chamar novamente o sppiner para atualizar a lista mostrada e os gráficos
            //Aqui ele pode lançar um outOfBounds Exception, então vamos usar um try. Caso pegue um erro, mostra todas as categorias
            try {
                menuCategorias.setSelection(indiceRelativoCategoria + 1)
            } catch (e: Exception) {
                Log.e("info_db", "Problema no recarregamento da lista ao atualizar gastos!")
                menuCategorias.setSelection(0)
            }

            Log.i("info_db", indiceRelativoCategoria.toString())

        }
        builder.setNegativeButton("Cancelar") { _, _ -> }

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
        if (gastoDAO.atualizar(gastoEditado)) {
            Toast.makeText(requireContext(), "Gasto atualizado com sucesso!", Toast.LENGTH_SHORT)
                .show()
            atualizarRVGastos(gastoEditado.categoria)

        } else {
            Toast.makeText(requireContext(), "Erro ao atualizar gasto!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart() {

        //POVOANDO DINÂMICAMENTE A LISTA DE CATEGORIAS
        val categoriaDAO = CategoriaDAO(requireContext())
        val listaDeCategorias = categoriaDAO.listar()
        val listaDeNomesDeCategorias = arrayListOf<String>()

        listaDeNomesDeCategorias.add("Todos")
        listaDeCategorias.forEach { categoria ->
            listaDeNomesDeCategorias.add(categoria.nome)
        }


        // ===================== --- =====================
        //Aqui vamos fazer as definições do spiner de seleção das categorias

        //Definindo o adaptador
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            listaDeNomesDeCategorias
        )

        //Inflando o adapter
        menuCategorias.adapter = adapter


        //Definindo o listener para cada elemento selecionado
        //Esse listener vai funcionar assim que o fragment é criado, perfeito para filtrar as views
        menuCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            //p0 é o parent, p2 é o integer que retorna a posição na lista
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                //está funcionando. É o melhor jeito? Não sei
                recarregadorRecyclerView(p2)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "nada selecionado", Toast.LENGTH_SHORT).show()
            }

        }

        super.onStart()
        atualizarRVGastos(menuCategorias.selectedItemId.toInt())
    }

    private fun recarregadorRecyclerView(indiceDoSpinner: Int) {
        val categoriaDAO = CategoriaDAO(requireContext())
        val listaDeCategorias = categoriaDAO.listar()
        if (indiceDoSpinner != 0) {
            atualizarRVGastos(listaDeCategorias[indiceDoSpinner - 1].idCategoria)

            //Agora precisamos setar o progresso da categoria no nosso gráfico (progressBar)
            val valorTotalDaCategoriaSelecionada = listaDeCategorias[indiceDoSpinner - 1].valor
            val valorGastoDaCategoriaSelecionada =
                categoriaDAO.somarCategoria(listaDeCategorias[indiceDoSpinner - 1].idCategoria)
            val progressoCategoria =
                (valorGastoDaCategoriaSelecionada / valorTotalDaCategoriaSelecionada) * 100
            progressBar.progress = progressoCategoria.roundToInt()
            porcentagemGastos.text = "${progressoCategoria.roundToInt()}%"
            valorTotalOrcamento.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(valorTotalDaCategoriaSelecionada)
            valorTotalGasto.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(valorGastoDaCategoriaSelecionada)

        } else {
            atualizarRVGastos()
            val valorGastoTotal = categoriaDAO.somarTodosGastos()
            val valorTotalDosOrcamentos = categoriaDAO.somarTodosOrcamentos()
            Log.i("info_db", "valor total gasto: $valorGastoTotal")
            Log.i("info_db", "valor total orcamentos: $valorTotalDosOrcamentos")
            val progressoTotal = (valorGastoTotal / valorTotalDosOrcamentos) * 100
            progressBar.progress = progressoTotal.roundToInt()
            porcentagemGastos.text = "${progressoTotal.roundToInt()}%"
            valorTotalOrcamento.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(valorTotalDosOrcamentos)
            valorTotalGasto.text =
                NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorGastoTotal)

        }
    }

    private fun atualizarRVGastos(idFiltragem: Int = 0) {
        val gastoDAO = GastoDAO(requireContext())

        //Vamos filtrar se vamos selecionar todos os gastos ou um específico. Isso vai depender
        // se for passado um parâmetro ou não na hora de chamar a função atualizarRVGastos
        //Se não for passado parâmetro nenhum ou se for 0, significa que todos estão selecionados. Por que os ID
        //no banco de dados começam obrigatóriamente por 1.
        listaDeGastos = if (idFiltragem != 0) {
            gastoDAO.listarFilrado(idFiltragem)
        } else {
            gastoDAO.listar()
        }


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
                val numSpinner = menuCategorias.selectedItemId

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


                        gastosAdapter?.notifyItemRemoved(viewHolder.adapterPosition)

                        /*                        gastosAdapter?.recarregarListaPorDelecao(
                                                    gastoDAO.listar(),
                                                    viewHolder.adapterPosition
                                                )*/


                        Log.i("info_db", menuCategorias.selectedItemId.toString())
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao tentar deletar gasto!",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    gastosAdapter?.recarregarListaDeGastos(gastoDAO.listar())
                }

                recarregadorRecyclerView(numSpinner.toInt())

            }
        })

}