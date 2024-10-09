# ConvaAIReminder App

The **ConvaAIReminder App** is a Flutter-based application that allows users to set reminders by providing input in natural language. The app uses the **Conva.AI platform** to process the input and generate relevant reminder details. It follows the **MVVM architecture** for efficient app structuring and logic separation.

## Screenshot
![App Screenshot](https://drive.google.com/uc?export=view&id=1UsxV6UfXayWGMbbNcdBvEKFCXEmMmoCD)

## Features

- **Input query**: Users can set reminders using natural language queries like "Set a reminder for meeting after 3 hours."
- **Confirmation handling**: The app confirms the accuracy of reminder details such as the title, date, and time before setting a reminder.
- **Reverse questioning**: If not all details are provided, the app will ask reverse questions to gather the required information to set the reminder.
- **Emoji representation**: Reminders come with an emoji to visually represent the reminder type (e.g., 📅 for meetings).
- **MVVM Architecture:** Utilizes the Model-View-ViewModel architecture for better separation of concerns and maintainability.

## Example Queries and Responses

### 1. Query: Set a reminder for a meeting after 3 hours

```json
{
    "reminder_confirmation": true,
    "reminder_title": "meeting",
    "reminder_date": "2024-09-30",
    "reminder_time": "15:58",
    "reminder_emoji": "📅"
}
```

### 2. Query: Set a reminder for a meeting (without specifying the time)

If the user provides a reminder title without a specific time, the app will ask a follow-up question to gather the missing information.

#### Initial Query:

```json
{
    "reminder_confirmation": false,
    "reminder_title": "meeting",
    "reminder_date": "2024-09-30",
    "reminder_time": "[30 minutes from now]",
    "reminder_emoji": "📅"
}
```

Since the reminder_time was not provided, the app responds with a follow-up question:
"When would you like to be reminded for the meeting?"

After User's Response: "After 2 hours"

```json
{
    "reminder_confirmation": true,
    "reminder_title": "meeting",
    "reminder_date": "2024-09-30",
    "reminder_time": "14:01",
    "reminder_emoji": "📅"
}
```
Here, reminder_confirmation: true indicates that the reminder has been successfully set with the updated time provided by the user. The reminder is now confirmed for 14:01 on 2024-09-30 with the title "meeting" and an associated emoji 📅.

## Conversation History

This app utilizes the **ConvaAICore SDK** to facilitate setting reminders while maintaining a **conversation history**. The conversation history allows the app to handle cases where user inputs may be incomplete (e.g., when time is not provided for a reminder). In such cases, the **Conva.AI** platform uses the conversation history to perform reverse questioning and seek missing details.

The following code snippet demonstrates how the app maintains and utilizes the conversation history:

```dart
String? _conversationHistory;

Future<Response?> invokeCapabilityWithName(
      String input, String capabilityName) async {
    try {
      Response response = await ConvaAI.invokeCapabilityWithName(
          input: input, capability: capabilityName, 
          context: ConvaAIContext(history: _conversationHistory));
      _conversationHistory = response.history;  // Updating the conversation history
      return response;
    } on Exception {
      return null;
    }
  }
 ```

## Conva.AI Use Cases

This app utilizes the **ConvaAICore SDK** to facilitate the setting of reminders based on user-defined conditions. For more information about the SDK use cases, you can navigate to the following files in the project:

- [ConvaAICoreRepository](https://github.com/slanglabs-projects/conva-ai-demo-apps/blob/master/Flutter/conva_reminder_app/lib/model/conva_ai_core_repository.dart): Implements the functionality for processing responses related to setting reminders.
- [ConvaAICoreImpl.dart](https://github.com/slanglabs-projects/conva-ai-demo-apps/blob/master/Flutter/conva_reminder_app/lib/model/conva/conva_ai_core.dart): Contains the core logic for connecting to the Conva.AI platform and making API calls.

These files are essential for handling user input analysis and generating accurate reminder settings based on the provided parameters.

## Additional Resources

For more information on the Conva.AI SDKs and how to create and manage assistants, please refer to the official [Conva.AI documentation](https://docs.conva.ai/).