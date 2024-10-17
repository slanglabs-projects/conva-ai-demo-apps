# ConvaAIWebViewDemo

## Description
ConvaAIWebViewDemo is a simple starter project designed to demonstrate how to integrate the ConvaAI SDK within a web view context. This app features a home URL and facilitates interaction with the ConvaAI Copilot, displaying responses directly in the web view. **Note: This is a demo app and not intended for actual use.**

This project serves as a foundational example for developers looking to incorporate AI-driven interactions into their web applications using the ConvaAI SDK.

## Features
- **Web View Integration**: The app launches a web view with a specified home URL, allowing users to interact with web content seamlessly.
  
- **ConvaAI Integration**: The app demonstrates how to trigger the ConvaAI Copilot and display its responses within the web view, enhancing user engagement through conversational AI.

## ConvaAI Integration

### ConvaAI Copilot SDK
The ConvaAI Copilot SDK provides a straightforward way to integrate a conversational assistant into your web view app. It manages interactions with the system and facilitates the delivery of AI-generated responses.

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

### Attach Web View
To attach the ConvaAI UI to your web view, pass the web view reference to the attach() API:

```
ConvaAICopilot.attach(activity)
```

*Note: You need to pass the web view where you want to display the ConvaAI interface.*

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
