import 'dart:convert';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';

import '../Interfaces/itef_service.dart';
import '../components/custom_dialog_component.dart';
import '../config_tef/operacao_retorno.dart';
import '../enums/com_externa.dart';
import '../enums/tipo_acao.dart';
import '../enums/tipo_pagto_enum.dart';
import '../models/lista_erros_tef.dart';

class TefServices implements Itefservice {
  late MethodChannel _platform;
  late BuildContext _context;

  String _valorVenda = "0";
  String get getValorVenda =>
      _valorVenda.replaceAll('.', "").replaceAll(',', "");
  set setValorVenda(String valorDeVenda) => _valorVenda = valorDeVenda;

  late TipoPagtoEnum _tipoPagamento;
  TipoPagtoEnum get getTipoPagamento => _tipoPagamento;
  set setTipoPagamento(TipoPagtoEnum tipopagto) => _tipoPagamento = tipopagto;

  late ComExternaEnum _comExterna;
  ComExternaEnum get getComExterna => _comExterna;
  set setComExterna(ComExternaEnum comExterna) => _comExterna = comExterna;

  int _quantParcelas = 0;
  int get getQuantParcelas => _quantParcelas;
  set setQuantParcelas(int quantParcelas) => _quantParcelas = quantParcelas;

  String _operador = "";
  String get getOperador => _operador;
  set setOperador(String operador) => _operador = operador;

  bool _habilitarImpressao = true;
  bool get getHabilitarImpressao => _habilitarImpressao;
  set setHabilitarImpressao(bool habilitarImpressao) =>
      _habilitarImpressao = habilitarImpressao;

  String _ipConfig = "";
  String get getIpConfig => _ipConfig;
  set setIpConfig(String ipConfig) => _ipConfig = ipConfig;

  String _empresaSitef = "";
  String get getEmpresaSitef => _empresaSitef;
  set setEmpresaSitef(String empresaSitef) => _empresaSitef = empresaSitef;

  String _cnpjcpf = "";
  String get getCnpjCpf => _cnpjcpf;
  set setCnpjCpf(String cnpjcpf) => _cnpjcpf = cnpjcpf;

  String _tokenRegistroTls = "";
  String get getTokenRegistroTls => _tokenRegistroTls;
  set setTokenRegistroTls(String tokenRegistroTls) =>
      _tokenRegistroTls = tokenRegistroTls;

  TefServices({
    required platform,
    required context,
    required String valorVenda,
    required TipoPagtoEnum tipoPagamento,
    required int quantParcelas,
    required String operador,
    required bool habilitarImpressao,
    required String ipConfig,
    required String empresaSitef,
    required String cnpjcpf,
    required String tokenRegistroTls,
    required ComExternaEnum comExterna,
  }) {
    _platform = platform;
    _context = context;
    _valorVenda = valorVenda;
    _tipoPagamento = tipoPagamento;
    _quantParcelas = quantParcelas;
    _operador = operador;
    _habilitarImpressao = habilitarImpressao;
    _ipConfig = ipConfig;
    _empresaSitef = empresaSitef;
    _cnpjcpf = cnpjcpf;
    _tokenRegistroTls = tokenRegistroTls;
    _comExterna = comExterna;
  }

  ListaErrosTef errosTef = ListaErrosTef();
  String _tipoParcelamento = "";
  String get getTipoParcelamento => _tipoParcelamento;
  set setTipoParcelamento(String tipoParcelamento) =>
      _tipoParcelamento = tipoParcelamento;

  //Recebe um Json obtido através da chamada feito para Tef e a formata para devolver ao Flutter.
  Map<String, dynamic> formatarInfoRecebida(myjson) {
    Map<String, dynamic> mapResultado;
    myjson = myjson.toString().replaceAll('\\r', "");
    myjson = myjson.toString().replaceAll('"{', "{ ");
    myjson = myjson.toString().replaceAll('}"', " }");
    var parsedJson = json.decode(myjson);
    mapResultado = Map<String, dynamic>.from(parsedJson);
    return mapResultado;
  }

