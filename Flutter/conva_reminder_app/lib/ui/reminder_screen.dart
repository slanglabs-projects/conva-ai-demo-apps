import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../viewmodel/reminder_view_model.dart';
import 'component/reminder_list.dart';

class ReminderScreen extends StatelessWidget {
  final TextEditingController reminderController = TextEditingController();

  ReminderScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<ReminderViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Reminders'),
      ),
      body: Column(
        children: [
          Expanded(
            child: ReminderList(),
          ),

          // Message box/sheet above the input field
          if (viewModel.message.isNotEmpty)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.blue.shade50,
                  border: Border.all(color: Colors.blue, width: 1.5),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Row(
                  children: [
                    const Icon(Icons.info, color: Colors.blue),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        viewModel.message,
                        style: const TextStyle(color: Colors.blue, fontSize: 16),
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.close, color: Colors.blue),
                      onPressed: () {
                        viewModel.clearMessage();
                      },
                    ),
                  ],
                ),
              ),
            ),

          // Show progress indicator while sending
          if (viewModel.isLoading)
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: LinearProgressIndicator(
                color: Colors.blue,
              ),
            ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: reminderController,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      hintText: 'Enter reminder',
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                IconButton(
                  icon: const Icon(Icons.send),
                  onPressed: () {
                    if (!viewModel.isLoading) {
                      viewModel.submitReminderQuery(reminderController.text);
                      reminderController.clear();
                    }
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
