import requests


class AuthProvider:
    @staticmethod
    def get_wikipedia_client():
        response_json = requests.get("https://commons.wikimedia.org/w/api.php?action=query&generator=images&gimlimit=500&iiprop=timestamp%7Cuser%7Cuserid%7Ccomment%7Ccanonicaltitle%7Curl%7Csize%7Cdimensions%7Csha1%7Cmime%7Cthumbmime%7Cmediatype%7Cbitdepth&prop=imageinfo&redirects=1&titles=Cat&format=json").json()


        print(response_json)

        return response_json

