# Slang - ConvaAI Android PG App

### Description

Slang - ConvaAI Android PG App is a developer-friendly project designed to help developers understand how to integrate the ConvaAI SDKs into their Android applications. This app allows users to scan a QR code created in Magic Console to test both the ConvaAI Copilot and ConvaAI Core SDKs. In addition to the Copilot and Core SDKs, the app also utilizes the ConvaAI TTS (Text-to-Speech) and ASR (Automatic Speech Recognition) SDKs to support the Core SDK.

### Features

**Copilot Mode:** Initializes the Copilot SDK and takes you to a screen similar to chat interface where the response can be seen in the form of a message. You can copy the response and invoke the Copilot SDK using the trigger.

**Headless SDK Mode:** Initializes the Core SDK along with the TTS and ASR SDKs, providing a chat interface where you can interact with the Assistant. You can type in the text box or use the ConvaAI ASR service to convert speech to text. The Assistant's response, spoken by the ConvaAI TTS Server, can be tapped to reveal the actual response JSON, stopping the TTS from speaking. The chat interface also allows you to choose the capability group or specific capability to use.

## ConvaAI Integration

## ConvaAI Copilot SDK

The ConvaAI Copilot SDK is a simple and easy-to-integrate tool that allows you to incorporate an Copilot into your app. It handles communication with the system and provides results. The ConvaAI Copilot SDK includes ASR, TTS, and UI components, enabling developers to use it without worrying about building and connecting these services.

### ConvaAI Copilot Initialization

To initialize the ConvaAI Copilot SDK, use the following syntax and app code:

#### Syntax:

```
ConvaAI.init(
    id = "replace this string with your_assistant_id",
    key = "replace this string with your_api_key",
    version = "LATEST", // this is a special tag to indicate 
                        // the latest version of the Assistant
    application = applicationContext
);

val options = ConvaAIOptions.Builder()
    .setCapabilityHandler(object : ConvaAIHandler {
        override fun onCapability(
            response: ConvaAIResponse, 
            interactionData: ConvaAIInteraction, 
            isFinal: Boolean) {
            // Handle the response from the assistant
        }
    })
    .setSuggestionHandler(object : ConvaAISuggestionHandler {
        override fun onSuggestion(suggestion: ConvaAISuggestion) {
            // Handle the selected suggestion
        }
    })
    .build()
    
ConvaAICopilot.setup(options)
```

#### App Code:

```kotlin
// Initialize the ConvaAICore with provided credentials
ConvaAI.init(assistantID, assistantKey, assistantVersion, this.application)

// Build the options for setting up ConvaAI
val options = ConvaAIOptions.Builder()
    .setListener(getConvaAICopilotListener())
    .setCapabilityHandler(getConvaAICopilotAction())
    .setInputListener(getConvaAIInputListener())

options.setStartActivity(startActivity)

// Setup the ConvaAI with the built options
ConvaAICopilot.setup(options.build())
```

### Start Conversation / Invoke Copilot

To start a conversation with the Copilot, use the following syntax and app code:

#### Syntax:

```
ConvaAICopilot.startConversation();
```

#### App Code:

```kotlin
ConvaAICopilot.startConversation()
```

### Show Activity

To display the ConvaAI UI, pass the activity you want to show the ConvaAI surface on:

#### Syntax:

```
ConvaAICopilot.builtinUI.show(activity)
```

#### App Code:

```kotlin
ConvaAICopilot.builtinUI.show(activity)
```

**Note: In order to show the UI you have to pass the activity where you want to show the ConvaAI surface.**

### Set Listener to Get Response Data

To set a listener and get response data, use the following syntax and app code:

#### Syntax:

```
interface ConvaAIHandler {
        override fun onCapability(
            response: ConvaAIResponse, 
            interactionData: ConvaAIInteraction, 
            isFinal: Boolean) {
            // Handle the response from the assistant
        }
    }
```

#### App Code:

```kotlin
private fun getConvaAICopilotAction() : ConvaAIHandler {
    return object : ConvaAIHandler {
        /**
         * Handles the capability response from the ConvaAICopilot.
         *
         * @param response The response from the ConvaAICopilot.
         * @param interactionData The interaction data from the ConvaAICopilot, which you can use to interact with the ConvaAI Copilot UI.
         * @param isFinal Indicates if the response is final.
         */
        override fun onCapability(response: ConvaAIResponse, interactionData: ConvaAIInteraction, isFinal: Boolean) {
            if (isFinal) {
                listener.onResponse(
                    message = response.message,
                    params = response.params,
                    jsonString = response.responseString,
                    capability = response.capability
                )
            }
        }
    }
}
```

### Add Maven Repository

Add the path to the Slang Maven repository to your Kotlin Script top-level Gradle file:

#### Syntax:

