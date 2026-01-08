using System.Runtime.CompilerServices;
using HelloWorld.service.impl;

namespace HikingClient;

public class Routes
{
    private StravaClient stravaClient = new();

    public WebApplication InitRoutes(WebApplication app)
    {
        app.MapGet("/weatherforecast", () =>
            {
                stravaClient.GetTrails();
                return "Weather Forecast";
            })
            .WithName("GetWeatherForecast");

        return app;
    }
}