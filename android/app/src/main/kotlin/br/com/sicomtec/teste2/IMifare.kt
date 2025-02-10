package com.sicomtec.br.testar_l400

import io.flutter.plugin.common.MethodChannel
import java.nio.charset.Charset

interface IMifare {
    fun readCard(result: MethodChannel.Result, setorToRead: Int, blockToRead: Int, key: String)
    fun String.decodeHex(): String
}