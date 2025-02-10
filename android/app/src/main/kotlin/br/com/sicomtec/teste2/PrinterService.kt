package com.sicomtec.br.testar_l400

import android.app.Activity
import br.com.positivo.printermodule.PrintAttributes.KEY_ALIGN
import br.com.positivo.printermodule.PrintAttributes.KEY_MARGINLEFT
import br.com.positivo.printermodule.PrintAttributes.KEY_TEXTSIZE
import br.com.positivo.printermodule.PrintAttributes.KEY_TYPEFACE
import br.com.positivo.printermodule.PrintAttributes.KEY_WEIGHT
import br.com.positivo.printermodule.PrintAttributes.VALUE_ALIGN_CENTER
import br.com.positivo.printermodule.PrintAttributes.VALUE_ALIGN_LEFT
import br.com.positivo.printermodule.PrintAttributes.VALUE_ALIGN_RIGHT
import br.com.positivo.printermodule.PrintAttributes.VALUE_TYPEFACE_BOLD
import rede.smartrede.commons.contract.IConnectorPrinter
import rede.smartrede.sdk.api.IRedeSdk


class PrinterService(private val activity: Activity, redeSdk: IRedeSdk) : IPrinter {

    private val connectorPrinter : IConnectorPrinter
//    private val listContent : MutableList<Pair<Array<String>, List<Map<String, Int>>>> = ArrayList()

    init {
        val terminalFunctions = redeSdk.getTerminalFunctions()
        connectorPrinter = terminalFunctions.getConnectorPrinter()
    }

    override fun impressoraOutput(
        saltarNLinha: Int,
        listContent: MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
    ) {
        postToWorkerThread {
            try {
                with(connectorPrinter) {

                    setPrinterCallback(PrinterCallBack(activity))

                    printArrayTextWithAttributes(listContent)

                    printBlankLines(saltarNLinha)

                }
            } catch (exception: Exception) {
                println(exception.message.orEmpty())
            }
        }
    }

    override fun impressoraOutputCFe(
        contentQrCode: String,
        contentBarCode: String,
        listContent0: MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
        listContent1: MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
    ) {
        postToWorkerThread {
            try {
                with(connectorPrinter) {

                    setPrinterCallback(PrinterCallBack(activity))

                    printArrayTextWithAttributes(listContent0)

                    printBarCode(contentBarCode, VALUE_ALIGN_CENTER, 380, 40, false)
                    printQRCode(contentQrCode, VALUE_ALIGN_CENTER, 320)

                    printArrayTextWithAttributes(listContent1)

                    printBlankLines(4)

                }

            } catch (exception: Exception) {
                println(exception.message.orEmpty())
            }
        }
    }

    override fun impressoraOutputCFeCanc(
        contentQrCode0: String,
        contentQrCode1: String,
        contentBarCode0: String,
        contentBarCode1: String,
        listContent0: MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
        listContent1: MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
    ) {
        postToWorkerThread {
            try {
                with(connectorPrinter) {

                    setPrinterCallback(PrinterCallBack(activity))

                    printArrayTextWithAttributes(listContent0)

                    printBarCode(contentBarCode0, VALUE_ALIGN_CENTER, 380, 40, false)
                    printQRCode(contentQrCode0, VALUE_ALIGN_CENTER, 320)

                    printArrayTextWithAttributes(listContent1)

                    printBarCode(contentBarCode1, VALUE_ALIGN_CENTER, 380, 40, false)
                    printQRCode(contentQrCode1, VALUE_ALIGN_CENTER, 320)

                    printBlankLines(4)

                }

            } catch (exception: Exception) {
                println(exception.message.orEmpty())
            }
        }
    }

    override fun avancaLinha(nlinhas: Int) {

        postToWorkerThread {
            try {
                with(connectorPrinter) {
                    setPrinterCallback(PrinterCallBack(activity))
                    printBlankLines(nlinhas)
                }
            } catch (exception: Exception) {
                println(exception.message.orEmpty())
            }
        }
    }

    override fun postToWorkerThread(command: Runnable) {
        Thread(command).start()
    }

