//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import ConvaAICore

class ConvaAICoreRepository {
    
    private let convaAICoreImpl = ConvaAICoreImpl()
    
    func initialize(id: String, key: String, version: String) {
        convaAICoreImpl.initialize(id: id, key: key, version: version)
    }
    
    func invokeCapability(input: String, capabilityName: String? = nil, capabilityGroup: String? = nil) async -> ChatResponse {
        let response = await convaAICoreImpl.invokeCapability(input, capabilityName: capabilityName, capabilityGroup: capabilityGroup)
        
        guard let response = response else {
            return ChatResponse(
                message: "Sorry! There was an error processing your request"
            )
        }
        
        return ChatResponse(
            message: response.message,
            params: response.parameters,
            fullResponse: responseToDictionary(response),
            relatedQueries: response.relatedQueries
        )
    }
    
    func responseToDictionary(_ capability: ConvaAICapability) -> [String: Any?] {
        var dictionary: [String: Any?] = [:]
        
        dictionary["input"] = capability.input
        dictionary["message"] = capability.message
        dictionary["reason"] = capability.reason
        dictionary["language"] = capability.language
        dictionary["llm"] = capability.llm
        dictionary["requestId"] = capability.requestId
        dictionary["responseType"] = capability.responseType
        dictionary["messageType"] = capability.messageType
        dictionary["capability"] = capability.capability
        dictionary["isError"] = capability.isError
        dictionary["isFinal"] = capability.isFinal
        dictionary["isUnsupported"] = capability.isUnsupported
        dictionary["parameters"] = capability.parameters
        dictionary["relatedQueries"] = capability.relatedQueries
        dictionary["conversationHistory"] = capability.conversationHistory
        dictionary["isParametersComplete"] = capability.isParametersComplete
        
        return dictionary
    }
}
