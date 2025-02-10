package com.sicomtec.br.testar_l400

import android.app.Activity
import rede.smartrede.sdk.api.IRedeSdk

class PrinterTef(activity: Activity, redeSdk: IRedeSdk) : IPrinterTef {
    private val printerService: IPrinter = PrinterService(activity, redeSdk)
    private val listContent : MutableList<Pair<Array<String>, List<Map<String, Int>>>> = ArrayList()
    private var tamanhoFonte = 17

    override fun ImprimeViaTef(bufferTef: ArrayList<String>) {
        var bufferAux = bufferTef.toString().split("\n")
        for (item in bufferAux){
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, item,tamanhoFonte))
        }
        printerService.impressoraOutput( saltarNLinha = 4, listContent = listContent)
    }
}