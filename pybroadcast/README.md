# Broadcast server

This is a simple command-line app that allows starting and connecting to server using WebSockets. Server instance can then broadcast messages to all connected clients

## Usage

For development, you can run package directly withouth installing the package. First, setup pipenv for the project, then run:

    pipenv run python -m pybroadcast.cli [action] [--port|-p <port>]


Otherwise, you need to install package using:

    pip install -e .

Then you can use cli tool:

    broadcast-server [action] [--port|-p <port>]

Project is based on [this idea](https://roadmap.sh/projects/broadcast-server)