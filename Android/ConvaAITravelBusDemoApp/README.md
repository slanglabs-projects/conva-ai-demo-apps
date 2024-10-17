# Slang - ConvaAI Travel Bus Demo App

## Description
The ConvaAI Travel Bus Demo App is a demonstration application designed to simulate a real-world e-commerce bus ticket booking experience. This app showcases how the ConvaAI SDK can be integrated into a travel platform, allowing developers to explore the features and capabilities of ConvaAI in the context of bus travel. 
**Note: This is a demo app and not intended for actual use.**

Within this app, ConvaAI is implemented to enhance user interactions as they navigate through the bus search and booking processes. Developers can use this project as a guide to integrate the ConvaAI SDK into their own applications, ultimately improving customer interactions and experiences.

## Features
- **ConvaAI Integration**: The app demonstrates the integration of ConvaAI within a travel context, allowing users to search for bus routes, view schedules, and book tickets through a conversational interface.
  
- **Developer-Friendly Implementation**: The ConvaAI Travel Bus Demo App serves as a hands-on example for developers, illustrating how to effectively incorporate the ConvaAI SDK to enhance customer experience through AI-driven interactions.

## ConvaAI Integration

### ConvaAI Copilot SDK
The ConvaAI Copilot SDK simplifies the integration of a conversational assistant into your app. It manages communication with the system and provides actionable results. The SDK includes ASR, TTS, and UI components, enabling developers to focus on enhancing the user experience without the complexity of backend integrations.

### ConvaAI Copilot Initialization
To initialize the ConvaAI Copilot SDK, use the following code:

```
ConvaAI.init(assistantID, assistantKey, assistantVersion, application)

val options = ConvaAIOptions.Builder()
    .setCapabilityHandler(getConvaAIHandlerAction())
    .setListener(getConvaAICopilotListener())
    .build()
ConvaAICopilot.setup(options)
```

### Start Conversation / Invoke Copilot
To start a conversation with the Copilot, use the following code:

```
ConvaAICopilot.startConversation()
```

### Attach Activity
To attach the ConvaAI UI to your app screen, pass the activity reference to the attach() API:

```
ConvaAICopilot.attach(activity)
```

*Note: You need to pass the activity where you want to display the ConvaAI interface.*

### Set Listener to Get Response Data
To set a listener for response data, use the following code:

```
private fun getConvaAIHandlerAction() : ConvaAIHandler {
    return object : ConvaAIHandler {
        override fun onCapability(response: ConvaAIResponse, interactionData: ConvaAIInteraction, isFinal: Boolean) {
            if (isFinal) listener.onResponse(executePostProcessing(response))
        }
    }
}
```

### Add Maven Repository
Add the path to the Slang Maven repository to your Kotlin Script top-level Gradle file:

```
allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://gitlab.com/api/v4/projects/25706948/packages/maven")
    }
}

```

### Add Dependency
Add the ConvaAICopilot core SDK dependency to your project:

```
implementation ("in.slanglabs.conva:conva-ai-copilot:1.0.0-beta")
```