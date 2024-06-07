package com.example.domesticbudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class NovoGastoFragment : Fragment() {

    private lateinit var inputCategoria: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        //Vamos povoar o dropdown menu de categorias com as categorias
        //Num primeiro momento estaticamente, e depois de forma dinâmica acessando o banco de dados
        val items = listOf("Alimentação", "Casa", "Pets", "Rolê")


        val view = inflater.inflate(R.layout.fragment_novo_gasto, container, false)


        inputCategoria = view.findViewById(R.id.inputCategoria)


        /*Importante deixar registrado que estava enfrentando um erro para setar esse adapter
        * devido a não estar conseguindo indicar o context no fragment. O mais complexo é que o
        * Android Studio (vulgo IntelliJ) estava indicando que o erro estava no item de layout, não no
        * context*/
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item, items
        )
        inputCategoria.setAdapter(adapter)

        return view
    }

}
