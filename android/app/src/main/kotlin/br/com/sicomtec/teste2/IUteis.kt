package com.sicomtec.br.testar_l400

interface IUteis {
    fun getSerialNumber() : String?
    fun Serialize(obj: Any?): String?
    fun <T> Deserialize(json: String, element: Class<T>): T
}