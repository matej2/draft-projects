import asyncio

import typer
import websockets
from prompt_toolkit import PromptSession
from prompt_toolkit.patch_stdout import patch_stdout
from typing_extensions import Annotated

clients = set()

async def handle_connection(websocket):
    clients.add(websocket)
    try:
        await websocket.wait_closed()
    finally:
        clients.discard(websocket)


async def broadcast_loop():
    session = PromptSession()
    with patch_stdout():
        while True:

            message = await session.prompt_async("Say something: ")

            if clients:
                await asyncio.gather(*(ws.send(message) for ws in clients))
                print(f"Sent to {len(clients)} client(s): {message}")
            else:
                print("No clients connected.")

async def init():
    async with websockets.serve(handle_connection, "localhost", 8765):
        print(f"WebSocket Server running on ws://localhost:8765.")
        await broadcast_loop()

def greet(
    ffff: Annotated[str, typer.Option("--name", "-n", help="The (last, if --title is given) name of the person to greet")] = ""
):
    asyncio.run(init())


