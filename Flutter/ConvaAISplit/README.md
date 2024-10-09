# ConvaAISplit App

The **ConvaAISplit App** is a Flutter application designed to facilitate the splitting of bills among friends based on user-defined conditions. It takes unstructured input and processes it to provide a structured output, making it easy to understand how much each friend owes.

## Screenshot

![App Screenshot](https://drive.google.com/uc?id=1D0OnhMGYnw1TAyGLpk-xzQIEw38LMJQN)

## Features

- **Unstructured Input Processing:** Accepts free-form text input for bill splitting.
- **Structured Output:** Generates a clear breakdown of splits, total amount, number of people, and split conditions.
- **Dynamic UI Updates:** Automatically updates the user interface with friends' names and amounts owed.
- **MVVM Architecture:** Utilizes the Model-View-ViewModel architecture for better separation of concerns and maintainability.
- **Integration with Conva.AI:** Leverages the Conva.AI platform to process input and provide accurate bill splitting calculations.

## Example Output

**Example Query:** Split 500 among Roshan, Kumar, and Subh. Divide 75% of the money between Roshan and Kumar, and the remaining should go to Subh.

When processing the input, the app returns a structured response in the following format:

```json
{
    "splits": {
        "Roshan": "187.5",
        "Kumar": "187.5",
        "Subh": 125
    },
    "total_amount": 500,
    "number_of_people": 3,
    "split_conditions": {
        "rosahan_kumar_split": "equal",
        "subh_share": 125
    }
}
```
## How It Works

1. **Input Processing:** Users provide unstructured input specifying the bill and any specific conditions for splitting.
2. **Conva.AI Processing:** The app uses the Conva.AI platform to analyze the input and determine how to split the bill.
3. **Dynamic UI Update:** The app extracts parameters from the response and updates the user interface to display each friend's name and the amount they owe.

## Conva.AI Use Cases

This app utilizes the **ConvaAICore SDK** to facilitate the splitting of bills among friends based on user-defined conditions. For more information about the SDK use cases, you can navigate to the following files in the project:

- [ConvaAICoreRepository](https://github.com/slanglabs/polyglot/blob/master/client/slang-flutter-sample-apps/ConvaAISplit/lib/model/conva_ai_core_repository.dart): Implements the functionality for processing responses related to bill splitting.
- [ConvaAICoreImpl.swift](https://github.com/slanglabs/polyglot/blob/master/client/slang-flutter-sample-apps/ConvaAISplit/lib/model/conva/conva_ai_core.dart): Contains the core logic for connecting to the Conva.AI platform and making API calls.

These files are essential for handling the input analysis and generating accurate split calculations based on user-defined parameters.

## Additional Resources

For more information on the Conva.AI SDKs and how to create and manage assistants, please refer to the official [Conva.AI documentation](https://docs.conva.ai/).


