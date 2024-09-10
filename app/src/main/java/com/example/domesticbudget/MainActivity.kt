package com.example.domesticbudget

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.domesticbudget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarToolbar()
        replaceFragment(NovoGastoFragment())
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Manipulação do bottom menu
        binding.bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.novo_gasto -> {
                    replaceFragment(NovoGastoFragment())
                    binding.includeToolbar.tbPrincipal.title = "Novo gasto"
                }

                R.id.categorias -> {
                    replaceFragment(CategoriasFragment())
                    binding.includeToolbar.tbPrincipal.title = "Categorias"
                }

                R.id.gastos -> {
                    replaceFragment(GastosFragment())
                    binding.includeToolbar.tbPrincipal.title = "Gastos"
                }

                else -> {}
            }
            true
        }


    }

    private fun inicializarToolbar() {
        binding.includeToolbar.tbPrincipal.title = "Novo gasto"
        binding.includeToolbar.tbPrincipal.overflowIcon
        binding.includeToolbar.tbPrincipal.isTitleCentered = true
        binding.includeToolbar.tbPrincipal.setTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.md_theme_onPrimary
            )
        )

        setSupportActionBar(binding.includeToolbar.tbPrincipal)

        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.top_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    TODO("Not yet implemented")
                }

            }
        )



    }

    fun replaceFragment(fragment: Fragment) {


        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framePrincipal, fragment)
        fragmentTransaction.commit()
    }
}