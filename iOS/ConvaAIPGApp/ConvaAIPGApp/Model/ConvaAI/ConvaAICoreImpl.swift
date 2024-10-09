//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import ConvaAICore

class ConvaAICoreImpl {
    
    func initialize(id: String, key: String, version: String) {
        ConvaAI.initialize(with: id, key: key, version: version)
    }
    
    func invokeCapability(_ input: String, capabilityName: String?, capabilityGroup: String?) async -> ConvaAICapability? {
        do {
            let response = try await ConvaAI.invokeCapability(with: input, capabilityName: capabilityName, capabilityGroup: capabilityGroup)
            return response
        } catch {
            return nil
        }
    }
}
