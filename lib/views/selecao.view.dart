import 'package:flutter/material.dart';

class SelecaoView extends StatelessWidget {
  const SelecaoView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Opções L400'),
        centerTitle: true,
        backgroundColor: const Color(0xFF212121),     
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(width: 200,
              child: OutlinedButton(
                onPressed: (){
                  Navigator.of(context).pushNamed('/lerMifare');
                }, 
                child: const Text('Ler Mifare')),
            ),
            const SizedBox(height: 20,),
            SizedBox(width: 200,
              child: OutlinedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/printer');
                }, 
                child: const Text('Printer L400')
              ),
            ),
            const SizedBox(height: 20,),
            SizedBox(width: 200,
            child: OutlinedButton(onPressed: () {
                Navigator.of(context).pushNamed('/tef');
              }, child: const Text('TEF')),
            ),
            const SizedBox(height: 20,),
            SizedBox(width: 200,
            child: OutlinedButton(onPressed: () {
              Navigator.of(context).pushNamed('/outras');
              }, child: const Text('Outras')),
            ),
            const SizedBox(height: 20,),
            const SizedBox(width: 200,),
            const SizedBox(height: 20,),
          ],
        ),
      ),
    );
  }
}