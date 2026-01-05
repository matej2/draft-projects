using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld.dto
{
    // Use the same naming convention as in GPX file
    internal class GPXEntryDto
    {
        double ele;
        DateTime time;
        String name;
        String desc;
        String sym;
        String type;
        double _lat;
        double _lon;
    }
}
