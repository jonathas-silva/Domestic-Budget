package com.example.domesticbudget

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.databinding.ActivityNovaCategoriaBinding
import com.example.domesticbudget.model.Categoria
import java.text.SimpleDateFormat
import java.util.Calendar
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
                binding.inputNome.setText(categoria.nome)
                binding.inputOrcamento.setText(categoria.valor.toString())
                binding.inputDataTermino.setText(categoria.periodo)

                binding.btnSalvar.text = "Atualizar"

                binding.btnSalvar.setOnClickListener {
                    val categoriaAtualizada = Categoria(
                        categoria.idCategoria,
                        binding.inputNome.text.toString(),
                        binding.inputOrcamento.text.toString().toDouble(),
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
            inicializarToolbar("Nova Categoria")
            binding.btnSalvar.setOnClickListener {
                val novaCategoria = Categoria(
                    -1,
                    binding.inputNome.text.toString(),
                    binding.inputOrcamento.text.toString().toDouble(),
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

        binding.containerDataTermino.setStartIconOnClickListener {
            mostrarDataPicker()
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

    private fun mostrarDataPicker() {

        val c = Calendar.getInstance()


        //esses valores serão os recuperados como padrão no momento que o picker abrir
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Dialog para mostrar o data picker.
        //Guardar modelo, muitíssimo útil
        val datePickerDialog = DatePickerDialog(
            this,
            { _, y, monthOfYear, dayOfMonth ->

                //recuperando a data recebida
                val dataRecebida = Calendar.getInstance()
                dataRecebida.set(y, monthOfYear, dayOfMonth)

                Log.i("info_datas", dataRecebida.timeInMillis.toString())

                //formatando a data recebida
                val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataFormatada = formatador.format(dataRecebida.time)

                binding.inputDataTermino.setText(dataFormatada)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}

