/// A utility class containing static properties for interacting with the CONVA AI system and providing initial messages.
class Utils {
  /// Singleton instance of the Utils class.
  static final Utils _instance = Utils._internal();

  /// Factory method to return the singleton instance of the Utils class.
  factory Utils() {
    return _instance;
  }

  /// Private constructor for the Utils class.
  Utils._internal();

  /// The Copilot ID of the CONVA AI Copilot.
  static String assistantId = "0c48ae816df1465caff9f4be2b7b3024";

  /// The API key for accessing the CONVA AI system.
  static String apiKey = "6991dd8158114fb9a00082c2f3b75057";

  /// The version of the CONVA AI Copilot.
  static String assistantVersion = "1.0.2";

  /// The initial message displayed to users.
  static String initialMessage = "Ask me any questions related to DigiYatra!";
}
