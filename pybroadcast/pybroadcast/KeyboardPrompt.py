import sys
import functools
import asyncio as aio

class KeyboardPrompt:
    def __init__(self, loop=None):
        self.loop = loop or aio.get_event_loop()
        self.q = aio.Queue()
        self.loop.add_reader(sys.stdin, self.got_input)

    def got_input(self):
        print("got_input called")
        aio.ensure_future(self.q.put(sys.stdin.readline()), loop=self.loop)
        print("Get input")

    async def __call__(self, msg, end='\n', flush=False):
        print(msg, end=end, flush=flush)
        return (await self.q.get()).rstrip('\n')

