package com.sicomtec.br.testar_l400

interface IImprimeExtratoCFe {
    fun imprimeCFe(infcfe: InfCFe?, eTeste: Boolean?, resumido: Boolean?): String?
    fun imprimeCFeCanc(infcfe: InfCFe?): String?
}