package com.example.domesticbudget

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.domesticbudget.database.CategoriaDAO
import com.example.domesticbudget.database.GastoDAO
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
                    when (menuItem.itemId) {
                        R.id.item_limpar -> {
                            limparTudo()
                        }

                        R.id.item_sobre -> {
                            Toast.makeText(applicationContext, "Sobre", Toast.LENGTH_SHORT).show()
                        }

                        else -> {}
                    }

                    return true
                }

            }
        )


    }

    private fun limparTudo() {

        val builder = AlertDialog.Builder(this)
        val categoriaDAO = CategoriaDAO(this)
        val gastoDAO = GastoDAO(this)

        builder.setTitle("Atenção!")
        builder.setMessage("Esta ação deletará todos os gastos e todas as categorias. Tem certeza de que deseja continuar?")

        builder.setPositiveButton("Sim") { _, _ ->

            if (gastoDAO.deletarTudo()) {
                if (categoriaDAO.deletarTudo()) {
                    binding.bottomMenu.selectedItemId = R.id.novo_gasto
                    Toast.makeText(this, "Entradas limpas com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao deletar as categorias!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Erro ao deletar os gastos!", Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }


        builder.show()
    }

    private fun replaceFragment(fragment: Fragment) {


        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framePrincipal, fragment)
        fragmentTransaction.commit()
    }
}