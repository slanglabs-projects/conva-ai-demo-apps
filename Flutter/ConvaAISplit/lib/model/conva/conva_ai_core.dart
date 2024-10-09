import 'package:conva_ai_core/conva_ai_core.dart';

class ConvaAICore {
  String? _conversationHistory = null;
  void initialize(String id, String key, String version) {
    ConvaAI.init(id: id, key: key, version: version);
  }

  Future<Response?>? invokeCapabilityWithName(
      String input, String capabilityName) async {
    try {
      Response response = await ConvaAI.invokeCapabilityWithName(
          input: input,
          capability: capabilityName,
          context: ConvaAIContext(history: _conversationHistory));
      _conversationHistory = response.history;
      return response;
    } on Exception {
      return null;
    }
  }
}
