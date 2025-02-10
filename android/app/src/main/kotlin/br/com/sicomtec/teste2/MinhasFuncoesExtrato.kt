package com.sicomtec.br.testar_l400

import android.annotation.SuppressLint

object MinhasFuncoesExtrato {

    fun retornaDescricaoPagtoSat(cMP: String): String {
        return when (cMP) {
            "01" -> "Dinheiro"
            "02" -> "Cheque"
            "03" -> "Cartão de Crédito"
            "04" -> "Cartão de Débito"
            "05" -> "Crédito Loja"
            "10" -> "Vale Alimentação"
            "11" -> "Vale Refeição"
            "12" -> "Vale Presente"
            "13" -> "Vale Combustível"
            "15" -> "Boleto Bancário"
            "16" -> "Depósito Bancário"
            "17" -> "Pagamento Instantâneo(PIX)"
            "18" -> "Transferência bancária, Carteira Digital"
            "19" -> "Programa de fidelidade, Cashback, Crédito Virtual"
            "90" -> "Sem pagamento"
            "99" -> "Outros"
            else -> try {
                throw Exception("Meio de pagamento invalido($cMP) ....")
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    fun mask(valor: String, mask: String): String {
        var maskared = ""
        var k: Short = 0
        try {
            if (valor.isEmpty() || mask.isEmpty()) {
                throw Exception("Mascara invalida.")
            }

            for (i in 0 until mask.length) {
                if (mask.substring(i, i + 1) == "#") {
                    if (valor.substring(k.toInt(), k + 1).isNotBlank() || valor.substring(k.toInt(), k + 1)
                            .isNotEmpty()
                    ) {
                        maskared += valor.substring(k.toInt(), k + 1)
                        k++
                    }
                } else {
                    if (maskared.length < mask.length) {
                        if (mask.substring(i, i + 1).isNotBlank() || mask.substring(i, i + 1)
                                .isNotEmpty()
                        ) maskared += mask.substring(i, i + 1)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }

        return maskared
    }

    fun montaLinha(buffer: String, ncol: Short): MutableCollection<String?> {
        val bufferaux1 = buffer.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        var bufferaux2 = ""

        var bufferfinal: MutableCollection<String?> = ArrayList()

        try {
            if (buffer.isNullOrEmpty() || buffer.isNullOrBlank()) {
                throw Exception("Buffer não pode ser nulo.")
            }

            if (ncol.toInt() == 0) {
                throw Exception("ncol não pode ser zero.")
            }

            for (i in bufferaux1.indices) {
                if (bufferaux2.isEmpty()) {
                    bufferaux2 = bufferaux1[i]
                } else if ((bufferaux2 + " " + bufferaux1[i]).length <= ncol) {
                    bufferaux2 = bufferaux2 + " " + bufferaux1[i]
                } else {
                    bufferfinal.add(bufferaux2)
                    bufferaux2 = bufferaux1[i]
                }
            }
            if (bufferaux2.isNotEmpty()) {
                bufferfinal.add(bufferaux2)
            }
        } catch (e: Exception) {
            throw e
        }
        return bufferfinal
    }

    fun montaLinha(buffer: String, buffervr: String, ncol: Short): MutableCollection<String> {
        val bufferaux1 = buffer.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        var bufferaux2 = ""

        val bufferfinal = ArrayList<String>()
        try {
            for (i in bufferaux1.indices) {
                if (bufferaux2.isEmpty()) {
                    bufferaux2 = bufferaux1[i]
                } else if ((bufferaux2 + 160.toChar() + bufferaux1[i]).length <= ncol) {
                    bufferaux2 = "$bufferaux2 " + bufferaux1[i]
                } else {
                    bufferfinal.add(bufferaux2)
                    bufferaux2 = bufferaux1[i]
                }
            }

            if (bufferaux2.isNotEmpty()) {
                if ("$bufferaux2 $buffervr".length <= ncol) {
                    var espacos = ""
                    for (i in 0..(ncol - (bufferaux2.length + buffervr.length))) {
                        espacos += String.format("%1\$c", 160.toChar())
                        if ((bufferaux2 + espacos + buffervr).length == ncol.toInt()) {
                            break
                        }
                    }
                    bufferfinal.add(bufferaux2 + espacos + buffervr)
                } else {
                    bufferfinal.add(bufferaux2)
                    val nEspacos = (ncol + 4 - buffervr.length).toString()
                    bufferfinal.add(
                        String.format(
                            "%$nEspacos" + "." + nEspacos + "s",
                            buffervr
                        )
                    ) //&nbsp
                }
            }
        } catch (e: Exception) {
            throw e
        }

        return bufferfinal
    }

    @SuppressLint("DefaultLocale")
    fun formataQtde(vr: Double): String {
        try {
            if (vr > 2147483647.0) {
                throw Exception("Valor invalido.")
            }

            if (vr.toString().contains(".")) {
                val _v = vr.toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
                if (_v.replace("0", "").isEmpty()) {
                    return String.format("%2$4.0f", "qtde.", vr)
                }
                return String.format("%2$4." + _v.length.toString() + "f", "qtde.", vr)
            }
        } catch (e: Exception) {
            throw e
        }
        return String.format("%2$4.0d", "qtde.", vr)
    }

    @SuppressLint("DefaultLocale")
    fun formataVr(vr: Double): String {
        try {
            if (vr > 2147483647.0) {
                throw Exception("Valor invalido.")
            }

            if (vr.toString().contains(".")) {
                return String.format("%2$,.2f", "valor.", vr)
            }
        } catch (e: Exception) {
            throw e
        }
        return String.format("%2$,.2f", "valor.", vr)
    }

    fun concatenaString(strings: MutableCollection<String>) : String {
      var buffer: String = ""
      for (string in strings){
          if(string.isNotEmpty()) {
              buffer += "$string "
          }
      }
      return buffer.trimEnd()
    }

    fun centralizaTexto(texto: String, ncol: Short) : String {
        if(texto.length == 38) return texto

        var tamTexto = texto.length
        var nespacos = ((ncol - tamTexto) / 2)
        var espacos = ""
        for (i in 0 until nespacos){
            espacos += 160.toChar()
        }
        return "${espacos}${texto}"
    }
}