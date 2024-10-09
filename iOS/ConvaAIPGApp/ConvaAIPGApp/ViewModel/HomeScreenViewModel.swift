//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import SwiftUI

class HomeScreenViewModel: ObservableObject {
    @Published var selectedAssistantData: AssistantData?
    @Published var navigateToChat = false

    func navigateToChatScreen(_ assistantData: AssistantData) {
        selectedAssistantData = assistantData
        navigateToChat = true
    }
    
    func setSelectedAssistantData(_ data: AssistantData) {
        selectedAssistantData = data
    }
    
    func clearState() {
        selectedAssistantData = nil
        navigateToChat = false
    }
}