  @override
  Future<void> dialogoImpressao(String textoNota) async {
    showDialog(
      context: _context,
      builder: (context) {
        return AlertDialog(
          title: const Text("Realizar Impressão"),
          content: const SizedBox(
            height: 150,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text("Deseja realizar a impressão pela aplicação ?"),
              ],
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text("Não"),
              onPressed: () {
                Navigator.pop(context);
              },
            ),
            TextButton(
              child: const Text("Sim"),
              onPressed: () {
                impressaoNotaTef(textoNota);
                Navigator.pop(context);
              },
            ),
          ],
        );
      },
    );
  }

  @override
  void dialogoTef(RetornoMsiTef operacaoRetorno) {
    showDialog(
      context: _context,
      builder: (context) {
        return AlertDialog(
          title: const Text("Erro!"),
          content: SizedBox(
            height: 100,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text(" ${errosTef.mensagemErro(operacaoRetorno.getCodResp)}"),
              ],
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text("Ok"),
              onPressed: () {
                Navigator.pop(context);
              },
            )
          ],
        );
      },
    );
  }

  @override
  Future enviarParametrosTef({required TipoAcaoEnum tipoAcao}) async {
    dynamic myjson;
    dynamic parametroFormatado;
    // Verificando qual foi a tef selecionada, dependendo da Tef os seus parâmetros são formatados;
    // Caso não for Ger7, então M-Sitef
    switch (tipoAcao) {
      case TipoAcaoEnum.venda:
        switch (getTipoPagamento) {
          case TipoPagtoEnum.pix:
            parametroFormatado = formatarPametrosPixMsiTef;
            break;
          default:
            parametroFormatado = formatarPametrosVendaMsiTef;
            break;
        }
        break;
      case TipoAcaoEnum.cancelamento:
        parametroFormatado = formatarPametrosCancelamentoMsiTef;
        break;
      case TipoAcaoEnum.funcoes:
        parametroFormatado = formatarPametrosFuncoesMsiTef;
        break;
      case TipoAcaoEnum.pix:
        parametroFormatado = formatarPametrosPixMsiTef;
        break;
      case TipoAcaoEnum.reimpressao:
        parametroFormatado = formatarPametrosReimpressaoMsiTef;
        break;
    }
    myjson = await _platform.invokeMethod(
      'realizarAcaoMsitef',
      <String, dynamic>{"mapMsiTef": parametroFormatado},
    );
    return RetornoMsiTef.fromJson(formatarInfoRecebida(myjson));
  }

  @override
  Map<String, String> get formatarPametrosCancelamentoMsiTef {
    Map<String, String> mapMsiTef = {};
    DateTime now = DateTime.now();
    mapMsiTef["empresaSitef"] = getEmpresaSitef;
    mapMsiTef["enderecoSitef"] = getIpConfig;
    mapMsiTef["operador"] = getOperador;
    mapMsiTef["data"] = DateFormat('yyyyMMdd').format(now);
    mapMsiTef["hora"] = DateFormat('kkmmss').format(now);
    mapMsiTef["numeroCupom"] = Random().nextInt(9999999).toString();
    mapMsiTef["valor"] = getValorVenda;
    mapMsiTef["CNPJ_CPF"] = getCnpjCpf; //"03654119000176";
    mapMsiTef["comExterna"] = getComExterna.index.toString();
    mapMsiTef["modalidade"] = "200";
    mapMsiTef["isDoubleValidation"] = "0";
    mapMsiTef["restricoes"] = "";
    //mapMsiTef["transacoesHabilitadas"] = getTransacoesHabilitadas;
    mapMsiTef["caminhoCertificadoCA"] = "ca_cert_perm";

    if (getListTipoPagamento[1] == "3") {
      //credito
      if (getQuantParcelas == 1 || getQuantParcelas == 0) {
        mapMsiTef["transacoesHabilitadas"] = "26";
        mapMsiTef["numParcelas"] = "";
      } else if (getTipoParcelamento == "Loja") {
        mapMsiTef["transacoesHabilitadas"] = "27";
      } else if (getTipoParcelamento == "Adm") {
        mapMsiTef["transacoesHabilitadas"] = "28";
      }
    }
    if (getListTipoPagamento[1] == "2") {
      //debito
      mapMsiTef["transacoesHabilitadas"] = "16";
      mapMsiTef["restricoes"] = "TransacoesHabilitadas=16}";
    }
    if (getListTipoPagamento[1] == "0") {
      mapMsiTef["restricoes"] = "transacoesHabilitadas=16";
      mapMsiTef["transacoesHabilitadas"] = "";
    }

    return mapMsiTef;
  }

  @override
  Map<String, String> get formatarPametrosFuncoesMsiTef {
    Map<String, String> mapMsiTef = {};
    DateTime now = DateTime.now();
    mapMsiTef["empresaSitef"] = getEmpresaSitef;
    mapMsiTef["enderecoSitef"] = getIpConfig;
    mapMsiTef["operador"] = getOperador;
    mapMsiTef["data"] = DateFormat('yyyyMMdd').format(now);
    mapMsiTef["hora"] = DateFormat('kkmmss').format(now);
    mapMsiTef["numeroCupom"] = Random().nextInt(9999999).toString();
    mapMsiTef["valor"] = getValorVenda;
    mapMsiTef["CNPJ_CPF"] = getCnpjCpf; //"03654119000176";
    mapMsiTef["cnpj_automacao"] = "02468872000104";
    mapMsiTef["comExterna"] = getComExterna.index.toString();
    mapMsiTef["modalidade"] = "110";
    mapMsiTef["isDoubleValidation"] = "0";
    mapMsiTef["caminhoCertificadoCA"] = "ca_cert_perm";
    mapMsiTef["restricoes"] = "transacoesHabilitadas=16;26;27";
    return mapMsiTef;
  }

  @override
  Map<String, String> get formatarPametrosReimpressaoMsiTef {
    Map<String, String> mapMsiTef = {};
    DateTime now = DateTime.now();
    mapMsiTef["empresaSitef"] = getEmpresaSitef;
    mapMsiTef["enderecoSitef"] = getIpConfig;
    mapMsiTef["operador"] = getOperador;
    mapMsiTef["data"] = DateFormat('yyyyMMdd').format(now);
    mapMsiTef["hora"] = DateFormat('kkmmss').format(now);
    mapMsiTef["numeroCupom"] = Random().nextInt(9999999).toString();
    mapMsiTef["valor"] = getValorVenda;
    mapMsiTef["CNPJ_CPF"] = getCnpjCpf; //"03654119000176";
    mapMsiTef["cnpj_automacao"] = "02468872000104";
    mapMsiTef["comExterna"] = getComExterna.index.toString();
    mapMsiTef["modalidade"] = "114";
    mapMsiTef["isDoubleValidation"] = "0";
    mapMsiTef["caminhoCertificadoCA"] = "ca_cert_perm";
    return mapMsiTef;
  }

  @override
  Map<String, String> get formatarPametrosPixMsiTef {
    Map<String, String> mapMsiTef = {};
    DateTime now = DateTime.now();
    mapMsiTef["empresaSitef"] = getEmpresaSitef;
    mapMsiTef["enderecoSitef"] = getIpConfig;
    mapMsiTef["comExterna"] = getComExterna.index.toString();
    mapMsiTef["modalidade"] = "0";
    mapMsiTef["CNPJ_CPF"] = getCnpjCpf; //"03654119000176";
    mapMsiTef["cnpj_automacao"] = "02468872000104";
    mapMsiTef["operador"] = getOperador;
    mapMsiTef["data"] = DateFormat('yyyyMMdd').format(now);
    mapMsiTef["hora"] = DateFormat('kkmmss').format(now);
    mapMsiTef["numeroCupom"] = Random().nextInt(9999999).toString();
    mapMsiTef["valor"] = getValorVenda;
    mapMsiTef["transacoesHabilitadas"] = "7;8;";
    return mapMsiTef;
  }

  @override
  Map<String, String> get formatarPametrosVendaMsiTef {
    Map<String, String> mapMsiTef = {};
    DateTime now = DateTime.now();
    mapMsiTef["empresaSitef"] = getEmpresaSitef;
    mapMsiTef["enderecoSitef"] = getIpConfig;
    mapMsiTef["operador"] = getOperador;
    mapMsiTef["data"] = DateFormat('yyyyMMdd').format(now);
    mapMsiTef["hora"] = DateFormat('kkmmss').format(now);
    mapMsiTef["numeroCupom"] = Random().nextInt(9999999).toString();
    mapMsiTef["modalidade"] = getListTipoPagamento[1];
    mapMsiTef["valor"] = getValorVenda;
    mapMsiTef["CNPJ_CPF"] = getCnpjCpf; //"03654119000176";
    mapMsiTef["cnpj_automacao"] = "02468872000104";
    mapMsiTef["comExterna"] = getComExterna.index.toString();
    mapMsiTef["timeoutColeta"] = "30";
    if (mapMsiTef["comExterna"] == "4") {
      mapMsiTef["tokenRegistroTls"] = getTokenRegistroTls;
    }
    if (getListTipoPagamento[1] == "3") {
      if (getQuantParcelas == 1 || getQuantParcelas == 0) {
        mapMsiTef["transacoesHabilitadas"] = "26";
        mapMsiTef["numParcelas"] = "";
      } else if (getTipoParcelamento == "Loja") {
        mapMsiTef["transacoesHabilitadas"] = "27";
      } else if (getTipoParcelamento == "Adm") {
        mapMsiTef["transacoesHabilitadas"] = "28";
      }
      mapMsiTef["numParcelas"] = getQuantParcelas.toString();
    }
    if (getListTipoPagamento[1] == "2") {
      mapMsiTef["transacoesHabilitadas"] = "16";
      mapMsiTef["restricoes"] = "TransacoesHabilitadas=16}";
      mapMsiTef["numParcelas"] = "";
    }
    if (getListTipoPagamento[1] == "0") {
      mapMsiTef["restricoes"] = "transacoesHabilitadas=16;26;27";
      mapMsiTef["transacoesHabilitadas"] = "";
      mapMsiTef["numParcelas"] = "";
    }
    mapMsiTef["isDoubleValidation"] = "0";
    mapMsiTef["caminhoCertificadoCA"] = "ca_cert_perm";
    return mapMsiTef;
  }

  @override
  List<String> get getListTipoPagamento {
    if (_tipoPagamento == TipoPagtoEnum.credito) {
      return ["1", "3"];
    } else if (_tipoPagamento == TipoPagtoEnum.debito) {
      return ["2", "2"];
    } else if (_tipoPagamento == TipoPagtoEnum.todos) {
      return ["4", "0"];
    } else {
      return ["0", "0"];
    }
  }

  @override
  Future<void> impressaoNotaTef(String texto) async {
    try {
      await _platform
          .invokeMethod('printerBuffer', <String, dynamic>{"buffer": texto});
    } on PlatformException catch (e) {
      CustomDialogComponent(
          msg: e.message!, titulo: "Erro de impressão do docto.TEF.");
    }
  }

  // @override
  // String? retornaDescricaoTransacaoNegadaMsitef(String codresp) {
  //   switch(codresp){
  //     case "-1":
  //       return "Módulo não inicializado.\n O PDV tentou chamar alguma rotina sem antes executar a função configura.";
  //     case "-2":
  //       return "Operação cancelada pelo operador.";
  //     case "-3":
  //       return "O parâmetro função / modalidade é inexistente/inválido.";
  //     case "-4":
  //       return "Falta de memória no PDV.";
  //     case "-5":
  //       return "Sem comunicação com o SiTef.";
  //     case "-6":
  //       return "Operação cancelada pelo usuário (no pinpad).";
  //     case "-9":
  //       return "A automação chamou a rotina ContinuaFuncaoSiTefInterativo\n sem antes iniciar uma função iterativa.";
  //     case "-10":
  //       return "Algum parâmetro obrigatório não foi passado pela automação comercial.";
  //     case "-12":
  //       return "Erro na execução da rotina iterativa.\n Provavelmente o processo iterativo anterior\n não foi executado até o final(enquanto o retorno for igual a 10000).";
  //     case "-13":
  //       return "Documento fiscal não encontrado nos registros da CliSiTef.\n Retornado em funções de consulta tais como ObtemQuantidadeTransaçõesPendentes.";
  //     case "-15":
  //       return "Operação cancelada pela automação comercial.";
  //     case "-20":
  //       return "Parâmetro inválido passado para a função.";
  //     case "-25":
  //       return "Erro no Correspondente Bancário: Deve realizar sangria.";
  //     case "-30":
  //       return "Erro de acesso ao arquivo.\n Certifique-se que o usuário que roda a aplicação\na tem direitos de leitura / escrita";
  //     case "-40":
  //       return "Transação negada pelo servidor SiTef.";
  //     case "-41":
  //       return "Dados inválidos.";
  //     case "-43":
  //       return "Problema na execução de alguma das rotinas no pinpad.";
  //     case "-50":
  //       return "Transação não segura.";
  //     case "-100":
  //       return "rro interno do módulo.";
  //     case "1":
  //       return "Endereço IP inválido ou não resolvido.";
  //     case "2":
  //       return "Código da loja inválido.";
  //     case "3":
  //       return "Código de terminal inválido.";
  //     case "6":
  //       return "Erro na inicialização do Tcp/Ip.";
  //     case "7":
  //       return "Falta de memória.";
  //     case "8":
  //       return "Não encontrou a CliSiTef ou ela está com problemas.";
  //     case "9":
  //       return "Configuração de servidores SiTef foi excedida.";
  //     case "10":
  //       return "Erro de acesso na pasta CliSiTef (possível falta de permissão para escrita).";
  //     case "11":
  //       return "Dados inválidos passados pela automação.";
  //     case "12":
  //       return "Modo seguro não ativo.";
  //     case "13":
  //       return "Caminho DLL inválido (o caminho completo das bibliotecas está muito grande).";
  //     default:
  //       return "Erro ${codresp} não deteminado.";
  //   }
  // }
}
