import 'package:conva_ai_split_app/ui/split_manager_home_page.dart';
import 'package:conva_ai_split_app/viewModel/split_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(const SplitManagerApp());
}

class SplitManagerApp extends StatelessWidget {
  const SplitManagerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => SplitViewModel(),
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Split Manager',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: const SplitManagerHomePage(),
      ),
    );
  }
}
