import 'friend.dart';

class SplitResponse {
  final String? message;
  final List<Friend> splits;
  final int numberOfFriends;

  SplitResponse(
      {required this.message,
      required this.splits,
      required this.numberOfFriends});
}
