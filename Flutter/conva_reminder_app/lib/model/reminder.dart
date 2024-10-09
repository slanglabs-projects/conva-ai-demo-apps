import 'package:hive/hive.dart';
import 'package:uuid/uuid.dart'; // Import the uuid package
part 'reminder.g.dart';

@HiveType(typeId: 0)
class Reminder {
  @HiveField(0)
  String id; // Unique identifier

  @HiveField(1)
  String message;

  @HiveField(2)
  String reminderName;

  @HiveField(3)
  String reminderDate;

  @HiveField(4)
  String reminderTime;

  @HiveField(5)
  String reminderEmoji;

  @HiveField(6)
  bool reminderConfirmation;

  Reminder({
    required this.message,
    required this.reminderName,
    required this.reminderDate,
    required this.reminderTime,
    required this.reminderEmoji,
    required this.reminderConfirmation,
  }) : id = const Uuid().v4();
}
