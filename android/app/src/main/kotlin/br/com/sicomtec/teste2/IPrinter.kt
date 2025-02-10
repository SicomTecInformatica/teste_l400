package com.sicomtec.br.testar_l400

interface IPrinter {
    fun impressoraOutput(saltarNLinha: Int, listContent : MutableList<Pair<Array<String>, List<Map<String, Int>>>>)

    fun impressoraOutputCFe(contentQrCode: String,
                            contentBarCode: String,
                            listContent0 : MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
                            listContent1 : MutableList<Pair<Array<String>, List<Map<String, Int>>>>)

    fun impressoraOutputCFeCanc(contentQrCode0: String,
                                contentQrCode1: String,
                                contentBarCode0: String,
                                contentBarCode1: String,
                                listContent0 : MutableList<Pair<Array<String>, List<Map<String, Int>>>>,
                                listContent1 : MutableList<Pair<Array<String>, List<Map<String, Int>>>>)
    fun avancaLinha(nlinhas: Int)
    fun postToWorkerThread(command: Runnable)
    fun bufferLinhaAtrib(alinhamento: Alinhamento, buffer: String, tamanhoFonte: Int) : Pair<Array<String>, List<Map<String, Int>>>
    fun bufferLinhaAtrib(buffer0: String, buffer1: String, tamanhoFonte: Int) : Pair<Array<String>, List<Map<String, Int>>>
    fun bufferLinhaAtrib(buffer0: String, buffer1: String, buffer2: String, tamanhoFonte: Int) : Pair<Array<String>, List<Map<String, Int>>>
}