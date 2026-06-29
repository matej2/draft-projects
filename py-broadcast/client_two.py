import asyncio
import random
import time

import websockets

async def send(websocket):
    random_number = random.randint(1, 10)
    time.sleep(random_number)
    await websocket.send("2")


async def receive(websocket):
    try:
        async for message in websocket:
            print(f"Received {message}")
    except websockets.exceptions.ConnectionClosed:
        print("\n[Client]: Connection to server lost.")

async def main():
    uri = "ws://localhost:8765"
    print("Connecting to websocket...")
    delay = 1  # Start with a 1-second delay
    max_delay = 60  # Maximum wait time between attempts

    while True:
        try:
            async with websockets.connect(uri) as websocket:
                print("Connected")

                await asyncio.gather(
                    receive(websocket),
                    send(websocket)
                )
        except (websockets.exceptions.ConnectionClosed, ConnectionRefusedError, OSError) as e:
            print(f"[Client]: Connection failed or lost ({type(e).__name__}).")
            print(f"[Client]: Retrying in {delay} seconds...")

            await asyncio.sleep(delay)
            delay = min(delay * 2, max_delay)

if __name__ == "__main__":
    asyncio.run(main())