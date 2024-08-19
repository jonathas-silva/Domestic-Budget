package com.example.domesticbudget

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

object Utilidades {

    /*Essa função pega uma string bruta, no padrão de formatação de moeda brasileira
    e devolve o valor double que representa, para que sejam realizados os cálculos adequados
    */
    fun limpadorDeFormatacao(textoBruto: String): Double {
        var valorReal = 0.00;
        val valorLimpo = textoBruto.toString()
            .replace("[R$,.\\s]".toRegex(), "")
            .trimStart('0')

        if (valorLimpo.isNotEmpty()) {
            valorReal = (valorLimpo.toDouble()) / 100
        }

        return valorReal
    }


    /*Função de Extensão da Classe TextInputEditText
    * Ela adiciona listeners de mudança de texto aplicando máscara de formatação no padrão BRL*/
    fun TextInputEditText.addCurrencyMask() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            var current = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.toString() != current) {

                    val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                    val valorRealFormatado =
                        formatador.format(limpadorDeFormatacao(s.toString()))

                    current = valorRealFormatado
                    this@addCurrencyMask.setText(valorRealFormatado)
                    this@addCurrencyMask.setSelection(valorRealFormatado.length)

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}