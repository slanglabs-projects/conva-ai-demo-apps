//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import Foundation
import SwiftUI

struct JSONViewBox: View {
    let jsonObject: Any
    let textHeader: String
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(textHeader)
                .font(.headline)
            
            ScrollView(.horizontal) {
                if let jsonString = prettyPrintedJSONString(jsonObject) {
                    Text(jsonString)
                        .font(.system(size: 14, weight: .regular, design: .monospaced))
                        .fixedSize(horizontal: false, vertical: true)
                } else {
                    Text("Invalid JSON")
                        .font(.system(size: 14, weight: .regular, design: .monospaced))
                        .lineLimit(nil)
                }
            }
        }
        .padding()
        .background(Color.blue.opacity(0.2))
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(Color.orange, lineWidth: 2)
        )
        .cornerRadius(10)
    }
    
    private func prettyPrintedJSONString(_ jsonObject: Any) -> String? {
        guard let data = try? JSONSerialization.data(withJSONObject: jsonObject, options: [.prettyPrinted]),
              let prettyString = String(data: data, encoding: .utf8) else {
            return nil
        }
        return prettyString
    }
}
