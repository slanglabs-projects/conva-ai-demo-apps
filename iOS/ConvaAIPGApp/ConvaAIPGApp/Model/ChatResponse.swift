//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

struct ChatResponse: Equatable {
    let message: String?
    let params: [String: Any]?
    let fullResponse: [String: Any?]?
    let relatedQueries: [String]?
    
    init(message: String?, params: [String: Any]? = nil, fullResponse: [String: Any?]? = nil, relatedQueries: [String]? = nil) {
        self.message = message
        self.params = params
        self.fullResponse = fullResponse
        self.relatedQueries = relatedQueries
    }
    
    static func == (lhs: ChatResponse, rhs: ChatResponse) -> Bool {
        return lhs.message == rhs.message &&
            NSDictionary(dictionary: lhs.params ?? [:]).isEqual(to: rhs.params ?? [:]) &&
            lhs.relatedQueries == rhs.relatedQueries
    }
}
