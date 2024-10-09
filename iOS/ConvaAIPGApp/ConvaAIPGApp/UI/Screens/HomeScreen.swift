//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct HomeScreen: View {
    @StateObject private var viewModel = HomeScreenViewModel()
    @State private var isScannerPresented = false
   
    var body: some View {
        NavigationStack {
            ZStack {
                LinearGradient(gradient: Gradient(colors: [Color.blue.opacity(0.8), Color.white]),
                               startPoint: .topLeading,
                               endPoint: .bottomTrailing)
                    .edgesIgnoringSafeArea(.all)
                
                VStack(spacing: 10) {
                    HStack {
                        VStack(alignment: .leading) {
                            Text("Hello AIEnthusiast,")
                                .font(.system(size: 30))
                                .fontWeight(.bold)
                                .foregroundColor(.white)
                            
                            VStack(alignment: .leading, spacing: 0) {
                                Text("Scan the QR code for your assistant")
                                    .font(.system(size: 12))
                                    .fontWeight(.semibold)
                                    .foregroundColor(.white)
                                
                                Text("Or use the default assistants")
                                    .font(.system(size: 12))
                                    .fontWeight(.semibold)
                                    .foregroundColor(.white)
                            }
                        }
                        
                        Spacer()
                        
                        ZStack {
                            Circle()
                                .fill(Color.white.opacity(0.2))
                                .frame(width: 55, height: 55)
                            
                            Image("logo")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 40, height: 40)
                                .foregroundColor(.blue)
                                .shadow(color: .black.opacity(0.5), radius: 2, x: 2, y: 2)
                        }
                    }
                    .padding()
                    
                    AssistantCard(icon: "qrcode.viewfinder", title: "ScanQR", iconColor: Color.orange, shouldShowBorder: true) {
                        isScannerPresented = true
                    }
                    
                    ScrollView {
                        LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())], spacing: 20) {
                            ForEach(Utils.getDefaultAssistantData()) { option in
                                AssistantCard(icon: "book.fill", title: option.appName, iconColor: Color.blue) {
                                    viewModel.setSelectedAssistantData(option)
                                }
                            }
                        }
                        .padding()
                    }
                }
                .fullScreenCover(isPresented: $isScannerPresented) {
                    QRCodeScannerScreen(viewModel: viewModel)
                }
                .onChange(of: viewModel.selectedAssistantData) { oldValue, newValue in
                    isScannerPresented = false
                    if let newValue = newValue {
                        viewModel.navigateToChatScreen(newValue)
                    }
                }
                .onAppear {
                    viewModel.clearState()
                }
                
                .navigationDestination(isPresented: $viewModel.navigateToChat) {
                    if let selectedAssistantData = viewModel.selectedAssistantData {
                        ChatScreen(assistantData: selectedAssistantData)
                    }
                }
            }
        }
    }
}

struct HomeScreen_Previews: PreviewProvider {
    static var previews: some View {
        HomeScreen()
    }
}
