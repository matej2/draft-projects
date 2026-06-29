import asyncio
import datetime

import websockets

one = None
two = None

async def receive(websocket):
    global one, two
    time_diff = None

    try:
        async for message in websocket:
            msg = int(message)
            print(f"Received {msg}")

            # One is second
            if msg == 1:
                one = datetime.datetime.now()
                if two is not None:
                    time_diff = datetime.datetime.now() - two

            # Two is second
            if msg == 2:
                two = datetime.datetime.now()
                if one is not None:
                    time_diff = datetime.datetime.now() - one

            if time_diff is not None:
                await websocket.send(f"You are too late for {time_diff}")
            else:
                await websocket.send(f"You are first")

        return [one, two]


    except websockets.exceptions.ConnectionClosed:
        print("\n[Client]: Connection to server lost.")

async def handle_connection(websocket):
    await receive(websocket)

async def main():
    # Start the server on localhost port 8765
    async with websockets.serve(handle_connection, "localhost", 8765):
        print("WebSocket Server running on ws://localhost:8765")
        await asyncio.Future()  # Keeps the server running indefinitely


if __name__ == '__main__':
    asyncio.run(main())

