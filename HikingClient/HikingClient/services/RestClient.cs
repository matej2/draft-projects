using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld.service
{
    internal interface RestClient
    {
        Task GetTrails();
    }
}
