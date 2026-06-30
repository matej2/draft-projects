import asyncio
import os
import subprocess

import typer
import websockets
from prompt_toolkit import PromptSession
from prompt_toolkit.patch_stdout import patch_stdout
from typing_extensions import Annotated

clients = set()

def cls():
    subprocess.call('cls' if os.name=='nt' else 'clear')

async def handle_connection(websocket):
    clients.add(websocket)
    try:
        await websocket.wait_closed()
    finally:
        clients.discard(websocket)

def screen(last_msg: str):
    output = f"""
Websocket Broadcast Server
-----------
:r - reload
:q - quit
-----------
Connected clients ({len(clients)}):
{"".join([c.remote_address[0] for c in clients])} 
-----------
Broadcast: {last_msg}
-----------
    """
    return output


async def broadcast_loop():
    session = PromptSession()
    last_message = ""
    with patch_stdout():
        while True:

            cls()
            print(screen(last_message))

            message = await session.prompt_async("Message [or :command]: ")

            if message == ":q":
                break
            if message == ":r":
                continue

            if clients:
                await asyncio.gather(*(ws.send(message) for ws in clients))
                last_message = f"Sent to {len(clients)} client(s): {message}"
            else:
                last_message = "No clients connected."

async def init():
    async with websockets.serve(handle_connection, "localhost", 8765):
        print(f"WebSocket Server running on ws://localhost:8765.")
        await broadcast_loop()

def greet(
    ffff: Annotated[str, typer.Option("--name", "-n", help="The (last, if --title is given) name of the person to greet")] = ""
):
    asyncio.run(init())


