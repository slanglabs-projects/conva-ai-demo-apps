//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

struct AssistantData: Codable, Equatable {
    let appName: String
    let assistantId: String
    let assistantVersion: String
    let apiKey: String
    
    init(appName: String, assistantId: String, assistantVersion: String, apiKey: String) {
        self.appName = appName
        self.assistantId = assistantId
        self.assistantVersion = assistantVersion
        self.apiKey = apiKey
    }
}
