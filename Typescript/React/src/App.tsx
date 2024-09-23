import React, { useState } from 'react';
import ConvaAI from 'conva-ai';
import './App.css';

const client = new ConvaAI({
    assistantID: "YOUR-ASSISTANT-ID",
    assistantVersion: "YOUR-ASSISTANT-VERSION",
    apiKey: "YOUR-API-KEY"
  });

const App: React.FC = () => {
  const [messages, setMessages] = useState<{ type: 'user' | 'assistant'; text: string; json?: any }[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [query, setQuery] = useState('');
  const [conversationHistory, setConversationHistory] = useState<boolean>(false);
  const [expanded, setExpanded] = useState<number | null>(null);

  const runQuery = async () => {
    if (!query.trim()) {
      return;
    }

    setLoading(true);
    setError(null);
//Basic Response Generation
    try {
      const response = await client.invokeCapability({
        query,
        stream: false, 
        history: conversationHistory ? JSON.stringify(messages) : undefined,
      });
      
//Invoking a specific capability
    //  try {
    //   const response = await client.invokeCapabilityName({
    //   query,
    //   stream: false, 
    //   capabilityName: 'product_search',
    //   history: conversationHistory ? JSON.stringify(messages) : undefined,
    //   });

      if (Symbol.asyncIterator in response) {
        for await (const res of response) {
          if (res.message) {
            setMessages((prevMessages) => [
              ...prevMessages,
              { type: 'user', text: query },
              { type: 'assistant', text: res.message, json: res },
            ]);
          }
        }
      } else {
        setMessages((prevMessages) => [
          ...prevMessages,
          { type: 'user', text: query },
          { type: 'assistant', text: response.message, json: response },
        ]);
      }

      setQuery('');
    } catch (err) {
      console.error('Detailed Error:', err);
      setError('An error occurred while processing the query.');
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      runQuery();
    }
  };

  return (
    <div className="app-container">
      <div className="chat-container">
        <div className="messages">
          {messages.map((msg, index) => (
            <div key={index} className={`message ${msg.type}`}>
              <div className="message-text">{msg.text}</div>
              {msg.type === 'assistant' && msg.json && (
                <button onClick={() => setExpanded(expanded === index ? null : index)}>
                  {expanded === index ? 'x' : '</>'}
                </button>
              )}
              {msg.type === 'assistant' && expanded === index && (
                <pre className="json-response">{JSON.stringify(msg.json, null, 2)}</pre>
              )}
            </div>
          ))}
        </div>
        <div className="input-container">
          <textarea
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyUp={handleKeyPress} 
            placeholder="Ask your question here..."
          />
          <button onClick={runQuery} disabled={loading}>
            {loading ? 'Processing...' : 'Send'}
          </button>
        </div>
      </div>
      <div className="sidebar">
        <label>
          <input
            type="checkbox"
            checked={conversationHistory}
            onChange={(e) => setConversationHistory(e.target.checked)}
          />
          Include Conversation History
        </label>
      </div>
      {error && <div className="error">{error}</div>}
    </div>
  );
};

export default App;