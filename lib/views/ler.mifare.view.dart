import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class LerMifareView extends StatefulWidget {
  const LerMifareView({super.key});

  @override
  State<LerMifareView> createState() => _LerMifareViewState();
}

class _LerMifareViewState extends State<LerMifareView> {
  static const platform = MethodChannel('samples.flutter.dev/l400');
  String? resultado;

  Future<void> _leitorMifare() async {
    try {
      resultado = await platform.invokeMethod('leitorMifare');
      await _toastMensagem(resultado!.split('|')[1]);

    } on PlatformException catch (e) {
      debugPrint('retornou ${e.message}');
    }
  }

  Future<void> _toastMensagem(String mensagem) async {
    final snackBar = SnackBar(
        content: Text('Resposta: $mensagem', style: const TextStyle(color: Colors.deepOrangeAccent),),
        backgroundColor: const Color(0xFF424242),
        duration: const Duration(milliseconds: 2000),
        action: SnackBarAction(
          label: 'Ok',
          textColor: Colors.white60,
          backgroundColor: const Color(0xFF303030),
          onPressed: () {
            // Some code to undo the change.
          },
        ),
      );
      ScaffoldMessenger.of(context).showSnackBar(snackBar);
 }
 
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Ler Mifare'),
        centerTitle: true,
      ),
      body: Center(
        child: OutlinedButton(onPressed: () async {
          await _leitorMifare();
        }, child: const Text('Posicione o cartão para leitura.'),)
      ),
    );
  }
}