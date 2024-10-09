import 'package:flutter/material.dart';
import 'package:conva_ai_split_app/model/conva_ai_core_repository.dart';
import 'package:conva_ai_split_app/model/split_response.dart';
import '../model/friend.dart';

class SplitViewModel extends ChangeNotifier {
  final ConvaAICoreRepository convaAICoreRepository = ConvaAICoreRepository();

  SplitViewModel() {
    convaAICoreRepository.initialize();
  }

  List<Friend> friends = [];
  List<TextEditingController> nameControllers = [];
  List<TextEditingController> amountControllers = [];

  bool isLoading = false;

  void addRow() {
    friends.add(Friend(name: '', amountOwed: '0'));
    nameControllers.add(TextEditingController());
    amountControllers.add(TextEditingController(text: '0'));
    notifyListeners();
  }

  void addFriendRow(Friend friend) {
    friends.add(friend);
    nameControllers.add(TextEditingController(text: friend.name));
    amountControllers.add(TextEditingController(text: friend.amountOwed));
    notifyListeners();
  }

  void updateFriend(int index, String name, String amount) {
    friends[index].name = name;
    friends[index].amountOwed = amount;
    nameControllers[index].text = name;
    amountControllers[index].text = amount;
    notifyListeners();
  }

  void removeFriendRow(int index) {
    friends.removeAt(index);
    nameControllers.removeAt(index);
    amountControllers.removeAt(index);
    notifyListeners();
  }

  Future<void> submitAndSplitBill(String input) async {
    isLoading = true;
    notifyListeners();

    try {
      SplitResponse response =
          await convaAICoreRepository.invokeCapabilitySplit(input);

      friends.clear();
      nameControllers.clear();
      amountControllers.clear();

      for (int i = 0; i < response.splits.length; i++) {
        addFriendRow(response.splits[i]);
      }
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  void clearAll() {
    friends.clear();
    nameControllers.clear();
    amountControllers.clear();
    notifyListeners();
  }
}
