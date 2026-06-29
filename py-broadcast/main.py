import asyncio

import websockets

async def send(websocket):
    await websocket.send("Hello from main")

async def receive(websocket):
    try:
        async for message in websocket:
            print(f"Received {message}")
    except websockets.exceptions.ConnectionClosed:
        print("\n[Client]: Connection to server lost.")

async def handle_connection(websocket):
    await asyncio.gather(
        receive(websocket),
        send(websocket)
    )

async def main():
    # Start the server on localhost port 8765
    async with websockets.serve(handle_connection, "localhost", 8765):
        print("WebSocket Server running on ws://localhost:8765")
        await asyncio.Future()  # Keeps the server running indefinitely


if __name__ == '__main__':
    asyncio.run(main())

