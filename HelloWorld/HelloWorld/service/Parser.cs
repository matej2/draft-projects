using HelloWorld.dto;
using System;
using System.Collections.Generic;
using System.Text;

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
