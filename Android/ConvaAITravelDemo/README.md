# Slang - ConvaAI Travel Demo

### Description
ConvaAI Travel Demo App is a demo application that shows how a travel search platform works, featuring ConvaAI integration. 
Note: This is a demo app, not for actual use.

In this app, users can search for buses using voice or text inputs powered by ConvaAI. It provides developers with a clear example of how to enhance travel apps with AI-driven search features.

### Features
ConvaAI Integration: Experience travel search through natural language, making it easy to find buses.
Developer-Friendly: Offers a simple example of integrating ConvaAI into a travel app to improve user experience.

## ConvaAI Integration

## ConvaAI Copilot SDK

The ConvaAI Copilot SDK is a simple and easy-to-integrate tool that allows you to incorporate an Copilot into your app. It handles communication with the system and provides results. The ConvaAI Copilot SDK includes ASR, TTS, and UI components, enabling developers to use it without worrying about building and connecting these services.

### ConvaAI Copilot Initialization

To initialize the ConvaAI Copilot SDK, use the following code:

```
 ConvaAI.init(
            BuildConfig.ASSISTANT_ID,
            BuildConfig.API_KEY,
            BuildConfig.COPILOT_VERSION,
            activity.application
        )
        val options = ConvaAIOptions.Builder()
            .setCapabilityHandler(object : ConvaAIHandler {
                override fun onCapability(
                    response: ConvaAIResponse,
                    interactionData: ConvaAIInteraction,
                    isFinal: Boolean
                ) {
                    if (isFinal) {
                        _copilotResponseState.value = response
                    }
                }
            })
            .setListener(object : ConvaAICopilot.Listener {
                override fun onASRPermissionDenied() {}

                override fun onASRPermissionGranted() {}

                override fun onAppBackgrounded() {}

                override fun onAppForegrounded() {}

                override fun onInitializationFailed(e: ConvaAICopilot.InitializationError?) {}

                override fun onInitialized(appConfigs: ConvaAIAppConfigs) {
                    _isCopilotInitialized.value = true
                }

                override fun onOnboardingFailure() {}

                override fun onOnboardingSuccess() {}

                override fun onSessionEnd(isCanceled: Boolean) {}

                override fun onSessionStart(isVoice: Boolean) {}
            })
            .build()
        ConvaAICopilot.setup(options)
```

### Start Conversation / Invoke Copilot

To start a conversation with the Copilot, use the following code:

```
ConvaAICopilot.startConversation()
```

### Show Activity
To display the ConvaAI UI, pass the activity you want to show the ConvaAI surface on:

```
ConvaAICopilot.attach(activity)
```

**Note: In order to show the UI you have to pass the activity where you want to show the ConvaAI surface.**

### Set Listener to Get Response Data

To set a listener and get response data, use the following code:

```
.setCapabilityHandler(object : ConvaAIHandler {
                override fun onCapability(
                    response: ConvaAIResponse,
                    interactionData: ConvaAIInteraction,
                    isFinal: Boolean
                ) {
                    if (isFinal) {
                        // Handle your response
                    }
                }
            })
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