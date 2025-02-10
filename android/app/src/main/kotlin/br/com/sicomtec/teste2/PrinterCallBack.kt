package com.sicomtec.br.testar_l400

import android.app.Activity
import android.widget.Toast
import android.widget.Toast.makeText
import rede.smartrede.commons.callback.IPrinterCallback

class PrinterCallBack(private val activity: Activity) : IPrinterCallback {

    override fun onCompleted() {
        showToast("Impressão Ok ...")
    }

    override fun onError(errorMessage: String) {
        showToast("Erro na impressora $errorMessage ...")
    }

    private fun showToast(message: String){
        with(activity) {
            runOnUiThread {
                makeText(this,message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}