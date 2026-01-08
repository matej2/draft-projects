using HikingClient;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddOpenApi();
Routes routes = new Routes();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseHttpsRedirection();

app = routes.InitRoutes(app);

app.Run();