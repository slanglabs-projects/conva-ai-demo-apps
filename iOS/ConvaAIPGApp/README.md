# ConvaAIPGApp

The **ConvaAIPGApp** is a playground application designed to test the capabilities of the **Conva.AI Assistant**. This app allows users to interact with their custom assistants created in the Conva.AI studio. If an user does not create an assistant, default assistants are available for use.

![App Screenshot](https://drive.google.com/uc?id=1UxB17Ve9lG4Lu13PzAWzI0P9aWIjGb2T)
*Screenshots showing all screens in the ConvaAIPG app.*

## Getting Started

### Setup Instructions

1. **Create an Assistant**:
   - Go to the [Conva.AI Studio](https://studio.conva.ai/)
   - Create your custom assistant.
   - Navigate to the **Magic App tab** to generate a QR code for your assistant.

2. **Scan the QR Code**:
   - Once the QR code is generated, scan it using the ConvaAIPG app.

3. **Interact with Your Assistant**:
   - After scanning, you will land on the chat screen.
   - You can ask any questions related to your assistant, and it will respond accordingly.
   - If you prefer, you can use the default assistants provided within the app.

## Key Features
- **Playground for Testing**: Provides an interactive environment to test and experiment with your own assistant created in the Conva.AI studio.
- **Default Assistants**: Access pre-configured assistants if you do not create your own.
- **QR Code Scanning**: Scan QR codes to quickly access and interact with your assistant.
- **Interactive Chat Interface**: The chat interface provides all the parameters and response structure for your queries. It first displays the message and associated parameters. Clicking on the response box reveals the entire response JSON structure.

## Integrating Conva.AI Core SDK

To integrate the **Conva.AI Core SDK** into your project, follow these steps:

1. **Add SDK to Your Project**:
  
   - Add the following line to your `Podfile`:

     ```ruby
     pod 'ConvaAICore'
     ```

2. **Import the SDK**:
   - In your Swift files, import the SDK:

     ```swift
     import ConvaAICore
     ```

3. **Utilize SDK Features**:
   - You can now use the SDK's features, such as sending queries and receiving responses, within your application. Refer to the SDK [documentation](https://docs.conva.ai/) for detailed usage instructions.
   
    - For detailed guidance on how to initialize the SDK and make API calls, refer to the following resources:
        - **ConvaAICoreRepository.swift**: [Link to file](https://github.com/slanglabs/polyglot/blob/master/client/slang-ios-sample-apps/ConvaAIPGApp/ConvaAIPGApp/Model/ConvaAI/ConvaAICoreImpl.swift)
        - **ConvaAICoreImpl.swift**: [Link to file](https://github.com/slanglabs/polyglot/blob/master/client/slang-ios-sample-apps/ConvaAIPGApp/ConvaAIPGApp/Model/Repository/ConvaAICoreRepository.swift)


## Technologies Used
- **ConvaAICore SDK**: Provides the necessary functionalities for invoking the capability.
- **SwiftUI**: Used for building a modern, responsive user interface.
- **MVVM Architecture**: Ensures a clean and maintainable code structure.

## Additional Resources

For more information on the Conva.AI SDKs and how to create and manage assistants, please refer to the official [Conva.AI documentation](https://docs.conva.ai/).

