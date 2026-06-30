import asyncio
import os

import aioconsole
import typer
import websockets
from typing_extensions import Annotated

clients = set()

def cls():
    os.system('cls' if os.name=='nt' else 'clear')

async def handle_connection(websocket):
    clients.add(websocket)
    try:
        await websocket.wait_closed()
    finally:
        clients.discard(websocket)

async def screen(stdout):
    stdout.write("\x1b[2J\x1b[H")
    output = f"""
--- WebSocket Broadcast ---
/r - refresh

--- Connected clients ---
{str([c.remote_address for c in clients])}

--- Send messages ---
Enter message to send:
    """
    stdout.write(output)


async def broadcast_loop():
    stdin, stdout = await aioconsole.get_standard_streams()
    while True:
        await screen(stdout)

        line = await stdin.readline()
        if not line:
            break
        message = line.decode().rstrip("\n") if isinstance(line, bytes) else line.rstrip("\n")
        if message == "/r":
            continue
        if clients:
            await asyncio.gather(*(ws.send(message) for ws in clients))
            print(f"Sent to {len(clients)} client(s): {message}")
        else:
            print("No clients connected.")

async def echo():
    stdin, stdout = await aioconsole.get_standard_streams()
    async for line in stdin:
        stdout.write(line)

async def init():
    async with websockets.serve(handle_connection, "localhost", 8765):
        print(f"WebSocket Server running on ws://localhost:8765.")
        await broadcast_loop()

def greet(
    ffff: Annotated[str, typer.Option("--name", "-n", help="The (last, if --title is given) name of the person to greet")] = ""
):
    asyncio.run(init())


