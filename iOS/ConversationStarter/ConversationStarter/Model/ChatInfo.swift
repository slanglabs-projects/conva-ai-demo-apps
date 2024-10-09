//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation

struct ChatInfo: Identifiable, Equatable {
    let id = UUID()
    var message: String? = nil
    var isUser: Bool = false
    var isProcessing: Bool = false
    var parameters: [String: Any]? = nil
    
    static func == (lhs: ChatInfo, rhs: ChatInfo) -> Bool {
        return lhs.id == rhs.id
    }
}
