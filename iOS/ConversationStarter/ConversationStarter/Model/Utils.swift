//
//  Utils.swift
//  ConversationStarter
//
//  Created by Slang on 20/09/24.
//

import Foundation

struct Utils {
    
    static func getAssistantData() -> AssistantData {
        return AssistantData(
            // User your credentials here
            appName: "Assistant name",
            assistantId: "id",
            assistantVersion: "version", // You can also use "latest" as version to use the latest version of your assistant
            apiKey: "key"
        )
    }
    
    static func getDefaultHints() -> [String] {
        return [
            "Hey! How are you",
            "Good job",
            "What's the plan for weekend?",
            "Do you like watching cricket?"
        ]
    }
    
    static func getCStarterDefaultHints() -> [String] {
        return [
            "What's your all-time favorite movie and why?",
            "Provide more conversation ideas related to films",
            "We are talking about football and I'm stuck in between. Suggest some replies",
        ]
    }
}
