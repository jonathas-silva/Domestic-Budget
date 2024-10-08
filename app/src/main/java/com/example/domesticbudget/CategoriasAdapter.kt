package com.example.domesticbudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domesticbudget.Utilidades.format
import com.example.domesticbudget.model.Categoria
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CategoriasAdapter(
    val onClickEditar: (Categoria) -> Unit,
    val recuperarSoma: (Int) -> Double
) : Adapter<CategoriasAdapter.CategoriasViewHolder>() {


    private var listaCategorias: List<Categoria> = emptyList()
    fun recarregarLista(lista: List<Categoria>) {
        this.listaCategorias = lista
        notifyDataSetChanged()
    }

    fun recarregarListaPorDelecao(lista: List<Categoria>, position: Int) {
        notifyItemRemoved(position)
        this.listaCategorias = lista
    }

    inner class CategoriasViewHolder(
        private val itemView: View
    ) : ViewHolder(itemView) {

        val nomeCategoria: TextView = itemView.findViewById(R.id.txtNomeCategoria)

        //val valorCategoria: TextView = itemView.findViewById(R.id.valorCategoria)
        val periodo: TextView = itemView.findViewById(R.id.txtDiasRestantes)

        //val valorRestante: TextView = itemView.findViewById(R.id.txtValorRestante)
        val container: CardView = itemView.findViewById(R.id.containerItemCategorias)
        val valorRestante: TextView = itemView.findViewById(R.id.txtValorRestante)
        val valorTotal: TextView = itemView.findViewById(R.id.txtValorTotal)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {

        val layoutInflater = LayoutInflater.from(
            parent.context
        )

        val itemView = layoutInflater.inflate(
            R.layout.item_categorias,
            parent,
            false
        )

        //precisamos retornar a conversão do nosso layout como uma view
        //No nosso caso vamos utilizar o layoutInflater para fazer esse processo
        return CategoriasViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }

    //onBindViewHolder é chamado na vinculação dos dados para a visualização
    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {

        //Aqui estamos atribuindo a posição da lista, que inicialmente é uma string
        //para a variável nome. Em seguida vamos inserir esse valor na textView do itemCategoria que inflamos
        val categoria = listaCategorias[position]


        //função de extensão


        //Esse valor gasto está estático, mas será calculado dinamicamente posteriormente


        holder.nomeCategoria.text = categoria.nome
        //holder.valorCategoria.text = "R$ ${categoria.valor.format()}"

        val diasRestantes = calcularEntreDatas(categoria.periodo)
        holder.periodo.text = if (diasRestantes == "1") {
            "$diasRestantes dia"
        } else {
            "$diasRestantes dias"
        }


        //estamos recuperando dinâmicamente direto do fragment
        //até então, essa é a maneira mais adequada de fazer isso. [pesquisar]
        val soma = recuperarSoma(categoria.idCategoria)
        val restante = categoria.valor - soma

        holder.valorRestante.text = "restam R$ ${restante.format()}"
        holder.valorTotal.text = "R$ ${categoria.valor.format()}"

 /*       //Calculo da porcentagem restante
        val porcentagem = (100 - ((restante / categoria.valor) * 100).roundToInt())
        Log.i("dbg.calculos", "valor categoria: ${categoria.valor}")
        Log.i("dbg.calculos", "restante: $restante")
        Log.i("dbg.calculos", "Porcentagem: $porcentagem")*/

        //holder.porcentagemRestante.text = "$porcentagem %"

        holder.container.setOnClickListener {
            onClickEditar(categoria)
        }

    }


    fun recuperarId(position: Int): Int {
        val categoria = listaCategorias[position]
        return categoria.idCategoria
    }


    /*AVISO IMPORTANTE!!!!!!!!!!
    * Essa função calcula a diferença entre duas datas, e depende diretamente que as
    * entradas fornecidas sejam strings estritamente no formado dd/MM/YYYY.
    * É preciso se certificar que o banco de dados só aceite esse formato e que o usuário
    * consiga inserir apenas datas nesse formato!
    *
    * A primeira medida, mais óbvia, foi desativar o input de tempo, para que o usuário não consiga digitar,
    * mas apenas selecionar uma data, que será formatada pela função própria em NovaCategoriaActivity*/
    private fun calcularEntreDatas(data: String): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        //Dei preferencia para getInstance() ao invés de Date() para minimizar discrepâncias
        val currentDate = sdf.format(Calendar.getInstance().timeInMillis)
        //val currentDate = sdf.format(Date())

        //Agora vamos converter as duas datas que estão no mesmo formato e comparar
        val hoje: Date = sdf.parse(currentDate) as Date
        val prazo: Date = sdf.parse(data) as Date

        val diferenca = prazo.time - hoje.time

        return TimeUnit.MILLISECONDS.toDays(diferenca).toString()
    }

}