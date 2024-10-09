//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import ConvaAICore

class ConvaAICoreImpl {
    
    private var conversationHistory: String?
    
    func initialize(id: String, key: String, version: String) {
        ConvaAI.initialize(with: id, key: key, version: version)
    }
    
    func invokeCapability(_ input: String, capabilityName: String?, capabilityGroup: String?) async -> ConvaAICapability? {
        do {
            let response = try await ConvaAI.invokeCapability(
                with: input,
                capabilityName: capabilityName,
                capabilityGroup: capabilityGroup,
                context: ConvaAIContext(history: conversationHistory)
            )
            conversationHistory = response?.conversationHistory
            return response
        } catch {
            return nil
        }
    }
}
