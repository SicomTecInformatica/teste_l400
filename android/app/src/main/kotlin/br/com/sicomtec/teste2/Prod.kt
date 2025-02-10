package com.sicomtec.br.testar_l400


class Prod : Imposto() {
    var NItem: Short = 0.toShort()
    var CProd:kotlin.String? = ""
    var CEAN: String = ""
    var XProd: String = ""
    var NCM: String = ""
    var CCEST: String = ""
    var CFOP: String = "0000"
    var UCom: String = "UN"
    var QCom: Double = 0.0
    var VUnCom: Double = 0.0
    var VProd: Double = 0.0
    var IndRegra: String? = null
    var VDesc: Float = 0f
    var VOutro: Float = 0f
    var VItem: Double = 0.0
    var VRatDesc: Float = 0f
    var VRatAcr: Float = 0f
    var CodProdAnp: String = ""
    var ObsFiscos: List<ObsFisco> = ArrayList<ObsFisco>()
}