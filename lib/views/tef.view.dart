import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_masked_text2/flutter_masked_text2.dart';
import '../Interfaces/itef_service.dart';
import '../Services/tef_services.dart';
import '../config_tef/operacao_retorno.dart';
import '../enums/com_externa.dart';
import '../enums/tipo_acao.dart';
import '../enums/tipo_pagto_enum.dart';

class TefView extends StatefulWidget {
  const TefView({super.key});

  @override
  State<TefView> createState() => _TefViewState();
}

class _TefViewState extends State<TefView> {
  final formKey = GlobalKey<FormState>();

  String opcaocomExterna = "";
  final _opcaocomExterna = [
    'Sem (apenas para SiTef dedicado)',
    'TLS Software Express',
    'TLS WNB Comnect',
    'TLS Gsurf',
    'TLS GWP (TLS Fiserv)'
  ];
  var _comExternaSelecionado = 'Sem (apenas para SiTef dedicado)';

  bool? isCheckedDebito = true;
  bool? isCheckedCredito = false;
  bool? isCheckedPIX = false;
  bool? isCheckedTodos = false;

  static const _platform = MethodChannel('samples.flutter.dev/l400');

  final valorVenda = MoneyMaskedTextController(
      decimalSeparator: ',', thousandSeparator: '.', initialValue: 0);

  final ipServidor =
      TextEditingController(text: '192.168.15.38'); //Text edit ip do servidor

  final numParcelas =
      TextEditingController(text: "1"); //Text edit da quantidade de parcelas

  final empresaSitef = TextEditingController(text: '00000000');

  final cnpjcpf = TextEditingController(text: '03654119000176');

  final tokenRegistroTls = TextEditingController(text: '');

  bool habilitarImpressao = true; //armazena se a impressao vai ser realizada

  ComExternaEnum comexterna = ComExternaEnum.sem;

  TipoPagtoEnum tipoPagamentoSelecionado = TipoPagtoEnum
      .debito; // = "Débito"; //armazena o tipo de pagamento escolhido

  String tefSelecionado = "msitef"; //armazena a Tef escolhida
  String tipoParcelamento = "Adm"; //armazena a Tef escolhida

