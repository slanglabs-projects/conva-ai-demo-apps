//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct CustomSheet<Content: View>: View {
    @Binding var isPresented: Bool
    var content: () -> Content

    var body: some View {
        VStack {
            if isPresented {
                content()
                    .padding(.top)
                    .padding([.leading, .trailing])
                    .padding(.bottom, 6)
                    .background(Color.white)
                    .cornerRadius(20, corners: [.topLeft, .topRight])
                    .shadow(color: Color.black.opacity(0.1), radius: 3, x: 0, y: -5)
            }
        }
        .scaleEffect(x: 1, y: isPresented ? 1 : 0, anchor: .top)
        .animation(.easeInOut, value: isPresented)
        .frame(maxWidth: .infinity)
        .transition(.identity) 
    }
}

extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RoundedCorner: Shape {
    var radius: CGFloat
    var corners: UIRectCorner

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}
