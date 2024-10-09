import 'package:conva_ai_core/conva_ai_core.dart';
import 'package:conva_reminder_app/model/reminder.dart';
import 'conva/conva_ai_core.dart';

class ConvaAICoreRepository {
  final ConvaAICore _impl = ConvaAICore();
  void initialize() {
    _impl.initialize("your_assistant_id",
        "your_api_key", "latest");
  }

  Future<Reminder?> invokeCapabilitySplit(String input, String capabilityName) async {
    try {
      Response? response =
      await _impl.invokeCapabilityWithName(input, capabilityName);
      if (response != null) {
        var reminderName = response.params!['reminder_title'];
        var reminderDate = response.params!['reminder_date'];
        var reminderTime = response.params!['reminder_time'];
        var reminderEmoji = response.params!['reminder_emoji'];
        var reminderConfirmation = response.params!['reminder_confirmation'];
        return Reminder(message: response.message ?? "", reminderName: reminderName, reminderDate: reminderDate, reminderTime: reminderTime, reminderEmoji: reminderEmoji, reminderConfirmation: reminderConfirmation);
      }
      else {
        return null;
      }

    } catch (e) {
      return null;
    }
  }
}
