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

  /// The ID of the CONVA AI assistant.
  static String assistantId = "8bdcfb98d787496e82f2e48d53edfa19";

  /// The API key for accessing the CONVA AI system.
  static String apiKey = "2c87766dea05467fad5a04dab6f9f08c";

  /// The version of the CONVA AI assistant.
  static String assistantVersion = "1.0.1";

  /// The initial message displayed to users.
  static String initialMessage = "AI without the drama !";

  /// A list containing names of different capability groups.
  static List<String> cpabilitiesGroup = [
    'Default',
    'Joke Generator',
    'Grammar Ninja',
    'Recipe Generator'
  ];
}
