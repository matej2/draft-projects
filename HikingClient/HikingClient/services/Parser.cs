using HelloWorld.dto;

namespace HelloWorld.service
{
    internal interface Parser
    {
        Location ReadWpt();
        Location ReadRte();
        Location ReadRtept();
        public Gpx ReadGpx();
    }
}
