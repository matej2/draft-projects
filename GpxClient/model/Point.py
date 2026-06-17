class Point:
    def __init__(self, latitude, longitude, elevation):
        self.lat_min = latitude - 0.000050
        self.lat_max = latitude + 0.000050

        self.lon_min = longitude - 0.000050
        self.lon_max = longitude + 0.000050

        self.elevation = elevation

    def __repr__(self):
        return f"[{self.lat_min}, {self.lat_max}];[{self.lon_min}, {self.lon_max}];[{self.elevation}]"