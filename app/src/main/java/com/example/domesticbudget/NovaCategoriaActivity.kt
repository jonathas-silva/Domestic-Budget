package com.example.domesticbudget

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domesticbudget.Utilidades.addCurrencyMask
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.databinding.ActivityNovaCategoriaBinding
import com.example.domesticbudget.model.Categoria
import java.text.NumberFormat
import java.util.Locale


/*acitivity para adição de nova categoria e edição de categorias existentes
talvez no futuro seja trocada, pois trata-se de uma página muito grande para uma
adição muito pequena. */
class NovaCategoriaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNovaCategoriaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Recuperando a categoria passada para edição
        val bundle = intent.extras

        //Vamos fazer  toda a verificação quanto a nulidade do bundle. Aqui verificaremos se é edição
        //ou criação
        if (bundle != null) { //Significa que essa activity foi criada pelo botão editar
            inicializarToolbar("Editar Categoria")
            var categoria: Categoria? = null

            //Agora vamos testar a versão do android do cabra

            //Se for maior que a versão 33, vamos usar a função criada a partir do 33
            categoria = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable("categoria", Categoria::class.java)

            } else { //Se for menor, vamos usar a versão depreciada
                bundle.getSerializable("categoria") as Categoria
            }

            if (categoria != null) {
                //ATUALIZANDO CATEGORIA
                binding.inputNome.setText(categoria.nome)
                binding.inputOrcamento.setText(
                    NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                        .format(categoria.valor)
                    //categoria.valor.toString()
                )
                binding.inputDataTermino.setText(categoria.periodo)

                binding.btnSalvar.text = "Atualizar"

                binding.btnSalvar.setOnClickListener {
                    val categoriaAtualizada = Categoria(
                        categoria.idCategoria,
                        binding.inputNome.text.toString(),
                        Utilidades.limpadorDeFormatacao(binding.inputOrcamento.text.toString()),
                        binding.inputDataTermino.text.toString()
                    )
                    val categoriaDAO = CategoriaDAO(this)

                    if (categoriaDAO.atualizar(categoriaAtualizada)) {
                        Toast.makeText(this, "Categoria atualizada!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Erro ao atualizar categoria!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            }

        } else {
            //CRIANDO NOVA CATEGORIA
            inicializarToolbar("Nova Categoria")
            binding.btnSalvar.setOnClickListener {
                val novaCategoria = Categoria(
                    -1,
                    binding.inputNome.text.toString(),
                    Utilidades.limpadorDeFormatacao(binding.inputOrcamento.text.toString()),
                    binding.inputDataTermino.text.toString()
                )

                //vamos tentar salvar
                val categoriaDAO = CategoriaDAO(this)
                if (categoriaDAO.salvar(novaCategoria)) {
                    Toast.makeText(this, "Categoria salva com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao salvar categoria!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        /*        binding.inputDataTermino.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        mostrarDataPicker()
                    }
                }*/

        //Adicionando a formatação para BRL. Atento que para recuperar o valor é preciso limpá-lo
        //ainda bem que já tenho uma função para isso
        binding.inputOrcamento.addCurrencyMask()

        binding.containerDataTermino.setStartIconOnClickListener {
            Utilidades.mostrarDataPicker(binding.inputDataTermino, this)
        }


    }

    private fun inicializarToolbar(titulo: String) {

        binding.novaCategoriaToolbar.tbPrincipal.title = titulo
        binding.novaCategoriaToolbar.tbPrincipal.isTitleCentered = true
        binding.novaCategoriaToolbar.tbPrincipal.setTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.md_theme_onPrimary
            )
        )
        setSupportActionBar(binding.novaCategoriaToolbar.tbPrincipal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}

