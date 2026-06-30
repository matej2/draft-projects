import asyncio
import os
import re
import subprocess

import typer
import websockets
from prompt_toolkit import PromptSession
from prompt_toolkit.patch_stdout import patch_stdout
from typing_extensions import Annotated

from pybroadcast.client import main

clients = set()

def cls():
    subprocess.call('cls' if os.name=='nt' else 'clear')

async def handle_connection(websocket):
    clients.add(websocket)
    try:
        await websocket.wait_closed()
    finally:
        clients.discard(websocket)

def screen(last_msg: str, port: int):
    output = f"""
Websocket Broadcast Server
 -> running on ws://localhost:{port}
-----------
:r - reload
:q - quit
-----------
Connected clients ({len(clients)}):
{", ".join([c.remote_address[0] for c in clients])} 
-----------
Broadcast: {last_msg}
-----------
    """
    return output


async def broadcast_loop(port: int):
    session = PromptSession()
    last_message = ""
    with patch_stdout():
        while True:

            cls()
            print(screen(last_message, port))

            message = await session.prompt_async("Message [or :command]: ")

            if message == ":q":
                break
            if message == ":r":
                continue
            if re.search("^\\s*$", message):
                last_message = "Invalid message - contains only whitespaces"
                continue

            if clients:
                await asyncio.gather(*(ws.send(message) for ws in clients))
                last_message = f"Sent to {len(clients)} client(s): {message}"
            else:
                last_message = "No clients connected."

async def start_server(port: int):
    async with websockets.serve(handle_connection, "localhost", port):
        await broadcast_loop(port)

def command(
    action: Annotated[str, typer.Argument(help="Required. Action for broadcast. Either 'start' or 'connect'")],
    port: Annotated[str, typer.Option("--port", "-p", help="Port to start server / connect to. Leave default for 8765")] = 8765
):
    if action == "start":
        asyncio.run(start_server(int(port)))
    elif action == "connect":
        asyncio.run(main(int(port)))
    else:
        print("Invalid option")



