import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/// A widget that displays user inputs and CONVA.ai responses in a list format.
class ResponseWidget extends StatefulWidget {
  /// A list containing maps of user inputs and CONVA.ai responses.
  final List<Map<String, String>> ioList;

  /// A boolean indicating whether CONVA.ai is currently processing a request.
  final bool isWaiting;

  /// Constructs a [ResponseWidget] with the given parameters.
  const ResponseWidget({
    super.key,
    required this.ioList,
    required this.isWaiting,
  });

  @override
  State<StatefulWidget> createState() => _ResponseWidgetWidgetState();
}

class _ResponseWidgetWidgetState extends State<ResponseWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: widget.ioList.length,
      itemBuilder: (context, index) {
        return Container(
          margin: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
          padding: const EdgeInsets.all(16.0),
          decoration: BoxDecoration(
            color: Colors.grey[900],
            borderRadius: BorderRadius.circular(10.0),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const Text(
                'You:',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 18.0,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 5.0),
              Text(
                widget.ioList[index]['input']!,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 16.0,
                ),
              ),
              const SizedBox(height: 10.0),
              const Text(
                'CONVA.ai:',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 18.0,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 5.0),
              _buildAnswer(index),
            ],
          ),
        );
      },
    );
  }

  /// Builds the response answer based on the current index.
  Widget _buildAnswer(int index) {
    if (widget.isWaiting && (widget.ioList.length - 1 == index)) {
      return _buildWaitingMessage();
    } else {
      return Container(
        decoration: BoxDecoration(
          color: Colors.grey[800],
          borderRadius: BorderRadius.circular(10.0),
        ),
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              widget.ioList[index]['output']!,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 16.0,
              ),
            ),
          ),
        ),
      );
    }
  }

  /// Builds a waiting message with an animated ellipsis to indicate processing.
  Widget _buildWaitingMessage() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text(
            'Please wait',
            style: TextStyle(
              color: Colors.white,
              fontSize: 16.0,
            ),
          ),
          const SizedBox(height: 8.0),
          _buildAnimatedEllipsis(),
        ],
      ),
    );
  }

  /// Builds an animated ellipsis to indicate processing.
  Widget _buildAnimatedEllipsis() {
    return StreamBuilder<int>(
      stream: Stream<int>.periodic(const Duration(milliseconds: 200), (i) => i),
      builder: (context, snapshot) {
        final data = snapshot.data ?? 0;
        return Text(
          '..' * (data % 3 + 1),
          style: const TextStyle(
            color: Colors.white,
            fontSize: 16.0,
          ),
        );
      },
    );
  }
}
