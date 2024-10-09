# Conversation Starter App

The **Conversation Starter App** is a smart chatting application that helps users generate conversation starters and replies when they’re stuck in a conversation. Powered by the **Conva.AI**, the app provides engaging prompts and contextually relevant replies, making conversations effortless and enjoyable.

## Screenshot

![App Screenshot](https://drive.google.com/uc?export=view&id=1fFrKIegzWUqdMZtZEzLl4jbKTL9T8yqN)
*Screenshot showing conversation starters, replies, hint chips, and chat interface*

## Key Features
- **Find Conversation Starters**: Get AI-generated prompts to begin any conversation seamlessly.
- **AI-powered Replies**: Stuck in a conversation? The app generates replies tailored to the context of your conversation.
- **Hint Chips for Conversations**: When the bottom sheet is opened and you type something, the app sends your input to the Conva.AI platform to find relevant conversation suggestions. These suggestions appear as hint chips. Clicking on a hint chip will initiate a real conversation with the selected response.
- **Seamless Chat Experience**: The app supports a fluid chat interface with AI-generated hints and replies.

## Technologies Used
- **Conva.AI SDK**: Powers the conversation starters, replies, and hint suggestions.
- **SwiftUI**: Ensures a modern, smooth user interface experience.
- **MVVM Architecture**: Keeps the code organized, scalable, and maintainable.


## Conva.AI Use Cases

This app utilizes the **Conva.AI Platform** to find conversation starters and replies. For more information about the Platform use cases, you can navigate to the following files in the project:

- **ConvaAICoreRepository.swift**: [Link to file](https://github.com/slanglabs-projects/conva-ai-demo-apps/blob/master/iOS/ConversationStarter/ConversationStarter/Model/Repository/ConvaAICoreRepository.swift)
- **ConvaAICoreImpl.swift**: [Link to file](https://github.com/slanglabs-projects/conva-ai-demo-apps/blob/master/iOS/ConversationStarter/ConversationStarter/Model/ConvaAI/ConvaAICoreImpl.swift)

These files contain the core logic for connecting to the Conva.AI platform, making API calls, and processing responses.

Note :- Please use your assistant credentials here to make it work :- [Link to file](https://github.com/slanglabs-projects/conva-ai-demo-apps/blob/master/iOS/ConversationStarter/ConversationStarter/Model/Utils.swift)

