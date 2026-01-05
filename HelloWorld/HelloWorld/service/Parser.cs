using HelloWorld.dto;
using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld.service
{
    internal interface Parser
    {
        GPXEntryDto ReadWpt();
        GPXEntryDto ReadRte();
        GPXEntryDto ReadRtept();
        public GPXDto ReadGpx();
    }
}
