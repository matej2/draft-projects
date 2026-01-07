using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld.dto
{
    // Use the same naming convention as in GPX file
    class Gpx
    {
        DateTime date;
        Bounds bounds;
        List<Location> wpt;
        List<Rte> rte;
    }
}