    override fun bufferLinhaAtrib(
        alinhamento: Alinhamento,
        buffer: String,
        tamanhoFonte: Int
    ): Pair<Array<String>, List<Map<String, Int>>> {
        var bufferaux: Array<String> = arrayOf(buffer)
        val attrCols: ArrayList<MutableMap<String, Int>>
        val mapTexto: MutableMap<String, Int>

        when(alinhamento){
            Alinhamento.CENTRO -> {
                mapTexto = mutableMapOf(
                    Pair(KEY_TEXTSIZE, tamanhoFonte),
                    Pair(KEY_ALIGN, VALUE_ALIGN_CENTER),
                    Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD)
                )
            }
            Alinhamento.DIREITA -> {
                mapTexto = mutableMapOf(
                    Pair(KEY_TEXTSIZE, tamanhoFonte),
                    Pair(KEY_ALIGN, VALUE_ALIGN_RIGHT),
                    Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD)
                )
            }

            Alinhamento.ESQUERDA -> {
                mapTexto = mutableMapOf(
                    Pair(KEY_TEXTSIZE, tamanhoFonte),
                    Pair(KEY_ALIGN, VALUE_ALIGN_LEFT),
                    Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD)
                )
            }
        }

        attrCols = getArrayList().also {
            it.add(mapTexto)
        }
        return Pair(bufferaux, attrCols)
    }

    override fun bufferLinhaAtrib(
        buffer0: String,
        buffer1: String,
        tamanhoFonte: Int
    ): Pair<Array<String>, List<Map<String, Int>>> {
        var bufferaux: Array<String> = arrayOf(buffer0, buffer1)

        val mapTextoCol1 = mutableMapOf(
            Pair(KEY_TEXTSIZE, tamanhoFonte),
            Pair(KEY_ALIGN, VALUE_ALIGN_LEFT),
            Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD),
            Pair(KEY_MARGINLEFT, 0),
            Pair(KEY_WEIGHT, 2)
        )

        val mapTextoCol2 = mutableMapOf(
            Pair(KEY_TEXTSIZE, tamanhoFonte),
            Pair(KEY_ALIGN, VALUE_ALIGN_RIGHT),
            Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD),
            Pair(KEY_MARGINLEFT, 0),
            Pair(KEY_WEIGHT, 1)
        )

        val attrCols = getArrayList().also {
            it.add(mapTextoCol1)
            it.add(mapTextoCol2)
        }

        return Pair(bufferaux, attrCols)
    }

    override fun bufferLinhaAtrib(
        buffer0: String,
        buffer1: String,
        buffer2: String,
        tamanhoFonte: Int
    ): Pair<Array<String>, List<Map<String, Int>>> {
        var bufferaux: Array<String> = arrayOf(buffer0, buffer1, buffer2)

        val mapTexto1 = mutableMapOf(
            Pair(KEY_TEXTSIZE, tamanhoFonte),
            Pair(KEY_ALIGN, VALUE_ALIGN_LEFT),
            Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD),
            Pair(KEY_MARGINLEFT, 4),
            Pair(KEY_WEIGHT, 3)
        )

        val mapTexto2 = mutableMapOf(
            Pair(KEY_TEXTSIZE, tamanhoFonte),
            Pair(KEY_ALIGN, VALUE_ALIGN_LEFT),
            Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD),
            Pair(KEY_MARGINLEFT, 4),
            Pair(KEY_WEIGHT, 2)
        )

        val mapTexto3 = mutableMapOf(
            Pair(KEY_TEXTSIZE, tamanhoFonte),
            Pair(KEY_ALIGN, VALUE_ALIGN_RIGHT),
            Pair(KEY_TYPEFACE, VALUE_TYPEFACE_BOLD),
            Pair(KEY_MARGINLEFT, 4),
            Pair(KEY_WEIGHT, 1)
        )

        val attrCols = getArrayList().also {
            it.add(mapTexto1)
            it.add(mapTexto2)
            it.add(mapTexto3)
        }

        return Pair(bufferaux, attrCols)
    }

    private fun getArrayList() = ArrayList<MutableMap<String, Int>>()
}