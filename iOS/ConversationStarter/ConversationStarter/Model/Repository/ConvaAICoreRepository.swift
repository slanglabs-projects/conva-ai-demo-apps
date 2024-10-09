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
                message: "Sorry! There was an error processing your request",
                isError: true
            )
        }
        
        return ChatResponse(
            message: response.message,
            capability: response.capability,
            params: response.parameters,
            relatedQueries: response.relatedQueries,
            isError: response.isError ?? false
        )
    }
}
