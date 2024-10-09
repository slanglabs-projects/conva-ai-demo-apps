//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

struct ChatResponse: Equatable {
    let message: String?
    let capability: String?
    let params: [String: Any]?
    let relatedQueries: [String]?
    let isError: Bool
    
    init(message: String?, capability: String? = nil, params: [String: Any]? = nil, relatedQueries: [String]? = nil, isError: Bool = false) {
        self.message = message
        self.capability = capability
        self.params = params
        self.relatedQueries = relatedQueries
        self.isError = isError
    }
    
    static func == (lhs: ChatResponse, rhs: ChatResponse) -> Bool {
        return lhs.message == rhs.message &&
            lhs.capability == rhs.capability &&
            NSDictionary(dictionary: lhs.params ?? [:]).isEqual(to: rhs.params ?? [:]) &&
            lhs.relatedQueries == rhs.relatedQueries
    }
}
