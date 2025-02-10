package com.sicomtec.br.testar_l400

import android.graphics.Bitmap
import br.com.positivo.printermodule.PrintAttributes.KEY_ALIGN
import br.com.positivo.printermodule.PrintAttributes.KEY_MARGINLEFT
import br.com.positivo.printermodule.PrintAttributes.KEY_TEXTSIZE
import br.com.positivo.printermodule.PrintAttributes.KEY_TYPEFACE
import br.com.positivo.printermodule.PrintAttributes.KEY_WEIGHT
import br.com.positivo.printermodule.PrintAttributes.VALUE_ALIGN_LEFT
import br.com.positivo.printermodule.PrintAttributes.VALUE_ALIGN_RIGHT
import br.com.positivo.printermodule.PrintAttributes.VALUE_TYPEFACE_BOLD
import org.apache.commons.lang3.StringUtils
import rede.smartrede.sdk.Receipt
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols.getInstance
import java.util.Locale

private const val MONEY_PATTERN = "###,###,##0.00"

