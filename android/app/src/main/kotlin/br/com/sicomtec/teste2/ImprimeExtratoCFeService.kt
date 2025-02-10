package com.sicomtec.br.testar_l400

import android.annotation.SuppressLint
import android.app.Activity
import com.sicomtec.br.testar_l400.MinhasFuncoesExtrato.formataVr
import com.sicomtec.br.testar_l400.MinhasFuncoesExtrato.mask
import com.sicomtec.br.testar_l400.MinhasFuncoesExtrato.montaLinha
import rede.smartrede.sdk.api.IRedeSdk


class ImprimeExtratoCFeService(activity: Activity, redeSdk: IRedeSdk) : IImprimeExtratoCFe  {

    private val printerService: IPrinter = PrinterService(activity, redeSdk)
//    private val concatena: MutableCollection<String> = arrayListOf()
    private val listContent : MutableList<Pair<Array<String>, List<Map<String, Int>>>> = ArrayList()

    @SuppressLint("DefaultLocale")
    override fun imprimeCFe(infcfe: InfCFe?, eTeste: Boolean?, resumido: Boolean?): String {

        val ncol = 38.toShort()

        val tamanhoFonte = 17

        var allret: MutableCollection<String?>

        var all: Collection<String?>

        listContent.clear()
        try {
            val cnpjCpfDestinatario = if (infcfe!!.Destinatario.CNPJ.length == 14) {
                infcfe.Destinatario.CNPJ
            } else if (infcfe.Destinatario.CPF.length == 11) {
                infcfe.Destinatario.CPF
            } else {
                ""
            }

            if (infcfe.Emitente.XFant.isNotEmpty() || infcfe.Emitente.XNome.isNotEmpty()) {
                allret = if(infcfe.Emitente.XFant.isNotEmpty()){
                    montaLinha(infcfe.Emitente.XFant, ncol)
                } else {
                    montaLinha(infcfe.Emitente.XNome, ncol)
                }
                for (item in allret) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!,tamanhoFonte))
                }
            }

            if (infcfe.Emitente.XLgr.isNotEmpty()) {
                val buffer = "${infcfe.Emitente.XLgr} ${infcfe.Emitente.Nro}"
                allret = montaLinha(buffer, ncol)
                for (item in allret) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!, tamanhoFonte))
                }
            }

            if (infcfe.Emitente.XCpl.isNotEmpty()) {
                for (item in montaLinha(infcfe.Emitente.XCpl, ncol)) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!,tamanhoFonte))
                }
            }

            if (infcfe.Emitente.XBairro.isNotEmpty()) {
                for (item in montaLinha("${infcfe.Emitente.XBairro} ${infcfe.Emitente.XMun} ${infcfe.Emitente.UF}", ncol)) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!,tamanhoFonte))
                }
            }

            if (infcfe.Emitente.IM.isNotEmpty()) {
                all = montaLinha(
                    java.lang.String.format(
                        "CNPJ:%1\$s", mask(
                            infcfe.Emitente.Cnpj, "##.###.###/####-##"
                        )
                    ) + " " + (java.lang.String.format(
                        "IE:%1\$s", mask(
                            infcfe.Emitente.IE, "###.###.###.###"
                        )
                    ) + " ") + " " + java.lang.String.format("IM:%1\$s", infcfe.Emitente.IM), ncol
                )
                when (all.size){
                    1 -> {
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.DIREITA, all.first()!!, tamanhoFonte))
                    }

                    2 -> {
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.CENTRO, "CNPJ: ${infcfe.Emitente.Cnpj}", tamanhoFonte))
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.CENTRO, "I.E.: ${infcfe.Emitente.IE}", tamanhoFonte))
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.CENTRO, "IM: ${infcfe.Emitente.IM}", tamanhoFonte))
                    }
                }
            } else {
                listContent.add(printerService.bufferLinhaAtrib("CNPJ:${mask(infcfe.Emitente.Cnpj, "##.###.###/####-##")}","I.E.${mask(infcfe.Emitente.IE, "###.###.###.###")}", 14))
            }

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, String.format("Extrato N.%1\$s", infcfe.Ide.NCFe),tamanhoFonte))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, "CUPOM FISCAL ELETRÔNICO - SAT",tamanhoFonte))

            if ((eTeste)!!) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "= T E S T E =",tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, ">".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, ">".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, ">".repeat(ncol.toInt()),tamanhoFonte))
            }

            if (infcfe.Destinatario.CNPJ.isNotEmpty()) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, String.format("CNPJ do Consumidor: %1\$s", mask(infcfe.Destinatario.CNPJ, "##.###.###/####-##")).repeat(ncol.toInt()),tamanhoFonte))

                if (infcfe.Destinatario.XNome.isNotEmpty()) {
                    all = montaLinha(
                        java.lang.String.format(
                            "Razao Social: %1\$s",
                            infcfe.Destinatario.XNome
                        ), ncol
                    )
                    when(all.size){
                        1 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.first()!!,tamanhoFonte))
                        }

                        2 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.first()!!,tamanhoFonte))
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.last()!!,tamanhoFonte))
                        }
                    }
                }
            } else if (infcfe.Destinatario.CPF.isNotEmpty()) {

                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, String.format("CPF do Consumidor: %1\$s", mask(infcfe.Destinatario.CPF, "###.###.###-##")),tamanhoFonte))

                if (infcfe.Destinatario.XNome.isNotEmpty()) {
                    all = montaLinha(
                        java.lang.String.format(
                            "Nome: %1\$s",
                            infcfe.Destinatario.XNome
                        ), ncol
                    )
                    when(all.size){
                        1 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.first()!!,tamanhoFonte))
                        }

                        2 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, all.first()!!,tamanhoFonte))
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, all.last()!!,tamanhoFonte))
                        }
                    }
                }
            }

            if (resumido == false) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "#|COD|QTD|UN|VL UN R$|(VL TR R$)*DESC|VL ITEM R$",15))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))

                for (produto: Det in infcfe.LDet) {
                    all = montaLinha(
                        String.format(
                            "%1\$s %2\$s %3\$s X ", produto.DetProd.NItem,
                            produto.DetProd.CProd,
                            MinhasFuncoesExtrato.formataQtde(produto.DetProd.QCom),
                            produto.DetProd.UCom
                        ),
                        String.format(
                            "%1\$s (%2\$s)", formataVr(produto.DetProd.VUnCom),
                            formataVr(produto.DetProd.VItem12741.toDouble())
                        ), ncol
                    )

                    if(all.size == 1){
                        listContent.add(printerService.bufferLinhaAtrib(String.format(
                            "%1\$s %2\$s %3\$s X ", produto.DetProd.NItem,
                            produto.DetProd.CProd,
                            MinhasFuncoesExtrato.formataQtde(produto.DetProd.QCom),
                            produto.DetProd.UCom
                        ),
                            String.format(
                                "%1\$s (%2\$s)", formataVr(produto.DetProd.VUnCom),
                                formataVr(produto.DetProd.VItem12741.toDouble())
                            ), tamanhoFonte))
                    } else {
                        for (item in all) {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA,
                                item,tamanhoFonte))
                        }
                    }

                    all = montaLinha(
                        String.format("%1\$s", produto.DetProd.XProd.uppercase()), String.format(
                            "%1\$s", formataVr(
                                produto.DetProd.VProd
                            )
                        ), ncol
                    )
                    if(all.size==1){
                        listContent.add(
                            printerService.bufferLinhaAtrib(
                                String.format("%1\$s",produto.DetProd.XProd.uppercase()),
                                String.format("%1\$s", formataVr(produto.DetProd.VProd)), tamanhoFonte))
                    } else {
                        for (item in all) {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA,
                                item,tamanhoFonte))
                        }
                    }

                    if (produto.DetProd.VOutro > 0) {
                        listContent.add(printerService.bufferLinhaAtrib("acrescimo", String.format("+%2$,.2f", produto.DetProd.VOutro),tamanhoFonte))
                    }

                    if (produto.DetProd.VDesc > 0) {
                        listContent.add(printerService.bufferLinhaAtrib("desconto", String.format("-%2$,.2f", produto.DetProd.VDesc),tamanhoFonte))
                    }

                    if (produto.DetProd.VRatAcr > 0) {
                        listContent.add(printerService.bufferLinhaAtrib("rateio de acrescimo sobre subtotal", String.format("+%2$,.2f", produto.DetProd.VRatAcr),tamanhoFonte))
                    }

                    if (produto.DetProd.VRatDesc > 0) {
                        listContent.add(printerService.bufferLinhaAtrib("rateio de desconto sobre subtotal", String.format("-%2$,.2f", produto.DetProd.VRatDesc),tamanhoFonte))
                    }
                }

                //printerService.avancaLinha(1)
                listContent.add(printerService.bufferLinhaAtrib("Total bruto de itens", formataVr(infcfe.Total.VProd), tamanhoFonte))

                if (infcfe.Total.VDescSubtot > 0 || infcfe.Total.VAcresSubtot > 0) {
                    if (infcfe.Total.VDescSubtot > 0) {
                        listContent.add(
                            printerService.bufferLinhaAtrib(
                                "Total de descontos sobre item",
                                String.format("-%2$,.2f", infcfe.Total.VDescSubtot.toFloat()),
                                tamanhoFonte
                            )
                        )
                    } else {
                        listContent.add(
                            printerService.bufferLinhaAtrib(
                                "Total de acrescimos sobre item",
                                String.format("+%2$,.2f", infcfe.Total.VAcresSubtot.toFloat()),
                                tamanhoFonte
                            )
                        )

                    }
                }
            }

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib("TOTAL R$", formataVr(infcfe.Total.VCFe), tamanhoFonte))

            if (resumido == false) {
                var vTroco = 0.0
                for (pagamento: Pagto in infcfe.LPagtos) {
                    if (pagamento.VTroco > 0) vTroco = pagamento.VTroco

                    listContent.add(printerService.bufferLinhaAtrib(MinhasFuncoesExtrato.retornaDescricaoPagtoSat(pagamento.CMP), formataVr(pagamento.VMP), tamanhoFonte))
                }

                if (vTroco > 0) {
                    listContent.add(printerService.bufferLinhaAtrib("Troco", formataVr(vTroco), tamanhoFonte))
                }
            }

            for (obsfisco: ObsFisco in infcfe.InfAdic.ObsFisco) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, "OBSERVAÇÕES DO FISCO", tamanhoFonte))

                all = montaLinha(
                    String.format(
                        "%1\$s %2\$s",
                        obsfisco.XCampoDet,
                        obsfisco.XTextoDet
                    ), ncol
                )
                if(all.size==1){
                    listContent.add(
                        printerService.bufferLinhaAtrib(
                            Alinhamento.ESQUERDA,
                            String.format(
                                "%1\$s %2\$s",
                                obsfisco.XCampoDet,
                                obsfisco.XTextoDet
                            ), tamanhoFonte))
                } else {
                    for (item in all) {
                        listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, item!!,tamanhoFonte))
                    }
                }
            }

            if (infcfe.Emitente.CRegTrib == 3.toShort()) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA,"*ICMS as ser recolhido conforme", tamanhoFonte))

                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA,"LC 123/2006 Simples Nacional", tamanhoFonte))

                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
            }

            if (infcfe.Entrega.XLgr.isNotEmpty()) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "DADOS PARA ENTREGA",tamanhoFonte))

                all = montaLinha(
                    String.format(
                        "Endereço: %1\$s %2\$s %3\$s %4\$s %5\$s", infcfe.Entrega.XLgr,
                        infcfe.Entrega.Nro,
                        infcfe.Entrega.XCpl,
                        infcfe.Entrega.XBairro,
                        infcfe.Entrega.UF
                    ), ncol
                )
                for (item in all) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, item!!,tamanhoFonte))
                }
            }

            if (infcfe.InfAdic.InfCpl.isNotEmpty()) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, "OBSERVAÇÕES DO CONTRIBUINTE",tamanhoFonte))

                all = montaLinha(infcfe.InfAdic.InfCpl, ncol)
                for (item in all) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, item!!,tamanhoFonte))
                }
            }

            if (resumido == false) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "*Valor aproximado dos tributos do item",tamanhoFonte))
            }

            all = montaLinha("Valor aproximado dos tributos deste cupom", ncol)
            for (item in all) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, item!!,tamanhoFonte))
            }

            listContent.add(printerService.bufferLinhaAtrib("(conforme Lei Fed.12.741/2012)", "R$ ${formataVr(infcfe.Total.VCFeLei12741)}", tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, String.format(
                "SAT Nº %1\$s", mask(
                    infcfe.Ide.NserieSAT, "###.###.###"
                )
            ),tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, String.format(
                "%1\$s - %2\$s",
                (infcfe.Ide.DEmi.substring(6, 8) + "/" + infcfe.Ide.DEmi.substring(
                    4,
                    6
                )) + "/" + infcfe.Ide.DEmi.substring(0, 4),
                infcfe.Ide.HEmi
            ),tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                mask(infcfe.Id.substring(3, 27), "#### #### #### #### #### ####"), tamanhoFonte))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                mask(infcfe.Id.substring(27, 47), "#### #### #### #### ####"), tamanhoFonte))

            val contentBarCode = infcfe.Id.substring(3, 47)
            val contentQrCode = String.format(
                "%1\$s %2\$s %3\$s %4\$s %5\$s", infcfe.Id.substring(3, 47),
                infcfe.Ide.DEmi + infcfe.Ide.HEmi,
                formataVr(infcfe.Total.VCFe).replace(",", "."),
                cnpjCpfDestinatario,
                infcfe.Ide.AssinaturaQRCODE
            )

            var listContent1 : MutableList<Pair<Array<String>, List<Map<String, Int>>>> = ArrayList()
            listContent1.clear()

            all = montaLinha(
                "Consulte o QR CODE pelo aplicativo De olho na nota, disponível na App Store (Apple) e Play Store (Android)",
                ncol
            )
            for (item in all) {
                listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, item!!,tamanhoFonte))
            }

            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, String.format(
                "PDV: %1\$s",
                infcfe.Ide.NumeroCaixa
            ),tamanhoFonte))

            printerService.impressoraOutputCFe(contentQrCode = contentQrCode ,contentBarCode = contentBarCode, listContent0 = listContent, listContent1 = listContent1)

        } catch (e: Exception) {
            e.printStackTrace()
            return ("|-1|${e.message}|")
        }
        return "|1|OK|"
    }

    override fun imprimeCFeCanc(infcfe: InfCFe?): String {

        val ncol = 38.toShort()

        val tamanhoFonte = 17

        var allret: MutableCollection<String?>

        var all: Collection<String?>

        listContent.clear()

        try {
            val cnpjCpfDestinatario = if (infcfe!!.Destinatario.CNPJ.length == 14) {
                infcfe.Destinatario.CNPJ
            } else if (infcfe.Destinatario.CPF.length == 11) {
                infcfe.Destinatario.CPF
            } else {
                ""
            }

            if (infcfe.Emitente.XFant.isNotEmpty() || infcfe.Emitente.XNome.isNotEmpty()) {
                allret = if(infcfe.Emitente.XFant.isNotEmpty()){
                    montaLinha(infcfe.Emitente.XFant, ncol)
                } else {
                    montaLinha(infcfe.Emitente.XNome, ncol)
                }
                for (item in allret) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!,tamanhoFonte))
                }
            }

            if (infcfe.Emitente.XLgr.isNotEmpty()) {
                val buffer = "${infcfe.Emitente.XLgr} ${infcfe.Emitente.Nro}"
                allret = montaLinha(buffer, ncol)
                for (item in allret) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!, tamanhoFonte))
                }
            }

            if (infcfe.Emitente.XCpl.isNotEmpty()) {
                for (item in montaLinha(infcfe.Emitente.XCpl, ncol)) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!,tamanhoFonte))
                }
            }

            if (infcfe.Emitente.XBairro.isNotEmpty()) {
                for (item in montaLinha("${infcfe.Emitente.XBairro} ${infcfe.Emitente.XMun} ${infcfe.Emitente.UF}", ncol)) {
                    listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, item!!,tamanhoFonte))
                }
            }

            if (infcfe.Emitente.IM.isNotEmpty()) {
                all = montaLinha(
                    java.lang.String.format(
                        "CNPJ:%1\$s", mask(
                            infcfe.Emitente.Cnpj, "##.###.###/####-##"
                        )
                    ) + " " + (java.lang.String.format(
                        "IE:%1\$s", mask(
                            infcfe.Emitente.IE, "###.###.###.###"
                        )
                    ) + " ") + " " + java.lang.String.format("IM:%1\$s", infcfe.Emitente.IM), ncol
                )
                when (all.size){
                    1 -> {
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.DIREITA, all.first()!!, tamanhoFonte))
                    }

                    2 -> {
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.CENTRO, "CNPJ: ${infcfe.Emitente.Cnpj}", tamanhoFonte))
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.CENTRO, "I.E.: ${infcfe.Emitente.IE}", tamanhoFonte))
                        listContent.add(printerService.bufferLinhaAtrib(alinhamento = Alinhamento.CENTRO, "IM: ${infcfe.Emitente.IM}", tamanhoFonte))
                    }
                }
            } else {
                listContent.add(printerService.bufferLinhaAtrib("CNPJ:${mask(infcfe.Emitente.Cnpj, "##.###.###/####-##")}","I.E.${mask(infcfe.Emitente.IE, "###.###.###.###")}", 14))
            }

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, String.format("Extrato N.%1\$s", infcfe.Ide.NCFe),18))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, "CUPOM FISCAL ELETRÔNICO - SAT CANCELAMENTO",15))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, "DADOS DO CUPOM FISCAL ELETRÔNICO CANCELADO",15))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))

            if (infcfe.Destinatario.CNPJ.isNotEmpty()) {
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, String.format("CNPJ do Consumidor: %1\$s", mask(infcfe.Destinatario.CNPJ, "##.###.###/####-##")).repeat(ncol.toInt()),tamanhoFonte))

                if (infcfe.Destinatario.XNome.isNotEmpty()) {
                    all = montaLinha(
                        String.format(
                            "Razao Social: %1\$s",
                            infcfe.Destinatario.XNome
                        ), ncol
                    )
                    when(all.size){
                        1 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.first()!!,tamanhoFonte))
                        }

                        2 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.first()!!,tamanhoFonte))
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.last()!!,tamanhoFonte))
                        }
                    }
                }
            } else if (infcfe.Destinatario.CPF.isNotEmpty()) {

                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))
                listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, String.format("CPF do Consumidor: %1\$s", mask(infcfe.Destinatario.CPF, "###.###.###-##")),tamanhoFonte))

                if (infcfe.Destinatario.XNome.isNotEmpty()) {
                    all = montaLinha(
                        java.lang.String.format(
                            "Nome: %1\$s",
                            infcfe.Destinatario.XNome
                        ), ncol
                    )
                    when(all.size){
                        1 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, all.first()!!,tamanhoFonte))
                        }

                        2 -> {
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, all.first()!!,tamanhoFonte))
                            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, all.last()!!,tamanhoFonte))
                        }
                    }
                }
            }
            listContent.add(printerService.bufferLinhaAtrib("TOTAL R$", formataVr(infcfe.Total.VCFe), tamanhoFonte))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO, String.format("SAT Nº %1\$s", mask(infcfe.Ide.NserieSAT, "###.###.###")), tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                String.format("%1\$s - %2\$s",(infcfe.IdeCancelado.DEmiCanc.substring(6,8) + "/" +
                        infcfe.IdeCancelado.DEmiCanc.substring(4, 6)) + "/" +
                        infcfe.IdeCancelado.DEmiCanc.substring(0, 4),infcfe.IdeCancelado.HEmiCanc), tamanhoFonte))

            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                mask(infcfe.IdeCancelado.NCFeCanc.substring(3,27), "#### #### #### #### #### ####"), tamanhoFonte))
            listContent.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                mask(infcfe.IdeCancelado.NCFeCanc.substring(27,47), "#### #### #### #### ####"), tamanhoFonte))

            val contentBarCode0 = infcfe.IdeCancelado.NCFeCanc.substring(3, 47)
            val contentQrCode0 = String.format(
                "%1\$s %2\$s %3\$s %4\$s %5\$s", infcfe.IdeCancelado.NCFeCanc.substring(3, 47),
                infcfe.IdeCancelado.DEmiCanc + infcfe.IdeCancelado.HEmiCanc,
                formataVr(infcfe.Total.VCFe).replace(",", "."),
                cnpjCpfDestinatario,
                infcfe.IdeCancelado.AssinaturaQRCODECanc
            )

            val listContent1 : MutableList<Pair<Array<String>, List<Map<String, Int>>>> = ArrayList()
            listContent1.clear()

            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.ESQUERDA, "=".repeat(ncol.toInt()),tamanhoFonte))

            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                "DADOS DO CUPOM FISCAL ELETRÔNICO DE CANCELAMENTO", tamanhoFonte))
            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                String.format("SAT Nº %1\$s", mask(infcfe.Ide.NserieSAT, "###.###.###")), tamanhoFonte))
            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                String.format("%1\$s - %2\$s",(infcfe.Ide.DEmi.substring(6, 8) + "/" +
                        infcfe.Ide.DEmi.substring(4,6)) + "/" +
                        infcfe.Ide.DEmi.substring(0, 4),infcfe.Ide.HEmi), tamanhoFonte))


            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                mask(infcfe.Id.substring(3, 27), "#### #### #### #### #### ####"), tamanhoFonte))
            listContent1.add(printerService.bufferLinhaAtrib(Alinhamento.CENTRO,
                mask(infcfe.Id.substring(27, 47), "#### #### #### #### ####"), tamanhoFonte))

            val contentBarCode1 = infcfe.Id.substring(3, 47)
            val contentQrCode1 = String.format(
                "%1\$s %2\$s %3\$s %4\$s %5\$s", infcfe.Id.substring(3, 47),
                infcfe.Ide.DEmi + infcfe.Ide.HEmi,
                formataVr(infcfe.Total.VCFe).replace(",", "."),
                cnpjCpfDestinatario,
                infcfe.Ide.AssinaturaQRCODE
            )

            printerService.impressoraOutputCFeCanc(contentQrCode0,
                                                contentQrCode1,
                                                contentBarCode0,
                                                contentBarCode1,
                                                listContent, listContent1)

        } catch (e: Exception) {
            e.printStackTrace()
            return ("|-1|${e.message}|")
        }
        return "|1|OK|"
    }

}