//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct ChatBubble: View {
    let chatInfo: ChatInfo
    @State private var showFullResponse = false

    var body: some View {
        HStack {
            if chatInfo.isUser {
                Spacer()
            }
            
            VStack(alignment: .leading) {
                if chatInfo.isProcessing {
                    ChatSkeletonBubble()
                } else {
                    // Hide the text when showFullResponse is true
                    if !showFullResponse {
                        Text(chatInfo.message ?? "")
                            .padding()
                    }

                    // JSON view box
                    if let fullResponse = chatInfo.fullResponse, !chatInfo.isUser, showFullResponse {
                        JSONViewBox(jsonObject: fullResponse, textHeader: "Full Response: ")
                            .padding()
                    } else if let parameters = chatInfo.parameters, !chatInfo.isUser {
                        JSONViewBox(jsonObject: parameters, textHeader: "Parameters: ")
                            .padding()
                    }
                }
            }
            .animation(.easeInOut, value: showFullResponse)
            .background(chatInfo.isUser ? Color.blue : Color.gray.opacity(0.2))
            .foregroundColor(chatInfo.isUser ? .white : .black)
            .cornerRadius(15)
            .onTapGesture {
                if chatInfo.fullResponse != nil, !chatInfo.isUser {
                    withAnimation {
                        showFullResponse.toggle()
                    }
                }
            }
            
            if !chatInfo.isUser {
                Spacer()
            }
        }
    }
}
