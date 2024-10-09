
import 'package:flutter/cupertino.dart';
import 'package:hive/hive.dart';
import '../model/conva_ai_core_repository.dart';
import '../model/reminder.dart';

class ReminderViewModel extends ChangeNotifier {
  final ConvaAICoreRepository convaAICoreRepository = ConvaAICoreRepository();
  bool isLoading = false;
  List<Reminder> reminders = [];
  String message = '';
  final Box<Reminder> reminderBox = Hive.box<Reminder>('remindersBox');

  ReminderViewModel() {
    convaAICoreRepository.initialize();
    loadRemindersFromDatabase();
  }

  Future<void> submitReminderQuery(String input) async {
    message = '';
    isLoading = true;
    notifyListeners();

    try {
      Reminder? reminder = await convaAICoreRepository.invokeCapabilitySplit(input, "upcoming_task_reminder");
      if (reminder != null && reminder.reminderConfirmation == true) {
        addReminder(reminder);
      }
      message = reminder?.message ?? "Sorry there was an error while processing your request";
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  void addReminder(Reminder reminder) {
    reminders.add(reminder);
    saveReminderToDatabase(reminder);
  }

  void deleteReminder(Reminder reminder) {
    deleteReminderFromDatabase(reminder);
    notifyListeners();
  }

  void saveReminderToDatabase(Reminder reminder) {
    reminderBox.add(reminder);
  }

  void deleteReminderFromDatabase(Reminder reminder) {
    final index = reminders.indexWhere((r) => r.id == reminder.id);
    reminderBox.deleteAt(index);
    reminders.removeAt(index);
  }

  void loadRemindersFromDatabase() {
    reminders = reminderBox.values.toList();
    notifyListeners();
  }

  void clearMessage() {
    message = '';
  }
}