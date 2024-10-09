//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import SwiftUI

struct ChatHeader: View {
    let assistantData: AssistantData
    let dismissAction: () -> Void
    
    var body: some View {
        HStack {
            Button(action: dismissAction) {
                Image(systemName: "chevron.left")
                    .foregroundColor(.blue)
            }
            
            Text(assistantData.appName)
                .font(.headline)
            
            Spacer()
            
            Circle()
                .fill(Color.green)
                .frame(width: 10, height: 10)
            Text("Active")
                .font(.subheadline)
                .foregroundColor(.gray)
        }
        .padding()
    }
}
