# Slang - ConvaAI Android PG App

### Description

Slang - ConvaAI Android PG App is a developer-friendly project designed to help developers understand how to integrate the ConvaAI SDKs into their Android applications. This app allows users to either scan a QR code created in the console or use the default assistant to test both the ConvaAI Assistant and ConvaAI Core SDKs. In addition to the Assistant and Core SDKs, the app also utilizes the ConvaAI TTS (Text-to-Speech) and ASR (Automatic Speech Recognition) SDKs to support the Core SDK.

### Features

**Assistant Mode:** Initializes the Assistant SDK and takes you to a screen with a box displaying the SDK's response. You can copy the response and invoke the Assistant SDK using a button.

**Headless SDK Mode:** Initializes the Core SDK along with the TTS and ASR SDKs, providing a chat interface where you can interact with the assistant. You can type in the text box or use the ConvaAI ASR service to convert speech to text. The assistant's response, spoken by the ConvaAI TTS Server, can be tapped to reveal the actual response JSON, stopping the TTS from speaking. The chat interface also allows you to choose the capability group and specific capability to use.

## ConvaAI Integration

## ConvaAI Assistant SDK

The ConvaAI Assistant SDK is a simple and easy-to-integrate tool that allows you to incorporate an assistant into your app. It handles communication with the system and provides results. The ConvaAI Assistant SDK includes ASR, TTS, and UI components, enabling developers to use it without worrying about building and connecting these services.

### ConvaAI Assistant Initialization

To initialize the ConvaAI Assistant SDK, use the following syntax and app code:

#### Syntax:

```
void initialize(ConvaAIOptions options)
```

#### App Code:

```kotlin
val options = ConvaAIOptions.Builder()
    .setApplication(this.mApplication)
    .setAssistantId(assistantID)
    .setAPIKey(assistantKey)
    .setUIProvider(ConvaAIRichUI())
    .setAssistantVersion(assistantVersion)
    .setEnvironment(ConvaAIAssistant.Environment.PRODUCTION)
    .setListener(getConvaAIAssistantListener())
    .setAppAction(getConvaAIAssistantAction())

if (startActivity != null) {
    options.setStartActivity(startActivity)
}

ConvaAIAssistant.initialize(options.build())
```

### Start Conversation / Invoke Assistant

To start a conversation with the assistant, use the following syntax and app code:

#### Syntax:

```
void startConversation()
```

#### App Code:

```kotlin
ConvaAIAssistant.startConversation()
```

### Show Activity

To display the ConvaAI UI, pass the activity you want to show the ConvaAI surface on:

#### Syntax:

```
void show(Activity activity)
```

#### App Code:

```kotlin
ConvaAIAssistant.builtinUI.show(activity)
```

**Note: In order to show the UI you have to pass the activity where you want to show the ConvaAI surface.**

### Set Listener to Get Response Data

To set a listener and get response data, use the following syntax and app code:

#### Syntax:

```
interface ConvaAIAction {
  void onCapability(ConvaAIResponse response, boolean isFinal);
}
```

#### App Code:

```kotlin
val options = ConvaAIOptions.Builder()
    .setAppAction(object : ConvaAIAction {
        override fun onCapability(response: ConvaAIResponse, isFinal: Boolean) {
            if (isFinal) listener.onResponse(response.message, response.responseString)
        }
    })
    .build()
ConvaAIAssistant.initialize(options)
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

Add the ConvaAIAssistant core SDK dependency to the project:

#### Syntax:

```
dependencies {  
   implementation 'in.slanglabs.conva:conva-ai-assistant:0.1.0'
}
```

#### App Code:

```kotlin
implementation ("in.slanglabs.conva:conva-ai-assistant:0.5.1")
```

## Core SDK

The Core SDK provides developers with an API to interact with the assistant and get results, allowing them to build their own ideas and business logic around it.

### Core SDK Initialization

To initialize the Core SDK, use the following syntax and app code:

#### Syntax:

```
void initialize(String id, String key, String version)
```

#### App Code:

```kotlin
override fun initialiseConvaAI(
    assistantID: String,
    assistantKey: String,
    assistantVersion: String
) {
    if (isSDKInit) return

    ConvaAI.init(assistantID, assistantKey, assistantVersion, mApplication)
    isSDKInit = true
}
```

### Single Shot Response

To invoke a single-shot response, use the following syntax and app code:

#### Syntax:

```
Response invokeCapability(String input, @Nullable String capability, @Nullable String context)
```

#### App Code:

```kotlin
val response = ConvaAI.invokeCapability(
    input = text, capability = capabilitySelected, history = conversationHistory
)
```

### Streaming Response
To invoke a streaming response, use the following syntax and app code:

#### Syntax:

```
void invokeCapability(String input, @Nullable String capability, @Nullable String context, ResponseListener listener)
interface ResponseListener {
  void onResponse(Response response);
  void onError(Exception e);
}
```

#### App Code:

```kotlin
ConvaAI.invokeCapability(
    input = text,
    capability = capabilitySelected,
    listener = object : ResponseListener {
        override fun onResponse(response: Response) {
            // Handle response
        }

        override fun onError(e: Exception) {
            // Handle error
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

Add the ConvaAIAssistant core SDK dependency to the project:

#### Syntax:

```
dependencies {  
   implementation 'in.slanglabs.conva:conva-ai-assistant:0.1.0'
}

```

#### App Code:

```kotlin
implementation ("in.slanglabs.conva:conva-ai-core:0.1.0")
```
