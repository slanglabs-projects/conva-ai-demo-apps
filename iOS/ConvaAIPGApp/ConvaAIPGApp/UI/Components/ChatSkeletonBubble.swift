//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI
struct ChatSkeletonBubble: View {
    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 8) {
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .frame(width: 150, height: 16)
                    .cornerRadius(8)
                
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .frame(width: 100, height: 16)
                    .cornerRadius(8)
            }
            .padding()
            .background(Color.gray.opacity(0.2))
            .cornerRadius(15)
            .shimmering()
        }
    }
}

extension View {
    func shimmering() -> some View {
        self.modifier(ShimmeringEffect())
    }
}

struct ShimmeringEffect: ViewModifier {
    @State private var phase: CGFloat = 0
    
    func body(content: Content) -> some View {
        content
            .overlay(
                GeometryReader { geometry in
                    Rectangle()
                        .fill(LinearGradient(
                            gradient: Gradient(colors: [Color.clear, Color.white.opacity(0.2), Color.clear]),
                            startPoint: .leading,
                            endPoint: .trailing
                        ))
                        .rotationEffect(.degrees(30))
                        .offset(x: phase * geometry.size.width * 2)
                        .frame(width: geometry.size.width, height: geometry.size.height)
                }
                .mask(content)
            )
            .onAppear {
                withAnimation(Animation.linear(duration: 1.5).repeatForever(autoreverses: false)) {
                    phase = 1
                }
            }
    }
}
