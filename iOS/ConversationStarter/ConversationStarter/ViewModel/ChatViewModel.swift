//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

class ChatViewModel: ObservableObject {
    @Published var messages: [ChatInfo] = []
    @Published var messageText = ""
    @Published var relatedQueries: [String]?
    @Published var cStarterInput: String? = nil
    @Published var cStarterProcessing = false
    @Published var cStarterShowCheckmark = false
    @Published var cStarterShowCrosskmark = false
    
    private let convaAIRepository = ConvaAICoreRepository()
    private var currentTaskMessage: Task<Void, Never>?
    private var currentTaskCStarter: Task<Void, Never>?

    func sendMessage() {
        guard !messageText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        
        // Check if there is an ongoing task and cancel it
        if currentTaskMessage != nil {
            // Remove any processing message from the UI
            DispatchQueue.main.async {
                self.messages.removeAll { $0.isProcessing }
            }
            currentTaskMessage?.cancel()
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
        
        currentTaskMessage = Task {
            let finalInput = messageText + "? What should I reply to it?"
            let chatResponse = await convaAIRepository.invokeCapability(
                input: finalInput,
                capabilityName: "chat_reply_generation"
            )
            
            if Task.isCancelled { return }

            DispatchQueue.main.async {
                self.messages.removeAll { $0.isProcessing }
                let reply: String? = chatResponse.params?["reply"] as? String
                let responseMessage = ChatInfo(
                    message: reply ?? chatResponse.message,
                    isUser: false,
                    isProcessing: false,
                    parameters: chatResponse.params
                )
                self.relatedQueries = chatResponse.relatedQueries
                self.messages.append(responseMessage)
            }
        }
    }
    
    func processHintOrText(inputText: String) {
        guard !inputText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        
        cStarterInput = inputText
        messageText = ""
        cStarterProcessing = true
        
        if currentTaskCStarter != nil {
            currentTaskCStarter?.cancel()
        }
        
        currentTaskCStarter = Task {
            let chatResponse = await convaAIRepository.invokeCapability(
                input: inputText,
                capabilityName: "conversation_generation"
            )
            
            if Task.isCancelled { return }

            DispatchQueue.main.async {
                let conversations = chatResponse.params?["conversations"] as? [String]
                self.relatedQueries = conversations
                self.cStarterProcessing = false
                if chatResponse.isError || conversations == nil {
                    self.cStarterShowCrosskmark = true
                } else {
                    self.cStarterShowCheckmark = true
                }
                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                    self.cStarterShowCheckmark = false
                    self.cStarterShowCrosskmark = false
                }
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
        relatedQueries = nil
        cStarterInput = nil
        cStarterProcessing = false
        cStarterShowCheckmark = false
        cStarterShowCrosskmark = false
    }
}
