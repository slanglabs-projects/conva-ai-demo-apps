//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import SwiftUI

struct AssistantCard: View {
    let icon: String
    let title: String
    let iconColor: Color
    var shouldShowBorder: Bool = false
    var onTap: () -> Void
    
    var body: some View {
        VStack(alignment: .center, spacing: 10) {
            ZStack {
                Circle()
                    .fill(iconColor.opacity(0.2))
                    .frame(width: 50, height: 50)
                Image(systemName: icon)
                    .foregroundColor(iconColor)
                    .font(.title2)
            }
            Text(title)
                .font(.headline)
                .lineLimit(1)
                .multilineTextAlignment(.center)
        }
        .padding()
        .frame(width: 100, height: 120)
        .background(Color.white)
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(shouldShowBorder ? Color.orange : Color.clear, lineWidth: 3)
        )
        .cornerRadius(15)
        .shadow(radius: 0.5)
        .onTapGesture {
            onTap()
        }
    }
}
