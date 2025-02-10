import 'package:flutter/material.dart';

class CustomDialogComponent extends StatelessWidget {
  final String msg;
  final String titulo;
  const CustomDialogComponent({super.key, required this.msg, required this.titulo,});

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(titulo),
      content: SizedBox(
        height: 100,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(msg),
          ],
        ),
      ),
    );
  }
}
