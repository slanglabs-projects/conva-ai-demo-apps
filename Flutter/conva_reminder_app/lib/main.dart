import 'package:conva_reminder_app/ui/reminder_screen.dart';
import 'package:conva_reminder_app/viewmodel/reminder_view_model.dart';
import 'package:flutter/material.dart';
import 'package:hive_flutter/adapters.dart';
import 'package:provider/provider.dart';

import 'model/reminder.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Hive.initFlutter();
  Hive.registerAdapter(ReminderAdapter());
  await Hive.openBox<Reminder>('remindersBox');
  runApp(const ReminderApp());
}

class ReminderApp extends StatelessWidget {
  const ReminderApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => ReminderViewModel(),
      child: MaterialApp(
        home: ReminderScreen(),
      ),
    );
  }
}