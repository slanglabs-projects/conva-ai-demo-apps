//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct ChatBubble: View {
    let chatInfo: ChatInfo

    var body: some View {
        HStack {
            if chatInfo.isUser {
                Spacer()
            }
            
            VStack(alignment: .leading) {
                if chatInfo.isProcessing {
                    ChatSkeletonBubble()
                } else {
                    Text(chatInfo.message ?? "")
                        .padding()
                }
            }
            .background(chatInfo.isUser ? Color.blue : Color.gray.opacity(0.2))
            .foregroundColor(chatInfo.isUser ? .white : .black)
            .cornerRadius(15)
            
            if !chatInfo.isUser {
                Spacer()
            }
        }
    }
}
