//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

struct AssistantData: Identifiable, Equatable {
    let id: UUID
    let appName: String
    let assistantId: String
    let assistantVersion: String
    let apiKey: String
    let capabilityName: [String]?
    let capabilityGroup: [String]?
    
    
    init(from json: [String: Any]) {
        self.id = UUID()
        self.appName = json["app_name"] as? String ?? ""
        self.assistantId = json["assistant_id"] as? String ?? ""
        self.assistantVersion = json["assistant_version"] as? String ?? ""
        self.apiKey = json["api_key"] as? String ?? ""
        self.capabilityName = json["capabilities"] as? [String]
        self.capabilityGroup = json["capability_groups"] as? [String]
    }
    
    init(appName: String, assistantId: String, assistantVersion: String, apiKey: String, capabilityName: [String]?, capabilityGroup: [String]?) {
        self.id = UUID()
        self.appName = appName
        self.assistantId = assistantId
        self.assistantVersion = assistantVersion
        self.apiKey = apiKey
        self.capabilityName = capabilityName
        self.capabilityGroup = capabilityGroup
    }
}
