import 'package:conva_ai_app/conva/conva_layer.dart';
import 'package:conva_ai_app/utils/utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../widget/response_widget.dart';

/// The [HomePage] widget represents the main screen of the CONVA.ai application.
/// It allows users to interact with the CONVA AI system by sending questions and receiving responses.
class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  void initState() {
    super.initState();
  }

  final TextEditingController _textEditingController = TextEditingController();
  final List<Map<String, String>> _ioList = [];
  bool _isWaiting = false;
  final ConvaLayer _convaLayer = ConvaLayer();

  /// Sends the user input to the CONVA AI system for processing.
  void _sendInput() {
    WidgetsBinding.instance!.addPostFrameCallback((_) {
      String question = _textEditingController.text;
      _textEditingController.clear();
      FocusScope.of(context).unfocus();
      _convaLayer.sendRequest(question);
      setState(() {
        _isWaiting = true;
        _ioList.add({'input': question, 'output': 'Unknown'}); // Sample answer
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => _convaLayer),
      ],
      child: Scaffold(
        backgroundColor: Colors.black, // Entire background is black
        appBar: AppBar(
          title: const Text('CONVA.ai'),
          backgroundColor: Colors.blue,
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            if (_ioList.isNotEmpty)
              Consumer<ConvaLayer>(builder: (context, convaLayer, child) {
                if (convaLayer.isCallback) {
                  _isWaiting = false;
                  _ioList[_ioList.length - 1]['output'] =
                      convaLayer.generatedOutput;
                  convaLayer.resetCallback();
                }
                return Expanded(
                    child: ResponseWidget(
                  ioList: _ioList,
                  isWaiting: _isWaiting,
                ));
              })
            else
              Expanded(
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Image.asset(
                        'assets/images/conva.png',
                        width: 80,
                        height: 80,
                      ),
                      const SizedBox(height: 20),
                      Text(
                        Utils.initialMessage,
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 20.0,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            _buildInputBox(), // Input box always at the bottom
          ],
        ),
      ),
    );
  }

  /// Builds the input box widget at the bottom of the screen.
  Widget _buildInputBox() {
    return Container(
      padding: const EdgeInsets.all(16.0),
      decoration: BoxDecoration(
        color: Colors.grey[900],
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(10.0),
          topRight: Radius.circular(10.0),
        ),
      ),
      child: Row(
        children: [
          Expanded(
            child: TextField(
              controller: _textEditingController,
              style: const TextStyle(color: Colors.white),
              decoration: const InputDecoration(
                hintText: 'Type your question...',
                hintStyle: TextStyle(color: Colors.grey),
                border: InputBorder.none,
              ),
            ),
          ),
          IconButton(
            icon: const Icon(
              Icons.send,
              color: Colors.white,
            ),
            onPressed: _sendInput,
          ),
        ],
      ),
    );
  }
}
