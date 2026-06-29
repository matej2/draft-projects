import asyncio

import websockets

async def hello(websocket):
    print("Hello fro m main")
    name = await websocket.recv()
    await websocket.send(f'Hello {name}')


async def print_hi():
    async with websockets.serve(hello, "localhost", 8765):
        await asyncio.Future()

if __name__ == '__main__':
    asyncio.run(print_hi())

