import SwiftUI

struct CStarterHintChips: View {
    let hints: [String]
    @Binding var inputText: String?
    @Binding var isProcessing: Bool
    @Binding var showCheckmark: Bool
    @Binding var showCrossmark: Bool
    
    var onHintSelected: ((String) -> Void)?
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Spacer()
                if showCheckmark {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.green)
                        .transition(.scale)
                } else if showCrossmark {
                    Image(systemName: "xmark.circle.fill")
                        .foregroundColor(.red)
                        .transition(.scale)
                } else if isProcessing {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                        .transition(.scale)
                }
            }
            
            Text("Conversation Starter")
                .font(.headline)
                .padding(.bottom, 8)
            
            if let displayText = inputText {
                Text(displayText)
                    .font(.subheadline)
                    .padding(12)
                    .background(Color.gray.opacity(0.2))
                    .fixedSize(horizontal: false, vertical: true)
                    .cornerRadius(16)
            } else {
                VStack(alignment: .leading, spacing: 6) {
                    ForEach(hints, id: \.self) { hint in
                        Text(hint)
                            .padding(12)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(Color.gray.opacity(0.2))
                            .cornerRadius(16)
                            .fixedSize(horizontal: false, vertical: true)
                            .onTapGesture {
                                onHintSelected?(hint)
                            }
                    }
                }
            }
        }
        .padding()
        .animation(.easeInOut, value: inputText)
    }
}
