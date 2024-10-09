//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI
struct QRCodeScannerScreen: View {
    @ObservedObject var viewModel: HomeScreenViewModel
    @Environment(\.dismiss) var dismiss

    var body: some View {
        ZStack {
            QRCodeScannerPreview(viewModel: viewModel)
                .edgesIgnoringSafeArea(.all)
            
            VStack {
                Spacer()
                
                Button(action: {
                    dismiss()
                }) {
                    Text("Cancel")
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.red)
                        .cornerRadius(8)
                }
                .padding()
            }
        }
    }
}

struct QRCodeScannerPreview: UIViewControllerRepresentable {
    @ObservedObject var viewModel: HomeScreenViewModel

    func makeUIViewController(context: Context) -> UIViewController {
        return QRCodeScannerController { assistantData in
            if let data = assistantData {
                viewModel.setSelectedAssistantData(data)
            }
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
