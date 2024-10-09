import SwiftUI

struct ChatScreen: View {
    let assistantData: AssistantData
    @StateObject private var viewModel = ChatViewModel()
    @State private var scrollProxy: ScrollViewProxy?
    @State private var isCustomSheetPresented = false
    @State private var animateHints = false
    @Environment(\.dismiss) private var dismiss
    
    let hints = Utils.getDefaultHints()

    var body: some View {
        VStack {
            // Chat Header
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
                .onChange(of: viewModel.messages) { _, newMessages in
                    withAnimation {
                        proxy.scrollTo(newMessages.last?.id, anchor: .bottom)
                    }
                }
                .onAppear {
                    scrollProxy = proxy
                }
            }

            CustomSheet(isPresented: $isCustomSheetPresented) {
                VStack(spacing: 16) {
                    CStarterHintChips(
                        hints: Utils.getCStarterDefaultHints(),
                        inputText: $viewModel.cStarterInput,
                        isProcessing: $viewModel.cStarterProcessing,
                        showCheckmark: $viewModel.cStarterShowCheckmark,
                        showCrossmark: $viewModel.cStarterShowCrosskmark,
                        onHintSelected: { hint in
                            viewModel.processHintOrText(inputText: hint)
                        }
                    )
                }
            }

            // Hints section
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 10) {
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
                toggleSheet: {
                    viewModel.cStarterInput = nil
                    isCustomSheetPresented.toggle()
                },
                sendMessage: {
                    if isCustomSheetPresented {
                        viewModel.processHintOrText(inputText: viewModel.messageText)
                    } else {
                        viewModel.sendMessage()
                    }
                },
                isCustomSheetPresented: isCustomSheetPresented
            )
        }
        .onAppear {
            viewModel.clearState()
            viewModel.initializeAssistant(assistantData)
            isCustomSheetPresented = false
            animateHints = false
        }
        .navigationBarBackButtonHidden(true)
    }
    
//    private func processHintOrText(inputText: String? = nil) {
//        cStarterProcessing = true
//        self.cStarterInput = inputText
//        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//            cStarterProcessing = false
//            cStarterShowCheckmark = true
//            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//                cStarterShowCheckmark = false
//            }
//        }
//    }
}
