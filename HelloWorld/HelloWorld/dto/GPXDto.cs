using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld.dto
{
    // Use the same naming convention as in GPX file
    internal class GPXDto
    {
        DateTime date;
        float[,] bounds;
        GPXEntryDto[] wpt;
        GpxRteDto[] rte;
    }
}
