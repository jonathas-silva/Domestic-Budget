package com.example.domesticbudget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.model.Categoria
import com.google.android.material.snackbar.Snackbar


//R.layout.fragment_categorias
class CategoriasFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var rvCategorias: RecyclerView
    private lateinit var btnNovaCategoria: Button

    /*Estamos configurando o adapter aqui em cima, pra
    * não precisar passar a lista de categorias como parâmetro na construção do adapter.
    * Isso faria com que fosse necessário recosntruir o RV para qualquer atualização da lista,
    * o que consumiria muitos recursos computacionais. Ao invés disso vamos apenas atualizar o adapter.*/
    private var categoriaAdapter: CategoriasAdapter? = null

    private var listaCategorias: List<Categoria> = emptyList()

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

        rvCategorias = view.findViewById(R.id.recyclerCategorias)
        btnNovaCategoria = view.findViewById(R.id.btnNovaCategoria)


        //Inflação do RecyclerView
        categoriaAdapter =
            CategoriasAdapter({ categoria -> editar(categoria) }, { id -> recuperarSoma(id) })
        rvCategorias.adapter = categoriaAdapter
        rvCategorias.layoutManager = LinearLayoutManager(activity)
        itemTouchHelper.attachToRecyclerView(rvCategorias)

        //Este botão apenas abre activity de nova categoria, sem passar nada.
        btnNovaCategoria.setOnClickListener {

            val intent = Intent(context, NovaCategoriaActivity::class.java)

            startActivity(intent)

        }


        return view

    }

    private fun editar(categoria: Categoria) {
        //1 - Abrir uma intent nova
        val intent = Intent(requireContext(), NovaCategoriaActivity::class.java)

        intent.putExtra("categoria", categoria)

        startActivity(intent)

    }

    private fun recuperarSoma(id: Int): Double {

        val categoriaDAO = CategoriaDAO(requireContext())
        return categoriaDAO.somarCategoria(id)

    }

    /*  Sempre que for necessário atualizar o RV de categorias, chamamos essa função.
        Ela busca a lista de categorias mais atual do banco de dados, instancia um adapter,
        passa essa lista atualizada para o RV e notifica o RV que os dados mudaram, para que as
        alterações mais recentes sejam recarregadas*/
    private fun atualizarRVCategorias() {
        val categoriaDAO = CategoriaDAO(mContext)
        listaCategorias = categoriaDAO.listar()
        categoriaAdapter?.recarregarLista(listaCategorias)
    }

    //A lista será atualizada sempre no ciclo de vida onStart, ou seja, sempre que retornarmos à tela
    override fun onStart() {
        super.onStart()
        atualizarRVCategorias()
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

                val categoriaDAO = CategoriaDAO(requireContext())

                /*Estamos usando non-null assertion aqui por que temos certeza de que a variável
                * gastosAdapter sempre será instanciada na criação da view, e nesse ponto da execução
                * nunca será nula.
                * Podemos posteriormente mudar a forma como essa variável é instanciada.*/
                if (categoriaDAO.remover(categoriaAdapter?.recuperarId(viewHolder.adapterPosition)!!)) {
                    view?.let {
                        Snackbar.make(
                            it.findViewById(R.id.recyclerCategorias),
                            "Item deletado!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    categoriaAdapter?.recarregarListaPorDelecao(
                        categoriaDAO.listar(), //recupera novamente do banco dedos, pq a deleção deu certo
                        viewHolder.adapterPosition //viewHolder.adapterPosition reflete a posição do item no RV
                    )


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao tentar deletar categoria!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    categoriaAdapter?.recarregarLista(
                        categoriaDAO.listar()
                    )
                }


            }
        })


}
