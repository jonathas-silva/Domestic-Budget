package com.example.domesticbudget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.database.GastoDAO
import com.example.domesticbudget.model.Gasto
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
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

        inputValor.addCurrencyMask()


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
                if (gastoDAO.salvar(novoGasto)) {
                    Toast.makeText(
                        requireContext(),
                        "Gasto inserido com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro na inserção de gasto!",
                        Toast.LENGTH_SHORT
                    ).show()
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

private fun TextInputEditText.addCurrencyMask() {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        var iteracoes = 0
        var current = ""
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString() != current) {

                var valorReal = 0.00;

                var valorLimpo = s.toString().replace("[R$,.\\s]".toRegex(), "")

//                Log.i("info_textWatcher", "valor limpo: $valorLimpo")

                var counter = 0
                //vamos descobrir quantos valores 0 tem antes do número
                for (i in valorLimpo.indices) {

                    if (valorLimpo[i] == '0') {
                        counter++
                    } else {
                        break
                    }
                }
//                Log.i("info_textWatcher", "counter: $counter")

                val novaString = valorLimpo.trimStart('0')

//                Log.i("info_textWatcher", "novaString: $novaString")
//                Log.i("info_textWatcher", "comprimeiro nova string: ${novaString.length}")
                if (novaString.isNotEmpty()) {

                    val valorPassadoDouble: Double = novaString.toDouble()
//                    Log.i("info_textWatcher", "valorPassadoDouble: $valorPassadoDouble")

                    valorReal = valorPassadoDouble / 100
                }

                //formatando para real brasileiro
                val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                val valorRealFormatado = formatador.format(valorReal)
//            Log.i("info_textWatcher", "valorFormatadoReal: $valorRealFormatado")


                current = valorRealFormatado
                this@addCurrencyMask.setText(valorRealFormatado)

                this@addCurrencyMask.setSelection(valorRealFormatado.length)

//               this@addCurrencyMask.addTextChangedListener(this)

                iteracoes++
                Log.i("info_textWatcher", "iterações = $iteracoes")
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    })
}
