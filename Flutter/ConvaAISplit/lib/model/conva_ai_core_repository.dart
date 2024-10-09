import 'package:conva_ai_core/conva_ai_core.dart';
import 'package:conva_ai_split_app/model/split_response.dart';

import 'friend.dart';
import 'conva/conva_ai_core.dart';

class ConvaAICoreRepository {
  final ConvaAICore _impl = ConvaAICore();
  void initialize() {
    // Use your own assistant credentials here
    _impl.initialize("id",
        "key", "version");
  }

  Future<SplitResponse> invokeCapabilitySplit(String input) async {
    int numberOfFriends = 0;
    List<Friend> friends = [];
    try {
      Response? response =
          await _impl.invokeCapabilityWithName(input, "bill_splitting");
      if (response != null) {
        if (response.params?.containsKey("splits") == true) {
          Map<String, dynamic> relatedProducts =
              Map<String, dynamic>.from(response.params!['splits']);
          relatedProducts.forEach((key, value) {
            friends.add(Friend(name: key, amountOwed: value.toString()));
          });
        }
        if (response.params?.containsKey("number_of_people") == true) {
          var noOfFriends = response.params!['number_of_people'];
          if (noOfFriends is int) {
            numberOfFriends = noOfFriends;
          }
        }
        return SplitResponse(
            message: response.message,
            splits: friends,
            numberOfFriends: numberOfFriends);
      }
      return SplitResponse(
          message: "Sorry there was an error while processing the splits",
          splits: friends,
          numberOfFriends: numberOfFriends);
    } catch (e) {
      return SplitResponse(
          message: "Sorry there was an error while processing the splits",
          splits: friends,
          numberOfFriends: numberOfFriends);
    }
  }
}
