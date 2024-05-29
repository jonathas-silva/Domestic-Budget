package com.example.domesticbudget

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domesticbudget.databinding.ActivityGastosBinding
import com.example.domesticbudget.databinding.ActivityMainBinding

class GastosActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGastosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicializarToolbar()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    private fun inicializarToolbar() {
        binding.includeToolbarGastos.tbPrincipal.title = "Gastos"
        binding.includeToolbarGastos.tbPrincipal.isTitleCentered = true
        binding.includeToolbarGastos.tbPrincipal.setTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.md_theme_onPrimary
            )
        )
        setSupportActionBar(binding.includeToolbarGastos.tbPrincipal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}