```
allprojects {  
   repositories {        
       maven { url "https://gitlab.com/api/v4/projects/25890120/packages/maven" }  
   }
}
```

#### App Code:

```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://gitlab.com/api/v4/projects/25890120/packages/maven")
    }
}

```

### Add Dependency

Add the ConvaAICopilot core SDK dependency to the project:

#### Syntax:

```
dependencies {
    implementation 'in.slanglabs.conva:conva-ai-copilot:1.0.0-beta'
}
```

#### App Code:

```kotlin
implementation ("in.slanglabs.conva:conva-ai-copilot:1.0.0-beta")
```

## Core SDK

The Core SDK provides developers with an API to interact with the assistant and get results, allowing them to build their own ideas and business logic around it.

### Core SDK Initialization

To initialize the Core SDK, use the following syntax and app code:

#### Syntax:

```
ConvaAI.init(
    id = "replace this string with your_assistant_id",
    key = "replace this string with your_api_key",
    version = "LATEST", // this is a special tag to indicate 
                        // the latest version of the Assistant
    application = applicationContext
);
```

#### App Code:

```kotlin
// Initialize the ConvaAICore with provided credentials
ConvaAI.init(assistantID, assistantKey,assistantVersion,mApplication)
```

### Single Shot Response

To invoke a single-shot response, use the following syntax and app code:

#### Syntax:

```
val response = ConvaAI.invokeCapability(
    input = "book a bus ticket from bangalore to chennai tomorrow at 2pm",
)
```

#### App Code:

```kotlin
val response = ConvaAI.invokeCapability(
    input = text, capabilityGroup = capabilityGroupSelected, context = ConvaAIContext(history = conversationHistory)
)
```

### Single Shot Response with Capability Name

To invoke a single-shot response with capability name, use the following syntax and app code:

#### Syntax:

```
val response = ConvaAI.invokeCapabilityWithName(
    input = "book a bus ticket from Bangalore to chennai tommorrow at 2pm", 
    capability = "ticket_booking"
);
```

#### App Code:

```kotlin
val response = ConvaAI.invokeCapabilityWithName(
    input = text, capability = capabilitySelected, context = ConvaAIContext(history = conversationHistory)
)
```

### Streaming Response
To invoke a streaming response, use the following syntax and app code:

#### Syntax:

```
ConvaAI.invokeCapability(
    input = "Hello, how are you?",
    listener = object : ResponseListener {
        override fun onResponse(response: Response, isFinal: Boolean) {
            // Check for is_final
            if (isFinal) {
                // Handle the final response
            }
        }
        override fun onError(error: Throwable) {
            // Handle the error
        }
    }
);
```

#### App Code:

```kotlin
ConvaAI.invokeCapability(
    input = text,
    capabilityGroup = capabilityGroupSelected,
    listener = object : ResponseListener {
        override fun onResponse(response: Response, isFinal: Boolean) {
            conversationHistory = response.history
            responseCallBack.onResponseStream(
                message = response.message,
                params = response.params,
                jsonString = response.responseString,
                capability = response.capability,
                isFinal = response.isFinal
            )
        }

        override fun onError(e: Exception) {
            // Handle error
            throw e
        }
    }
)
```

### Streaming Response with Capability Name 
To invoke a streaming response, use the following syntax and app code:

#### Syntax:

```
ConvaAI.invokeCapabilityWithName(
    input = "Hello, how are you?",
    listener = object : ResponseListener {
        override fun onResponse(response: Response, isFinal: Boolean) {
            // Check for is_final
            if (isFinal) {
                // Handle the final response
            }
        }
        override fun onError(error: Throwable) {
            // Handle the error
        }
    }
);
```

#### App Code:

```kotlin
ConvaAI.invokeCapabilityWithName(
    input = text,
    capability = capabilitySelected,
    listener = object : ResponseListener {
        override fun onResponse(response: Response, isFinal: Boolean) {
            conversationHistory = response.history
            responseCallBack.onResponseStream(
                message = response.message,
                params = response.params,
                jsonString = response.responseString,
                capability = response.capability,
                isFinal = response.isFinal
            )
        }

        override fun onError(e: Exception) {
            // Handle error
            throw e
        }
    }
)
```

### Add Maven Repository

Add the path to the Slang Maven repository to your Kotlin Script top-level Gradle file:

#### Syntax:

```
allprojects {  
   repositories {        
       maven { url "https://gitlab.com/api/v4/projects/25706948/packages/maven" }  
   }
}
```

#### App Code:

```kotlin
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

Add the ConvaAICore core SDK dependency to the project:

#### Syntax:

```
dependencies {
    implementation 'in.slanglabs.conva:conva-ai-core:1.0.1-beta'
}
```

#### App Code:

```kotlin
implementation ("in.slanglabs.conva:conva-ai-core:1.0.1-beta")
```