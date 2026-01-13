using HelloWorld.service.impl;

namespace HikingClient.services.impl;

class FileManager
{
    private string baseDir = "D:";
    private string validFileDir;
    private GpxParserImpl gpxParser;

    public FileManager(GpxParserImpl gpxParser)
    {
        validFileDir = Path.Combine(baseDir, "hike-app", "valid");
        this.gpxParser = gpxParser;
    }

    public void ReadSingleFile(string filePath)
    {
        String line;
        try
        {
            StreamReader sr = new StreamReader(filePath);
            gpxParser.ReadGpx(sr.BaseStream);
        }
        finally
        {
            Console.WriteLine("Executing finally block.");
        }
    }

    public void ReadFiles()
    {
        Console.WriteLine("Reading files in directory " + validFileDir);
        foreach (string file in Directory.EnumerateFiles(validFileDir, "*.gpx"))
        {
            ReadSingleFile(file);
        }
    }
}