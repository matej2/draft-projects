using HelloWorld.config;

namespace HelloWorld.service.impl
{
    internal class StravaClient : RestClient
    {
        private HttpClient client;

        public StravaClient() { 
            client = StravaConfig.GetHttpClient();
        }
        public async Task GetTrails()
        {
            HttpResponseMessage response = client.GetAsync("recipes").Result;

            if (response.IsSuccessStatusCode)
            {
                // Parse the response body
                String responseString = await response.Content.ReadAsStringAsync();

                Console.WriteLine(responseString);
                Console.WriteLine("Response code: " + response.StatusCode);
            }
            else
            {
                Console.WriteLine("{0} ({1})", (int)response.StatusCode, response.ReasonPhrase);
            }
        }
    }
}
