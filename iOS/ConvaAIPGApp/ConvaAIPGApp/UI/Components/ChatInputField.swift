//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct ChatInputField: View {
    @Binding var messageText: String
    let toggleSheet: () -> Void
    let sendMessage: () -> Void
    let isCustomSheetPresented: Bool
    
    var body: some View {
        HStack(spacing: 10) {
            Button(action: {
                withAnimation(.spring()) {
                    toggleSheet()
                }
            }) {
                Image(systemName: isCustomSheetPresented ? "minus" : "plus")
                    .foregroundColor(.blue)
                    .frame(width: 40, height: 40)
                    .background(Color.gray.opacity(0.2))
                    .clipShape(Circle())
            }
            
            HStack {
                TextField("  Ask anything", text: $messageText)
                
                Button(action: sendMessage) {
                    Image(systemName: "paperplane.fill")
                        .foregroundColor(.blue)
                }
            }
            .padding(8)
            .background(Color.gray.opacity(0.2))
            .cornerRadius(20)
        }
        .padding()
        .frame(height: 50)
    }
}
