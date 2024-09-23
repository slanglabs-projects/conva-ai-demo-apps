# Initialization for the Node Project - Typescript Library for Conva.AI

npm install

# Install Conva.AI

npm install conva-ai

## Usage

### Initializing the Client

To use the Conva AI library, you first need to initialize the `Conva.AI` client with your credentials:

```import ConvaAI from 'conva-ai';

const client = new ConvaAI({
assistantID:"<YOUR_ASSISTANT_ID>",
assistantVersion:"<YOUR_ASSISTANT_VERSION>",
apiKey:"<YOUR_API_KEY>"});
```
//Rest of the implementation in the index.ts file

# Typescript configuration for the node project

```{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "node",
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "declaration": true
  },
  "include": ["src/**/*.ts"],
  "exclude": ["node_modules"]
}
```

# Add build script to package.json

```
"build": "tsc",
"start": "node dist/index.js"
```
# Compiling the project

npm run build

# Start the project and check the output for Conva.AI

npm start
