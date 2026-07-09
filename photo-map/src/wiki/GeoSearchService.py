class GeoSearchService:
    def __init__(self, ):

        def dd():
            # 3. Geosearch iskanje, OMEJENO samo na slike (Namespace.FILE)
            # Knjižnica Wikipedia-API bo v ozadju v klic dodala '&ggsnamespace=6'
            results = wiki.geosearch(
                coord=wikipediaapi.GeoPoint(51.5074, -0.1278),
                sort=GeoSearchSort.DISTANCE,
                globe=Globe.EARTH,
                radius=1000,
                ns=Namespace.FILE  # KLJUČNA LINIJA: Išče samo datoteke/slike!
            )