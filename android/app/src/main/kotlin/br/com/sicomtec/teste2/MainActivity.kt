package com.sicomtec.br.testar_l400

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.gson.JsonObject
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import rede.smartrede.sdk.RedePaymentValidationError
import rede.smartrede.sdk.RedePayments
import rede.smartrede.sdk.api.IRedeSdk


class MainActivity: FlutterActivity() {

    private lateinit var redepayments: RedePayments
    private lateinit var printer: IPrinter
    private lateinit var mifare: IMifare
    private lateinit var uteis: IUteis
    private lateinit var imprimeExtrato: IImprimeExtratoCFe
    private lateinit var imprimeTef: IPrinterTef
    private var _result: MethodChannel.Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val redeSdk = IRedeSdk.newInstance(applicationContext)
        redepayments = redeSdk.getRedePayments(this)
        mifare = MifareService(redeSdk)
        uteis = UteisService(redeSdk)
        printer = PrinterService(this.activity, redeSdk)
        imprimeExtrato = ImprimeExtratoCFeService(this.activity, redeSdk)
        imprimeTef = PrinterTef(this.activity, redeSdk)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger, "samples.flutter.dev/l400"
        ).setMethodCallHandler { call, result ->
            val bundle = Bundle()
            val map: Map<String, JsonObject>
            _result = result
            when (call.method) {
//                "tojson" -> {
//                    try {
//                        result.success(uteis.Serialize(call.argument("obj")))
//                    } catch (e: Exception) {
//                        result.success("|-1|${e.message}|")
//                    }
//                }
//
//                "fromjson" -> {
//                    try {
//                        val rootObject = JsonObject()
//                        rootObject.get(call.argument("json"))
//                        result.success(
//                            uteis.Deserialize(
//                                call.argument("json")!!,
//                                rootObject.javaClass
//                            )
//                        )
//                    } catch (e: Exception) {
//                        result.success("|-1|${e.message}|")
//                    }
//                }

                "leitorMifare" -> {
                    try {
                        mifare.readCard(result, 2, 0, "2D9C39CFF97E")

                    } catch (e: Exception) {
                        result.success("|-1|${e.message}|")
                    } finally {
                        //mifare.powerOff()
                    }

                }

                "NumeroSerialPOS" -> {
                    try {
                        val ns = uteis.getSerialNumber()
                        result.success("|1|$ns|")
                    } catch (e: Exception) {
                        result.success("|-1|${e.message}|")
                    }
                }
                "printerBuffer" -> {
                    try {
                        val json: String = call.argument("buffer")!!
                        val listaLinhas: ArrayList<String> = ArrayList()
                        listaLinhas.add(json)
                        imprimeTef.ImprimeViaTef(listaLinhas)
                        result.success("|1|Ok|")

                    } catch (e: Exception) {
                        result.success("|-1|" + e.message + "|")
                    }
                }

                "printerCFe" -> {
                    try {
                        val json: String = call.argument("json")!!

                        val infcfe: InfCFe = uteis.Deserialize(json, InfCFe::class.java)
                            ?: throw Exception("InfCFe invalido, não pode ser nulo")

                        val retornoPrt: String? =
                            imprimeExtrato.imprimeCFe(infcfe, eTeste = false, resumido = false)

                        result.success(retornoPrt)

                    } catch (e: Exception) {
                        result.success("|-1|" + e.message + "|")
                    }

                }

                "printerCancCFe" -> {
                    try {
                        val json: String = call.argument("json")!!

                        val infcfe: InfCFe = uteis.Deserialize(json, InfCFe::class.java)
                            ?: throw Exception("InfCFe invalido, não pode ser nulo")

                        val retornoPrt: String? =
                            imprimeExtrato.imprimeCFeCanc(infcfe)

                        result.success(retornoPrt)

                    } catch (e: Exception) {
                        result.success("|-1|" + e.message + "|")
                    }

                }

                "realizarAcaoMsitef" -> {
                    try {
                        val intentSitef =
                            Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF") // Instanciando o Intent

                        map = call.argument("mapMsiTef")!!
                        for (key in map.keys) {
                            bundle.putString(key, map[key].toString())
                        }

                        intentSitef.putExtras(bundle)
                        startActivityForResult(intentSitef, 4321)

                    } catch (e: ActivityNotFoundException) {
                        result.success("|-1|Application Pagamentos not found.|")
                        //Toast.makeText(this, "Application Pagamentos not found", LENGTH_SHORT).show()
                    } catch (e: RedePaymentValidationError) {
                        result.success("|-1|Intent validation error.|")
                        //Toast.makeText(this, "Intent validation error", LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        result.success("|-1|${e.message}}|")
                        //Toast.makeText(this, e.message, LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
        if (requestCode == 4321) {
            if (resultcode == RESULT_OK || resultcode == RESULT_CANCELED && data != null) {
                try {
                    _result!!.success(respSitefToJson(data))
                } catch (e: Exception) {
                    _result!!.success("-1|${e.message}|")
                    //Toast.makeText(this, e.message, LENGTH_SHORT).show()
                }
            } else {
                _result!!.success(respSitefToJson(data))
            }

        } else {
            _result!!.success("-1|${resultcode.toChar()}|")
            //Toast.makeText(this, requestCode.toString(), LENGTH_SHORT).show()
        }
    }

    // O M-Sitef não retorna um json como resposta, logo é criado um json com a
    // reposta do Sitef.
    private fun respSitefToJson(data: Intent?): String {
        val json = JSONObject()
        json.put("CODRESP", data!!.getStringExtra("CODRESP"))
        json.put("COMP_DADOS_CONF", data.getStringExtra("COMP_DADOS_CONF"))
        json.put("CODTRANS", data.getStringExtra("CODTRANS"))
        json.put("VLTROCO", data.getStringExtra("VLTROCO"))
        json.put("REDE_AUT", data.getStringExtra("REDE_AUT"))
        json.put("BANDEIRA", data.getStringExtra("BANDEIRA"))
        json.put("NSU_SITEF", data.getStringExtra("NSU_SITEF"))
        json.put("NSU_HOST", data.getStringExtra("NSU_HOST"))
        json.put("COD_AUTORIZACAO", data.getStringExtra("COD_AUTORIZACAO"))
        json.put("NUM_PARC", data.getStringExtra("NUM_PARC"))
        json.put("TIPO_PARC", data.getStringExtra("TIPO_PARC"))
        // Quando passa esta informação para o flutter os break lines são perdidos logo
        // é necessario para outro dado para representar no caso %%
        json.put("VIA_ESTABELECIMENTO", data.getStringExtra("VIA_ESTABELECIMENTO"))
        json.put("VIA_CLIENTE", data.getStringExtra("VIA_CLIENTE"))
        //json.put("TIPO_CAMPOS", data.getStringExtra("TIPO_CAMPOS"))
        return json.toString()
    }
}


