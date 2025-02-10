package com.sicomtec.br.testar_l400

import com.google.gson.Gson
import rede.smartrede.commons.contract.ITerminalFunctions
import rede.smartrede.sdk.api.IRedeSdk


class UteisService : IUteis {

    private val terminalFunctions : ITerminalFunctions

    constructor(redeSdk: IRedeSdk){
        terminalFunctions = redeSdk.getTerminalFunctions()
    }

    override fun getSerialNumber(): String {
        return terminalFunctions.getSerialNumber()
    }

    override fun Serialize(obj: Any?): String? {
        return Gson().toJson(obj)
    }

    override fun <T> Deserialize(json: String, element: Class<T>): T {
        return Gson().fromJson<T>(json, element)
    }
}