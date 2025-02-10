package com.sicomtec.br.testar_l400

abstract class Imposto : InfAdProd() {
    var VItem12741: Float = 0f
    var CSTIcms: String = ""
    var Orig: Byte = 0.toByte()
    var PICMS: Float = 0f
    var VICMS: Float = 0f
    var CSTPis: String = ""
    var VBCPis: Float = 0f
    var PPIS: Float = 0f
    var VPIS: Float = 0f
    var QBCProdPis: Float = 0f
    var VAliqProdPis: Float = 0f
    var CSTCofins: String = ""
    var VBCCofins: Float = 0f
    var PCofins: Float = 0f
    var VCofins: Float = 0f
    var QBCProdCofins: Float = 0f
    var VAliqProdCofins: Float = 0f
}