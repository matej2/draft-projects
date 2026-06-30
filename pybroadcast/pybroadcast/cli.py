import typer

from pybroadcast.server import greet

app = typer.Typer()
app.command()(greet)


if __name__ == "__main__":
    app()