import '../config_tef/operacao_retorno.dart';
import '../enums/tipo_acao.dart';

abstract class Itefservice {
  List<String> get getListTipoPagamento;
  Map<String, String> get formatarPametrosVendaMsiTef;
  Map<String, String> get formatarPametrosCancelamentoMsiTef;
  Map<String, String> get formatarPametrosFuncoesMsiTef;
  Map<String, String> get formatarPametrosReimpressaoMsiTef;
  Map<String, String> get formatarPametrosPixMsiTef;
  Future<dynamic> enviarParametrosTef({required TipoAcaoEnum tipoAcao});
  void impressaoNotaTef(String texto);
  void dialogoImpressao(String textoNota);
  void dialogoTef(RetornoMsiTef operacaoRetorno);
  //String? retornaDescricaoTransacaoNegadaMsitef(String codresp);
}
