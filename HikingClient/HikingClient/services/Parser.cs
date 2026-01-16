using System.Xml.Linq;
using HelloWorld.dto;

namespace HelloWorld.service
{
    internal interface Parser
    {
        List<Location> ReadWpt(XDocument gpx);

    }
}
