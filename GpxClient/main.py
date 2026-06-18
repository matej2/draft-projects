# This is a sample Python script.
import gpxpy
import uvicorn
from fastapi import FastAPI, UploadFile, File

from model.Point import Point
from model.PointInput import PhotoInput

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

app = FastAPI(title="GPX Client")

@app.post("/")
async def process_gpx(file: UploadFile = File(...)):
    contents = await file.read()
    gpx = gpxpy.parse(contents)

    for track in gpx.tracks:
        for segment in track.segments:
            return ([
                Point(point.latitude, point.longitude, point.elevation)
                for point in segment.points
            ])
    return ()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

