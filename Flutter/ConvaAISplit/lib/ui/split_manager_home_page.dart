import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewModel/split_view_model.dart';

class SplitManagerHomePage extends StatefulWidget {
  const SplitManagerHomePage({super.key});

  @override
  SplitManagerHomePageState createState() => SplitManagerHomePageState();
}

class SplitManagerHomePageState extends State<SplitManagerHomePage> {
  final TextEditingController queryTextController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<SplitViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Split Manager'),
        actions: [
          IconButton(
            icon: const Icon(Icons.clear),
            onPressed: () {
              viewModel.clearAll();
            },
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            if (viewModel.isLoading)
              const Center(
                child: CircularProgressIndicator(),
              )
            else
              Expanded(
                child: ListView.builder(
                  itemCount: viewModel.friends.length,
                  itemBuilder: (context, index) {
                    return Row(
                      children: [
                        Expanded(
                          child: TextField(
                            controller: viewModel.nameControllers[index],
                            onChanged: (value) {
                              viewModel.updateFriend(index, value,
                                  viewModel.friends[index].amountOwed);
                            },
                            decoration: const InputDecoration(
                                labelText: 'Friend\'s Name'),
                          ),
                        ),
                        const SizedBox(width: 10),
                        Expanded(
                          child: TextField(
                            controller: viewModel.amountControllers[index],
                            keyboardType: TextInputType.number,
                            onChanged: (value) {
                              viewModel.updateFriend(
                                  index, viewModel.friends[index].name, value);
                            },
                            decoration: const InputDecoration(
                                labelText: 'Split Amount'),
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete),
                          onPressed: () {
                            viewModel.removeFriendRow(index);
                          },
                        ),
                      ],
                    );
                  },
                ),
              ),
            TextField(
              controller: queryTextController,
              decoration: const InputDecoration(labelText: 'Type your query'),
              keyboardType: TextInputType.text,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: viewModel.isLoading
                  ? null
                  : () {
                      viewModel.submitAndSplitBill(queryTextController.text);
                    },
              child: const Text('Submit & Split Bill'),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: viewModel.isLoading
            ? null
            : () {
                viewModel.addRow();
              },
        child: const Icon(Icons.add),
      ),
    );
  }
}
