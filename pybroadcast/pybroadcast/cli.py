import typer

from pybroadcast.server import command

app = typer.Typer()
app.command()(command)


if __name__ == "__main__":
    app()