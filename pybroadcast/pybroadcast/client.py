import asyncio
import datetime

import websockets


async def receive(websocket):
    try:
        async for message in websocket:
            time = datetime.datetime.now()
            print(f"{time} - [Server]: {message}")
    except websockets.exceptions.ConnectionClosed:
        print("\n[Client]: Connection to server lost.")


async def main():
    uri = "ws://localhost:8765"
    print("Connecting to websocket...")
    delay = 1
    max_delay = 60

    while True:
        try:
            async with websockets.connect(uri) as websocket:
                print("Connected")

                await asyncio.gather(
                    receive(websocket),
                )
                delay = 1
        except (websockets.exceptions.ConnectionClosed, ConnectionRefusedError, OSError) as e:
            print(f"[Client]: Connection failed or lost ({type(e).__name__}).")
            print(f"[Client]: Retrying in {delay} seconds...")

            await asyncio.sleep(delay)
            delay = min(delay * 2, max_delay)

if __name__ == "__main__":
    asyncio.run(main())