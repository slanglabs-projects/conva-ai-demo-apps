import asyncio
from conva_ai.client import AsyncConvaAI 

def greeting():
    print("Ah, the ever-so-wise Boomers, \nthe perpetually self-aware Millennials, and \nthe remarkably knowledgeable Gen Z - together, we form the circle of eternal enlightenment!\n Welcome, wise sages of all ages, to another day of unrivaled wisdom and unending insight in the AI-powered wonderland! \nWhere machines dream of electric sheep and chatbots tell jokes!")
    
async def generate(query: str, stream: bool):
    client = AsyncConvaAI(
        copilot_id="YOUR_COPILOT_ID", 
        copilot_version="YOUR_ASSISTANT_VERSION", 
        api_key="<YOUR_API_KEY>"
    )
    response = client.invoke_capability(query, stream=stream)
    out = "" 
    async for res in response:
        out = res.model_dump_json(indent=4)
    return out

async def main():
    greeting()
    while True:
        query = input("What would you like to ask about DigiYatra? (Type 'exit' to quit) ")
        if query.lower() == "exit":
            break
        
        final_response = asyncio.run(generate(query, True))
        print(final_response)

if __name__ == "__main__":
    asyncio.run(main())
