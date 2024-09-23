# Initialization for the React Project - Typescript Library for Conva.AI

npx create-react-app my-react-app --template typescript

# Install Conva.AI

npm install conva-ai

# Dependencies

```
axios == 1.7.7
os == 0.1.2
uuid == 10.0.0
```

## Usage

### Initializing the Client

To use the Conva AI library, you first need to initialize the `Conva.AI` client with your credentials:

```
import ConvaAI from 'conva-ai';

const client = new ConvaAI({
assistantID:"<YOUR_ASSISTANT_ID>",
assistantVersion:"<YOUR_ASSISTANT_VERSION>",
apiKey:"<YOUR_API_KEY>"});
```

//Rest of the implementation is in the main React file - App.tsx

# Compiling the project

npm run build

# Start the project and check the output for Conva.AI

npm start
