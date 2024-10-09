//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import SwiftUI

struct ChatScreen: View {
    let assistantData: AssistantData
    @StateObject private var viewModel = ChatViewModel()
    @State private var scrollProxy: ScrollViewProxy?
    @State private var isCustomSheetPresented = false
    @State private var capabilityName: [String]?
    @State private var capabilityGroup: [String]?
    @State private var animateHints = false
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        VStack {
            
            ChatHeader(assistantData: assistantData) {
                viewModel.clearState()
                dismiss()
            }

            // Chat messages
            ScrollViewReader { proxy in
                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 10) {
                        ForEach(viewModel.messages) { message in
                            ChatBubble(chatInfo: message)
                                .id(message.id)
                        }
                    }
                    .padding()
                }
                .onChange(of: viewModel.messages) { oldmessage, newMessages in
                    withAnimation {
                        proxy.scrollTo(newMessages.last?.id, anchor: .bottom)
                    }
                }
                .onAppear {
                    scrollProxy = proxy
                }
            }
            
            if !isCustomSheetPresented {
                Spacer().frame(height: 8)
            }
            
            CustomSheet(isPresented: $isCustomSheetPresented) {
                VStack(spacing: 16) {
                    DropdownPicker(
                        selectedOption: $viewModel.selectedCapabilityName,
                        options: capabilityName ?? ["default"],
                        dropDownName: "Select Capability Name"
                    )
                    DropdownPicker(
                        selectedOption: $viewModel.selectedCapabilityGroup,
                        options: capabilityGroup ?? ["default"],
                        dropDownName: "Select Capability Group"
                    )
                }
            }
            
            // Hints section remains the same
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 10) {
                    let hints = Utils.getDefaultHints()
                    ForEach((viewModel.relatedQueries ?? hints).isEmpty ? hints : viewModel.relatedQueries ?? hints, id: \.self) { hint in
                        Text(hint)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 8)
                            .background(Color.gray.opacity(0.2))
                            .cornerRadius(16)
                            .opacity(animateHints ? 1.0 : 0.0)
                            .offset(x: animateHints ? 0 : -UIScreen.main.bounds.width)
                            .scaleEffect(animateHints ? 1.0 : 0.8)
                            .onTapGesture {
                                viewModel.sendHintMessage(hint: hint)
                            }
                            .animation(
                                .easeInOut(duration: 0.5)
                                    .delay(Double(viewModel.relatedQueries?.firstIndex(of: hint) ?? 0) * 0.05),
                                value: animateHints
                            )
                            .onAppear {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                    animateHints = true
                                }
                            }
                    }
                }
                .padding(.horizontal)
            }
            .frame(height: 40)
            .onChange(of: viewModel.relatedQueries) { _, _ in
                animateHints = false
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    animateHints = true
                }
            }
            
            ChatInputField(
                messageText: $viewModel.messageText,
                toggleSheet: { isCustomSheetPresented.toggle() },
                sendMessage: viewModel.sendMessage,
                isCustomSheetPresented: isCustomSheetPresented
            )
        }
        .onAppear {
            viewModel.clearState()
            viewModel.initializeAssistant(assistantData)
            capabilityName = assistantData.capabilityName ?? ["default"]
            capabilityGroup = assistantData.capabilityGroup ?? ["default"]
            isCustomSheetPresented = false
            animateHints = false
        }
        .navigationBarBackButtonHidden(true)
    }
}
