import asyncio

import websockets


async def hello():
    url = "ws://localhost:8765"

    async with websockets.connect(url) as websocket:
        await websocket.send('John')
        greeting = await websocket.recv()
        print(greeting)

if __name__ == "__main__":
    asyncio.run(hello())