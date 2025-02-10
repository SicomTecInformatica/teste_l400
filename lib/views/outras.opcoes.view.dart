import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class OutrasOpcoesView  extends StatefulWidget {
  const OutrasOpcoesView ({super.key});

  @override
  State<OutrasOpcoesView> createState() => _OutrasOpcoesViewState();
}

class _OutrasOpcoesViewState extends State<OutrasOpcoesView> {
  
  static const platform = MethodChannel('samples.flutter.dev/l400');
  String? resultado;

  Future<void> _numeroSerialPOS() async {
    try {
      resultado = await platform.invokeMethod('NumeroSerialPOS');
      await _toastMensagem(resultado!.split('|')[2]);
    }on PlatformException catch (e) {
      await _toastMensagem("retornou ${e.message}");
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
        title: const Text('Outras funções'),
        centerTitle: true,
        backgroundColor: const Color(0xff212121),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              width: 200,
              child: OutlinedButton(
                onPressed: () async{
                  await _numeroSerialPOS();
                }, 
                child: const Text('Obtem numero serial')
                ),
            )
          ],
        ),
      ),
    );
  }
}