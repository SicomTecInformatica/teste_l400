package com.sicomtec.br.testar_l400

import io.flutter.plugin.common.MethodChannel
import rede.smartrede.commons.contract.IConnectorMifare
import rede.smartrede.commons.mifare.MifareKeyType
import rede.smartrede.commons.mifare.MifareResult
import rede.smartrede.commons.mifare.MifareResult.Success
import rede.smartrede.commons.mifare.MifareUtils.parseToHex
import rede.smartrede.commons.mifare.MifareUtils.toHexString
import rede.smartrede.sdk.api.IRedeSdk

class MifareService : IMifare {

    private val connectorMifare : IConnectorMifare

    constructor(redeSdk: IRedeSdk){
        val terminalFunctions = redeSdk.getTerminalFunctions()
        connectorMifare = terminalFunctions.getConnectorMifare()
    }

    override fun readCard(
        result: MethodChannel.Result,
        setorToRead: Int,
        blockToRead: Int,
        key: String
    ) {
        var setor = setorToRead.toString().toByte()
        var bloco = blockToRead.toString().toByte()
        var suaChave = key.parseToHex()

        with(connectorMifare) {
            activateCard { activateCardResult ->
                when (activateCardResult) {
                    is Success -> {
                        detectCards { detectResult ->
                            when (detectResult) {
                                is Success -> {
                                    authenticateSector(MifareKeyType.A, suaChave, setor) { authResult ->
                                        when (authResult) {
                                            is Success -> {
                                                readBlock(setor, bloco) { readBlockResult ->
                                                    when (readBlockResult) {
                                                        is Success -> {
                                                            println("1|${readBlockResult.data?.toHexString()?.decodeHex()?.substring(0,14)}|")
                                                            connectorMifare.powerOff{}
                                                            result.success("1|${readBlockResult.data?.toHexString()?.decodeHex()}|")
                                                        }

                                                        is MifareResult.Error -> {
                                                            println("-1|${readBlockResult.exception.message}|")
                                                            connectorMifare.powerOff{}
                                                            result.success("-1|${readBlockResult.exception.message}|")
                                                        }
                                                    }
                                                }
                                            }
                                            is MifareResult.Error -> {
                                                println("-1|${authResult.exception.message}|")
                                                connectorMifare.powerOff{}
                                                result.success("-1|${authResult.exception.message}|")
                                            }
                                        }
                                    }
                                }
                                is MifareResult.Error -> {
                                    println("-1|${detectResult.exception.message}|")
                                    connectorMifare.powerOff{}
                                    result.success("-1|${detectResult.exception.message}|")
                                }
                            }
                        }
                    }
                    is MifareResult.Error -> {
                        println("-1|${activateCardResult.exception.message}|")
                        connectorMifare.powerOff{}
                        result.success("-1|${activateCardResult.exception.message}|")
                    }
                }
            }
        }
    }

    override fun String.decodeHex(): String  {
        require(length % 2 == 0) { "Deve ter um comprimento uniforme"}
        return  chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
            .toString(Charsets.ISO_8859_1)
    }
}