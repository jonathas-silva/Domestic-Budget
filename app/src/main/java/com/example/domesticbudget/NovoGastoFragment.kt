package com.example.domesticbudget

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.database.GastoDAO
import com.example.domesticbudget.model.Gasto
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NovoGastoFragment : Fragment() {

    private lateinit var inputCategoria: AutoCompleteTextView
    private lateinit var btnSalvar: Button
    private lateinit var inputValor: TextInputEditText
    private lateinit var inputDescricao: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //vamos recuperar dinamicamente as categorias cadastradas e inserir no dropdown
        //IMPORTANTE NOTAR QUE ESSA ATUALIZAÇÃO SERÁ FEITA SEMPRE QUE A VIEW FOR CRIADA!!
        //Refletir sobre a necessidade (ou não) de colocar esse método dentro de outro ciclo de vida
        val categoriaDAO = CategoriaDAO(requireContext())
        val listaDeCategorias = categoriaDAO.listar()
        val listaNomesCategorias = ArrayList<String>()
        val listaDeIndices = ArrayList<Int>()

        listaDeCategorias.forEach { categoria ->

            listaNomesCategorias.add(categoria.nome)
            listaDeIndices.add(categoria.idCategoria)

        }
        Log.i("info_db", listaNomesCategorias.toString())

        //Vamos povoar o dropdown menu de categorias com as categorias
        //Num primeiro momento estaticamente, e depois de forma dinâmica acessando o banco de dados
        //val items = listOf("Alimentação", "Casa", "Pets", "Rolê")


        val view = inflater.inflate(R.layout.fragment_novo_gasto, container, false)


        inputCategoria = view.findViewById(R.id.inputEditarCategoria)
        btnSalvar = view.findViewById(R.id.btnSalvar)
        inputValor = view.findViewById(R.id.inputValor)
        inputDescricao = view.findViewById(R.id.inputDescricao)


        btnSalvar.setOnClickListener {

            //a variável num armazena índice que a categoria selecionada está
            //que é o mesmo indice que o Id do BD daquela categoria está armazenado
            //logo, com essa informação sabemos qual id representa aquela categoria no BD
            val num = listaNomesCategorias.indexOf(inputCategoria.text.toString())
            var indiceCategoria = -1

            //definindo a data atual
            val hoje = Calendar.getInstance()
            val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dataFormatada = formatador.format(hoje.time)


            if (num != -1) {
                indiceCategoria = listaDeIndices[num]
                //Vamos montar o objeto e adicioná-lo
                val novoGasto = Gasto(
                    -1,
                    inputValor.text.toString().toDouble(),
                    inputDescricao.text.toString(),
                    indiceCategoria,
                    dataFormatada
                )
                val gastoDAO = GastoDAO(requireContext())
                if (gastoDAO.salvar(novoGasto)){
                    Toast.makeText(requireContext(), "Gasto inserido com sucesso!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Ocorreu um erro na inserção de gasto!", Toast.LENGTH_SHORT).show()
                }

            }

            Log.i("info_db", indiceCategoria.toString())
        }

        /*Importante deixar registrado que estava enfrentando um erro para setar esse adapter
        * devido a não estar conseguindo indicar o context no fragment. O mais complexo é que o
        * Android Studio (vulgo IntelliJ) estava indicando que o erro estava no item de layout, não no
        * context*/
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item, listaNomesCategorias
        )
        inputCategoria.setAdapter(adapter)

        return view
    }

}
