using System.Xml.Linq;
using HelloWorld.dto;
using Location = HelloWorld.dto.Location;

namespace HelloWorld.service.impl
{
    class GpxParserImpl : Parser
    {
        public Gpx ReadGpx(Stream gpxFileStream)
        {
            using StreamReader reader = new StreamReader(gpxFileStream);
            string xml = reader.ReadToEnd();
            XDocument gpx = XDocument.Parse(xml);

            List<Location> points = ReadWpt(gpx);

            return new Gpx(new DateTime(), null, points, null);

        }

        public List<Location> ReadWpt(XDocument gpx)
        {
            List<Location> points = gpx.Root.Elements()
                .Where(x => x.Name.LocalName == "wpt")
                .Select<XElement, Location>(x =>
                {
                    return new Location(
                        new DateTime(),
                        x.Elements().Where(x => x.Name.LocalName == "name").FirstOrDefault()?.Value,
                        x.Elements().Where(x => x.Name.LocalName == "desc").FirstOrDefault()?.Value,
                        x.Elements().Where(x => x.Name.LocalName == "sym").FirstOrDefault()?.Value,
                        x.Elements().Where(x => x.Name.LocalName == "type").FirstOrDefault()?.Value,
                        Double.Parse(x.Attribute("lat").Value),
                        Double.Parse(x.Attribute("lon").Value));

                })
                .ToList();

            return points;
        }

        
    }
}
