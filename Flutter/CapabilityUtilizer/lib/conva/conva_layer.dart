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

  List<String>? _relatedQueries;

  /// The history of interactions with the CONVA AI system
  String? _history;

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
  Future<void> sendRequest(String input, String capabilityName) async {
    try {
      ConvaAICapabilityModel completion;

      switch (capabilityName) {
        case "Default":
          completion = await sendRequestWithoutCapabilityName(input);
          break;
        default:
          completion =
              await sendRequestWithCapabilityName(input, capabilityName);
      }

      _history = completion.history;
      _message = completion.message;
      _relatedQueries = completion.relatedQueries;
      _generatedOutput = generateOutput(capabilityName, completion);
      _isCallback = true;
      notifyListeners();
    } catch (error) {
      _generatedOutput = "No answer found";
      _isCallback = true;
      notifyListeners();
    }
  }

  /// Sends a request to the CONVA AI system for processing without specifying a capability name.
  Future<ConvaAICapabilityModel> sendRequestWithCapabilityName(
      String input, String capability) async {
    ConvaAICapabilityModel completion = await ConvaAI.instance.invokeCapability(
        input: input, capabilityGroup: capability, context: _history);
    return completion;
  }

  /// Sends a request to the CONVA AI system for processing with a specified capability name.
  Future<ConvaAICapabilityModel> sendRequestWithoutCapabilityName(
      String input) async {
    ConvaAICapabilityModel completion = await ConvaAI.instance
        .invokeCapability(input: input, context: _history);
    return completion;
  }

  /// Generates the output response based on the capability name and completion model.
  String generateOutput(
      String capabilityName, ConvaAICapabilityModel completion) {
    switch (capabilityName) {
      case "Default":
        return generateResponse();
      case "Joke Generator":
        return generateJokeResponse(completion);
      case "Grammar Ninja":
        return generateGrammarResponse(completion);
      case "Recipe Generator":
        return generateRecipeResponse(completion);
      default:
        return generateResponse();
    }
  }

  /// Generates the response from the CONVA AI system.
  String generateResponse() {
    String response = "No answer found";
    if (_message != null) {
      response = 'Message: $_message';
    }

    if (_relatedQueries != null) {
      response += ('\nRelated Qeuries: $_relatedQueries');
    }
    return response;
  }

  /// Generates a response for the Recipe Generator capability.
  String generateRecipeResponse(ConvaAICapabilityModel completion) {
    String response = generateResponse();
    Map<String, dynamic>? param = completion.params;
    if (param == null) return response;
    if (param['ingredients'] != null) {
      response += ('\nIngredients: ${param['ingredients']}');
    }
    if (param['recipe'] != null) {
      response += ('\nRecipe: ${param['recipe']}');
    }
    return response;
  }

  /// Generates a response for the Joke Generator capability.
  String generateJokeResponse(ConvaAICapabilityModel completion) {
    String response = generateResponse();
    Map<String, dynamic>? param = completion.params;
    if (param == null) return response;
    if (param['topic'] != null) {
      response += ('\nTopic: ${param['topic']}');
    }
    return response;
  }

  /// Generates a response for the Grammar Ninja capability.
  String generateGrammarResponse(ConvaAICapabilityModel completion) {
    String response = generateResponse();
    Map<String, dynamic>? param = completion.params;
    if (param == null) return response;
    if (param['phrase'] != null) {
      response += ('\nPhrase: ${param['phrase']}');
    }
    if (param['corrected_word'] != null) {
      response += ('\nCorrected Word: ${param['corrected_word']}');
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
