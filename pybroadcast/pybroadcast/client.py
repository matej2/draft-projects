import asyncio
import datetime

import websockets

domain = "localhost"

def get_curr_time():
    return datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')


async def receive(websocket):
    try:
        async for message in websocket:
            print(f"{get_curr_time()} - [Server]: {message}\n")
    except websockets.exceptions.ConnectionClosed:
        print(f"{get_curr_time()} - [Client]: Connection to server lost.\n")


async def main(port: int):
    uri = f"ws://{domain}:{port}"
    print(f"Connecting to websocket on {uri}...")
    delay = 5
    max_delay = 60

    while True:
        try:
            async with websockets.connect(uri) as websocket:
                print(f"Connected to {domain}:{port}. Ctrl + c to stop")

                await asyncio.gather(
                    receive(websocket),
                )
                delay = 5
        except (websockets.exceptions.ConnectionClosed, ConnectionRefusedError, OSError) as e:
            print(f"{get_curr_time()} - [Client]: Connection failed or lost ({type(e).__name__}).\n")
            print(f"{get_curr_time()} - [Client]: Retrying in {delay} seconds...\n")

            await asyncio.sleep(delay)
            delay = min(delay * 2, max_delay)

if __name__ == "__main__":
    asyncio.run(main())