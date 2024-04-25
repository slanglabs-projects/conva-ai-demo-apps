import asyncio
from conva_ai.client import AsyncConvaAI


def greeting():
    print("Why did the robot go to therapy? It had too many bugs and needed to debug its emotions\n")


async def generate(client: AsyncConvaAI, query: str, capability_group: str):
    response = client.invoke_capability(query, stream=True, capability_group=capability_group)
    out = ""
    async for res in response:
        out = res
    return out
    
async def main():
    client = AsyncConvaAI(
        copilot_id="", # Copy the Copilot ID from Magic Studio
        api_key="", # Copy the API Key from Magic Studio
        copilot_version="" # Copy the version from Magic Studio
    )
    greeting()
    while True:
        print("What capability would you like to choose from the following: JokeGenerator, RecipeGenerator, GrammarNinja \n")
        capability = input("Enter the capability (Type 'exit'to quit): ").lower()
        if capability == 'jokegenerator':
            await jokegenerator(client)
        elif capability == 'recipegenerator':
            await recipegenerator(client)
        elif capability == 'grammarninja':
            await grammar_ninja(client)
        elif capability == 'exit':
            break
        else:
            print("Invalid capability selection. Please try again.\n")


async def jokegenerator(client):
    print("You've selected the JokeGenerator capability!\n")
    print("Enter the topic for which you wanna hear the joke!")
    while True:
        query = input("Enter the Topic (Type 'exit' to quit): ")
        if query.lower() == 'exit':
            break

        final_response = await generate(client, query, capability_group = "Jokes")
        print(final_response)


async def recipegenerator(client):
    print("You've selected the RecipeGenerator capability!\n")
    print("What do you call a fake noodle? - An impasta!")
    while True:
        print("Could you provide the list of ingredients (Type 'exit' to quit): ")
        query = input("Ingredients: ")
        if query.lower() == 'exit':
            break

        final_response = await generate(client, query, "Receipes")
        print("Here are some receipes: ")
        print(final_response)
        #for receipe in final_response.parameters.receipes_list:
        #  print(receipe)


async def grammar_ninja(client):
    print("You've selected the GrammarNinja capability!\n")
    print("Why don't chatbots ever get tired of talking?\nBecause they have an unlimited supply of 'byte-sized' conversation!")
    while True:
        print("Enter the text with some spelling mistake(Type 'exit' to quit)")
        text = input("Text: ")
        if text == 'exit':
            break

        print("Enter the corrected word: ")
        replace = input("Word: ")
        query = text + ":" + replace

        final_response = await generate(client, query, "ninja")
        print(final_response)


if __name__ == "__main__":
    asyncio.run(main())