  void realizarFuncao(TipoAcaoEnum acao) async {
    // Settando os valores necessarios pra venda e chamando a funcao responsel pela venda.
    // Retira mascara Money antes de enviar o valor para a função.

    Itefservice tefServices = TefServices(
        platform: _platform,
        context: context,
        valorVenda: valorVenda.text,
        tipoPagamento: tipoPagamentoSelecionado,
        quantParcelas: int.parse(numParcelas.text),
        operador: "0001",
        habilitarImpressao: habilitarImpressao,
        ipConfig: ipServidor.text,
        empresaSitef: empresaSitef.text,
        cnpjcpf: cnpjcpf.text,
        tokenRegistroTls: tokenRegistroTls.text,
        comExterna: comexterna);

    await tefServices.enviarParametrosTef(tipoAcao: acao).then((resultadoTef) {
      RetornoMsiTef retornoMsiTef = resultadoTef;
      // print(retornoMsiTef.toJson()); - Caso deseje printar o Json de retorno
      // Verifica se tem algo pra imprimir
      if (retornoMsiTef.getCodResp == "0") {
        String impressao = "";
        if (retornoMsiTef.textoImpressoCliente.isNotEmpty) {
          impressao += retornoMsiTef.textoImpressoCliente;
        }

        if (retornoMsiTef.textoImpressoEstabelecimento.isNotEmpty) {
          impressao += "\n\n-----------------------------     \n";
          impressao += retornoMsiTef.textoImpressoEstabelecimento;
        }
        if (impressao != "") {
          tefServices.dialogoImpressao(impressao);
        }
      } else {
        if (retornoMsiTef.getCodResp != "0") {
          tefServices.dialogoTef(retornoMsiTef);
        }
      }
      // Verifica se ocorreu um erro durante venda ou cancelamento
      //   if (acao == "venda" || acao == "cancelamento") {
      //     tefServices.dialogoTef(retornoMsiTef);
      // }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('TEF(TLS Fiserv)'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(10),
        child: Form(
          key: formKey,
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const SizedBox(
                  height: 5,
                ),
                const Text("Selecione opção"),
                SizedBox(
                  width: 450,
                  child: DropdownButton<String>(
                    alignment: Alignment.center,
                    items: _opcaocomExterna.map((String dropDownComExterna) {
                      return DropdownMenuItem<String>(
                        value: dropDownComExterna,
                        child: Text(dropDownComExterna),
                      );
                    }).toList(),
                    onChanged: (String? comExternaSelecionado) {
                      setState(() {
                        _comExternaSelecionado = comExternaSelecionado!;
                      });
                      setState(() {
                        _comExternaSelecionado = comExternaSelecionado!;
                      });
                    },
                    value: _comExternaSelecionado,
                  ),
                ),
                const SizedBox(
                  height: 10,
                ),
                SizedBox(
                  width: 200,
                  child: TextFormField(
                    controller: empresaSitef,
                    obscureText: false,
                    keyboardType: TextInputType.number,
                    maxLength: 8,
                    textAlign: TextAlign.center,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Empresa Sitef',
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "Não pode ser nulo.";
                      }

                      return null;
                    },
                  ),
                ),
                const SizedBox(height: 10),
                SizedBox(
                  width: 200,
                  child: TextFormField(
                    controller: cnpjcpf,
                    obscureText: false,
                    keyboardType: TextInputType.number,
                    maxLength: 14,
                    textAlign: TextAlign.center,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'CNPJ/CPF',
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "Não pode ser nulo.";
                      }

                      if (double.parse(value) == 0) {
                        return "Não pode ser zero.";
                      }

                      return null;
                    },
                  ),
                ),
                const SizedBox(height: 10),
                SizedBox(
                  width: 250,
                  child: TextFormField(
                    controller: ipServidor,
                    obscureText: false,
                    keyboardType: TextInputType.text,
                    textAlign: TextAlign.center,
                    textInputAction: TextInputAction.next,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'IP TEF',
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "O IP não pode ser nulo.";
                      }

                      return null;
                    },
                  ),
                ),
                const SizedBox(height: 10),
                SizedBox(
                  width: 250,
                  child: TextFormField(
                    controller: tokenRegistroTls,
                    obscureText: false,
                    keyboardType: TextInputType.number,
                    textAlign: TextAlign.center,
                    textInputAction: TextInputAction.next,
                    maxLength: 16,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Token Registro Tls',
                    ),
                    validator: (value) {
                      if (_comExternaSelecionado == "TLS GWP (TLS Fiserv)" &&
                          value == null) {
                        return "Token não pode ser nulo.";
                      }
                      return null;
                    },
                  ),
                ),
                const SizedBox(
                  height: 10,
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Checkbox(
                        value: isCheckedDebito,
                        onChanged: (bool? value) {
                          setState(() {
                            isCheckedDebito = true;
                            isCheckedCredito = false;
                            isCheckedPIX = false;
                            isCheckedTodos = false;
                          });
                        }),
                    const Text('Débito'),
                    const SizedBox(
                      width: 1,
                    ),
                    Checkbox(
                        value: isCheckedCredito,
                        onChanged: (bool? value) {
                          setState(() {
                            isCheckedCredito = true;
                            isCheckedDebito = false;
                            isCheckedPIX = false;
                            isCheckedTodos = false;
                          });
                        }),
                    const Text('Crédito'),
                    const SizedBox(
                      width: 1,
                    ),
                    Checkbox(
                        value: isCheckedPIX,
                        onChanged: (bool? value) {
                          setState(() {
                            isCheckedPIX = true;
                            isCheckedTodos = false;
                            isCheckedDebito = false;
                            isCheckedCredito = false;
                          });
                        }),
                    const Text('PIX'),
                    const SizedBox(
                      width: 1,
                    ),
                    Checkbox(
                        value: isCheckedTodos,
                        onChanged: (bool? value) {
                          setState(() {
                            isCheckedTodos = true;
                            isCheckedPIX = false;
                            isCheckedDebito = false;
                            isCheckedCredito = false;
                          });
                        }),
                    const Text('Adm.'),
                  ],
                ),
                const SizedBox(
                  height: 10,
                ),
                SizedBox(
                  width: 200,
                  child: TextFormField(
                    controller: valorVenda,
                    obscureText: false,
                    keyboardType: TextInputType.number,
                    textAlign: TextAlign.center,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Valor',
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "Valor não pode ser nulo.";
                      }

                      if (double.parse(value.replaceAll(',', '.')) < 0) {
                        return "Valor não pode ser zero.";
                      }

                      return null;
                    },
                  ),
                ),
                const SizedBox(
                  height: 10,
                ),
                SizedBox(
                  width: 200,
                  child: OutlinedButton(
                      onPressed: () async {
                        if (formKey.currentState!.validate()) {
                          if (isCheckedCredito!) {
                            tipoPagamentoSelecionado = TipoPagtoEnum.credito;
                          } else if (isCheckedDebito!) {
                            tipoPagamentoSelecionado = TipoPagtoEnum.debito;
                          } else if (isCheckedPIX!) {
                            tipoPagamentoSelecionado = TipoPagtoEnum.pix;
                          } else if (isCheckedTodos!) {
                            tipoPagamentoSelecionado = TipoPagtoEnum.todos;
                          }

                          switch (_comExternaSelecionado) {
                            case 'Sem (apenas para SiTef dedicado)':
                              comexterna = ComExternaEnum.sem;
                            case 'TLS Software Express':
                              comexterna = ComExternaEnum.tlssoftwareexpress;
                            case 'TLS WNB Comnect':
                              comexterna = ComExternaEnum.tlswnbcomnect;
                            case 'TLS Gsurf':
                              comexterna = ComExternaEnum.tlsgsurf;
                            case 'TLS GWP (TLS Fiserv)':
                              comexterna = ComExternaEnum.tlsgwp;
                          }
                          realizarFuncao(TipoAcaoEnum.venda);
                          return;
                        }
                      },
                      child: const Text('Enviar Transação')),
                ),
                const SizedBox(
                  height: 10,
                ),
                SizedBox(
                  width: 200,
                  child: OutlinedButton(
                      onPressed: () {
                        if (formKey.currentState!.validate()) {
                          switch (_comExternaSelecionado) {
                            case 'Sem (apenas para SiTef dedicado)':
                              comexterna = ComExternaEnum.sem;
                            case 'TLS Software Express':
                              comexterna = ComExternaEnum.tlssoftwareexpress;
                            case 'TLS WNB Comnect':
                              comexterna = ComExternaEnum.tlswnbcomnect;
                            case 'TLS Gsurf':
                              comexterna = ComExternaEnum.tlsgsurf;
                            case 'TLS GWP (TLS Fiserv)':
                              comexterna = ComExternaEnum.tlsgwp;
                          }
                          realizarFuncao(TipoAcaoEnum.cancelamento);

                          return;
                        }
                      },
                      child: const Text('Cancelar Transação')),
                ),
                // const SizedBox(height: 10,),
                // SizedBox(width: 200,
                //   child: OutlinedButton(onPressed: (){
                //     if(formKey.currentState!.validate()){

                //       switch(_comExternaSelecionado){
                //         case 'Sem (apenas para SiTef dedicado)':
                //           comexterna = ComExterna.sem;
                //         case 'TLS Software Express':
                //           comexterna = ComExterna.tlssoftwareexpress;
                //         case 'TLS WNB Comnect':
                //           comexterna = ComExterna.tlswnbcomnect;
                //         case 'TLS Gsurf':
                //           comexterna = ComExterna.tlsgsurf;
                //         case 'TLS GWP (TLS Fiserv)':
                //           comexterna = ComExterna.tlsgwp;
                //       }
                //       realizarFuncao("pix");
                //       return;
                //     }
                //   }, child: const Text('PIX')),
                // ),
                const SizedBox(
                  height: 10,
                ),
                SizedBox(
                  width: 200,
                  child: OutlinedButton(
                      onPressed: () {
                        if (formKey.currentState!.validate()) {
                          switch (_comExternaSelecionado) {
                            case 'Sem (apenas para SiTef dedicado)':
                              comexterna = ComExternaEnum.sem;
                            case 'TLS Software Express':
                              comexterna = ComExternaEnum.tlssoftwareexpress;
                            case 'TLS WNB Comnect':
                              comexterna = ComExternaEnum.tlswnbcomnect;
                            case 'TLS Gsurf':
                              comexterna = ComExternaEnum.tlsgsurf;
                            case 'TLS GWP (TLS Fiserv)':
                              comexterna = ComExternaEnum.tlsgwp;
                          }
                          realizarFuncao(TipoAcaoEnum.reimpressao);
                          return;
                        }
                      },
                      child: const Text('ReImpressão')),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
