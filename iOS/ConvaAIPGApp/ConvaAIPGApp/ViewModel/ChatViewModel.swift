//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

class ChatViewModel: ObservableObject {
    @Published var messages: [ChatInfo] = []
    @Published var messageText = ""
    @Published var selectedCapabilityName: String?
    @Published var selectedCapabilityGroup: String?
    @Published var relatedQueries: [String]?
    
    private let convaAIRepository = ConvaAICoreRepository()
    private var currentTask: Task<Void, Never>?

    func sendMessage() {
        guard !messageText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        
        // Check if there is an ongoing task and cancel it
        if currentTask != nil {
            // Remove any processing message from the UI
            DispatchQueue.main.async {
                self.messages.removeAll { $0.isProcessing }
            }
            currentTask?.cancel()
        }
        
        let newMessage = ChatInfo(message: messageText, isUser: true)
        DispatchQueue.main.async {
            self.messages.append(newMessage)
            self.messageText = ""
        }
        
        let processingMessage = ChatInfo(isUser: false, isProcessing: true)
        DispatchQueue.main.async {
            self.messages.append(processingMessage)
        }
        
        currentTask = Task {
            let chatResponse = await convaAIRepository.invokeCapability(
                input: messageText,
                capabilityName: selectedCapabilityName,
                capabilityGroup: selectedCapabilityGroup
            )
            
            if Task.isCancelled { return }

            DispatchQueue.main.async {
                self.messages.removeAll { $0.isProcessing }
                let responseMessage = ChatInfo(
                    message: chatResponse.message,
                    fullResponse: chatResponse.fullResponse,
                    isUser: false,
                    isProcessing: false,
                    parameters: chatResponse.params
                )
                self.relatedQueries = chatResponse.relatedQueries
                self.messages.append(responseMessage)
            }
        }
    }
    
    func sendHintMessage(hint: String) {
        messageText = hint
        sendMessage()
    }
    
    func initializeAssistant(_ assistantData: AssistantData) {
        convaAIRepository.initialize(
            id: assistantData.assistantId,
            key: assistantData.apiKey,
            version: assistantData.assistantVersion
        )
    }
    
    func clearState() {
        messages = []
        messageText = ""
        selectedCapabilityName = nil
        selectedCapabilityGroup = nil
    }
}
