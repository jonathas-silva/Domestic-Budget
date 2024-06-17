package com.example.domesticbudget

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domesticbudget.databinding.ActivityNovaCategoriaBinding


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

        inicializarToolbar()

    }

    private fun inicializarToolbar() {

        binding.novaCategoriaToolbar.tbPrincipal.title = "Nova Categoria"
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