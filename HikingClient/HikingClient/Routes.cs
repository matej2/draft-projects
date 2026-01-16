using System.Xml.Linq;

namespace HikingClient;

public class Routes
{

    public WebApplication InitRoutes(WebApplication app)
    {
        app.MapPost("/processgpx", async (IFormFile file) =>
            {
                if (file == null || file.Length == 0)
                    return Results.BadRequest("No file uploaded");
                using Stream stream = file.OpenReadStream();
                using var reader = new StreamReader(stream);

                string xml = await reader.ReadToEndAsync();
                XDocument gpx = XDocument.Parse(xml);

                var points = gpx.Descendants()
                    .Where(x => x.Name.LocalName == "trkpt")
                    .Select(x => new
                    {
                        Lat = (double)x.Attribute("lat"), Lon = (double)x.Attribute("lon"),
                        Ele = (double?)x.Element(x.Name.Namespace + "ele")
                    })
                    .ToList();
                return Results.Ok(points);
            })
            .Accepts<IFormFile>("application/gpx+xml")
            .WithName("GetWeatherForecast"); 
        
        return app;
    }
}