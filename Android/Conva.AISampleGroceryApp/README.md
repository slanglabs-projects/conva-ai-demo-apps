# Slang - ConvaAI Sample Grocery App

### Description
Sample Grocery App is a demo application that simulates a real-world e-commerce grocery shopping experience. This app is designed to showcase how the ConvaAI SDK can be integrated into a grocery shopping platform, allowing developers to explore the features and capabilities of ConvaAI within an e-commerce context. 
Note: This is a demo app and not intended for actual use.

Within this app, ConvaAI is implemented to be triggered and utilized by users as they navigate through the app. 
Developers can use this project to gain insights into how to integrate the ConvaAI SDK into their own applications, ultimately enhancing customer interactions and experience.

### Features
ConvaAI Integration: The app demonstrates the integration of ConvaAI into a grocery shopping environment. Developers can experience how ConvaAI can be used to enhance the user experience by providing a seamless interface for searching, ordering, and interacting with the app’s features.

Developer-Friendly Implementation: The Sample Grocery App provides a hands-on example for developers, illustrating how to effectively incorporate the ConvaAI SDK into an e-commerce application. This can serve as a guide for enhancing customer experience through AI-driven capabilities within their own projects.

## ConvaAI Integration

## ConvaAI Copilot SDK

The ConvaAI Copilot SDK is a simple and easy-to-integrate tool that allows you to incorporate an Copilot into your app. It handles communication with the system and provides results. The ConvaAI Copilot SDK includes ASR, TTS, and UI components, enabling developers to use it without worrying about building and connecting these services.

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

To attach the ConvaAI UI to your app screen, pass the activity reference to the `attach()` API:

```
ConvaAICopilot.attach(activity)
```

**Note: In order to show the UI you have to pass the activity where you want to show the ConvaAI surface.**

### Set Listener to Get Response Data

To set a listener and get response data, use the following code:

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

Add the ConvaAICopilot core SDK dependency to the project:

```
implementation ("in.slanglabs.conva:conva-ai-copilot:1.0.0-beta")
```