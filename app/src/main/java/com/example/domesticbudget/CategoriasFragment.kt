package com.example.domesticbudget

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.model.Categoria


//R.layout.fragment_categorias
class CategoriasFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var rvCategorias: RecyclerView
    private lateinit var btnNovaCategoria: Button
    private lateinit var btnListar: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val thisContext = container?.context
        val view = inflater.inflate(
            R.layout.fragment_categorias,
            container,
            false
        )

        //LISTA APENAS PARA TESTE DO ADAPTER.
        //PARA PROD SERÁ UTILIZADO O BANCO DE DADOS, QUE SERÁ IMPLEMENTADO POSTERIORMENTE
        val lista = listOf<Categoria>(
            Categoria(1, "Alimentação", "R$ 500,00"),
            Categoria(2, "Rolê", "R$ 350,00"),
            Categoria(3, "Farmácia", "R$ 150,00"),
            Categoria(4, "Pets", "R$ 275,00"),
            Categoria(5, "Casa", "R$ 200,00")
        )


        rvCategorias = view.findViewById(R.id.recyclerCategorias)
        btnNovaCategoria = view.findViewById(R.id.btnNovaCategoria)
        btnListar = view.findViewById(R.id.btnListar)

        rvCategorias.adapter = CategoriasAdapter(lista)
        rvCategorias.layoutManager = LinearLayoutManager(activity)

        btnNovaCategoria.setOnClickListener {
            val novaCategoria = Categoria(
                -1, "Nova tarefa inserida no BD", "500"
            )
            val categoraDAO = CategoriaDAO(mContext)
            if (categoraDAO.salvar(novaCategoria)) {
                Toast.makeText(mContext, "deu tudo certo na inserção", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mContext, "deu erro na inserção", Toast.LENGTH_SHORT).show()
            }
        }

        btnListar.setOnClickListener {
            var listaCategorias = emptyList<Categoria>()

            val categoriaDAO = CategoriaDAO(mContext)
            listaCategorias = categoriaDAO.listar()

            Log.i("info_db", listaCategorias.toString())
        }

        return view

    }


}
