
import 'package:conva_reminder_app/viewmodel/reminder_view_model.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class ReminderList extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Consumer<ReminderViewModel>(
      builder: (context, viewModel, child) {
        return ListView.builder(
          itemCount: viewModel.reminders.length,
          itemBuilder: (context, index) {
            final reminder = viewModel.reminders[index];
            return Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 6.0),
              child: Card(
                elevation: 2,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                  side: BorderSide(color: Colors.grey.shade300, width: 1),
                ),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue.shade100,
                    child: Text(
                      reminder.reminderEmoji,
                      style: TextStyle(fontSize: 24),
                    ),
                  ),
                  title: Text(
                    reminder.reminderName,
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  subtitle: Row(
                    children: [
                      Text(
                        reminder.reminderDate,
                        style: TextStyle(fontSize: 14, color: Colors.grey.shade600),
                      ),
                      SizedBox(width: 10),
                      Text(
                        reminder.reminderTime,
                        style: TextStyle(fontSize: 14, color: Colors.grey.shade600),
                      ),
                    ],
                  ),
                  trailing: IconButton(
                    icon: Icon(Icons.delete, color: Colors.redAccent),
                    onPressed: () {
                      viewModel.deleteReminder(reminder);
                    },
                  ),
                ),
              ),
            );
          },
        );
      },
    );
  }
}
