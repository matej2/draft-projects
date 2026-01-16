using HelloWorld.service.impl;
using HikingClient;
using HikingClient.services.impl;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddOpenApi();
builder.Services.AddAntiforgery(options =>
{    
    options.Cookie.Expiration = TimeSpan.Zero;
});

Routes routes = new Routes();
GpxParserImpl gpxParser = new GpxParserImpl();
FileManager fileManager = new FileManager(gpxParser);

fileManager.ReadFiles();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseHttpsRedirection();
app.UseAntiforgery();

app = routes
    .InitRoutes(app);

app.Run();