//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct DropdownPicker: View {
    @Binding var selectedOption: String?
    let options: [String]
    let dropDownName: String
    
    var body: some View {
        Menu {
            ForEach(options, id: \.self) { option in
                Button(option) {
                    selectedOption = option
                }
            }
        } label: {
            HStack {
                Text(selectedOption ?? dropDownName)
                    .foregroundColor(.black)
                Spacer()
                Image(systemName: "chevron.down")
                    .foregroundColor(.gray)
            }
            .padding()
            .background(Color.gray.opacity(0.1))
            .cornerRadius(10)
        }
    }
}
