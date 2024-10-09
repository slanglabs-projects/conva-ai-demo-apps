//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

struct Utils {
    
    static func getDefaultAssistantData() -> [AssistantData] {
        
        let defaultAssistants: [AssistantData] = [
            AssistantData(appName: "assistant name", assistantId: "id", assistantVersion: "version", apiKey: "key", capabilityName: ["your_capability_name"], capabilityGroup: ["your_capability_group"])
            // Add more default assistants here
        ]
        return defaultAssistants
    }
    
    static func getDefaultHints() -> [String] {
            return [
                "Hey! How are you",
                "Good job",
                "Tell me something about you",
            ]
        }
}
