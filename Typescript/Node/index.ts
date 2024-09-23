//Initialising Conva.AI
import ConvaAI from "conva-ai";

const client = new ConvaAI({
  assistantID: "<YOUR_ASSISTANT_ID>",
  assistantVersion: "<YOUR_ASSISTANT_VERSION>",
  apiKey: "<YOUR_API_KEY>",
});

//Basic Response Generation
client
  .invokeCapability({
    query: "What is the weather like today?",
    stream: false,
  })
  .then((response) => {
    console.log("ConvaAI Response:", response);
    // TODO: Add application logic here
  })
  .catch((error) => {
    console.error("Error:", error);
  });


//Invoking Specific Capabilities

let conversationHistory: string | undefined = "{}";

  client.invokeCapabilityName({
      query: 'Can you tell me about yourself?',
      history: conversationHistory,
      capabilityName: 'small_talk',
      stream: false
    }).then(response => {
      console.log('ConvaAI Response:', response);  
      //TODO: Add application logic here
    }).catch(error => {
      console.error('Error:', error);
    });

//Maintaining Conversation History

let conversationHistory: string | undefined = "{}";

client.invokeCapability({
  query: 'What all products do you sell?',
  stream: false,
  history: conversationHistory
}).then(response => {
  if (response && 'conversationHistory' in response) {
    conversationHistory = JSON.stringify(response.conversationHistory)
  }
  console.log('ConvaAI Response:', response);
  //TODO: Add application logic here
}).catch(error => {
  console.error('Error:', error);
});