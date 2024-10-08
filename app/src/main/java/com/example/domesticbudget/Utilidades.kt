package com.example.domesticbudget
import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Classe utilitária com métodos e funções de extensão para operações repetitivas.
 * @author Jonathas Silva
 */
object Utilidades {

    /**
     * Formata o valor [Double] como moeda no formato brasileiro (R$).
     *
     * @return String com o valor formatado em reais.
     */
    fun Double.format(): String {
        return String.format("%.2f", this).replace('.', ',')
    }


    /**
     * Transforma uma string no formato moeda BRL no valor [Double] que ele representa.
     * @param textoBruto String com o valor formatado em reais.
     * @return [Double] que representa o valor de entrada
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


    /**
     * Adiciona listeners de mudança de texto ao objeto da classe [TextInputEditText] aplicando máscaras de formação. Deve ser usada quando se quer aplicar uma máscara que atualiza o formato de entrada automaticamente conforme o usuário digita.
     * */

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

    /**
     * Função de extensão da classe [TextInputEditText] para exibição do picker de data ao clicar na caixa de inserção de texto.
     * */
    fun mostrarDataPicker(calendarInput: TextInputEditText, context: Context) {

        val c = Calendar.getInstance()


        //esses valores serão os recuperados como padrão no momento que o picker abrir
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Dialog para mostrar o data picker.
        //Guardar modelo, muitíssimo útil
        val datePickerDialog = DatePickerDialog(
            context,
            { _, y, monthOfYear, dayOfMonth ->

                //recuperando a data recebida
                val dataRecebida = Calendar.getInstance()
                dataRecebida.set(y, monthOfYear, dayOfMonth)

                Log.i("info_datas", dataRecebida.timeInMillis.toString())

                //formatando a data recebida
                val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataFormatada = formatador.format(dataRecebida.time)

                calendarInput.setText(dataFormatada)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

}