//
//  ConversationStarterApp.swift
//  ConversationStarter
//
//  Created by Slang on 20/09/24.
//

import SwiftUI

@main
struct ConversationStarterApp: App {
    var body: some Scene {
        WindowGroup {
            ChatScreen(assistantData: Utils.getAssistantData())
        }
    }
}
