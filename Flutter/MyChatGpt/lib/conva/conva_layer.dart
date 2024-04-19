import 'dart:ui';

import 'package:conva_ai_app/utils/utils.dart';
import 'package:conva_ai_core/conva_ai_core.dart';
import 'package:conva_ai_core/models/conva_ai_capability_completion.dart';
import 'package:flutter/cupertino.dart';

/// A class representing the layer that interacts with the CONVA AI system.
class ConvaLayer with ChangeNotifier {
  ConvaLayer() {
    initConvaAI();
  }

  /// The message received from the CONVA AI system.
  String? _message;

  /// The history of interactions with the CONVA AI system
  String? history;

  /// A boolean indicating whether a callback from CONVA AI is in progress.
  bool _isCallback = false;

  /// The generated output response from CONVA AI.
  String _generatedOutput = "No answer found";

  /// Initializes the CONVA AI system by providing the necessary credentials.
  void initConvaAI() {
    ConvaAI.instance.initCopilot(
        id: Utils.assistantId,
        key: Utils.apiKey,
        version: Utils.assistantVersion);
  }

  /// Sends a request to the CONVA AI system for processing.
  Future<void> sendRequest(String input) async {
    try {
      ConvaAICapabilityModel completion = await ConvaAI.instance
          .invokeCapability(input: input, context: history);

      history = completion.history;
      _message = completion.message;
      _generatedOutput = generateResponse();
      _isCallback = true;
      notifyListeners();
    } catch (error) {
      _generatedOutput = "No answer found";
      _isCallback = true;
      notifyListeners();
    }
  }

  /// Generates the response from the CONVA AI system.
  String generateResponse() {
    String response = "No answer found";
    if (_message != null) {
      response = '$_message';
    }
    return response;
  }

  /// Resets the callback status after processing.
  void resetCallback() {
    _isCallback = false;
  }

  bool get isCallback => _isCallback;

  /// Returns the generated output response from CONVA AI.
  String get generatedOutput => _generatedOutput;
}
