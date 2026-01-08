namespace HelloWorld.config
{
    internal static class StravaConfig
    {
        private static HttpClient? httpClient;
        private static string? url;

        public static string GetUrl()
        {
            if (url == null)
            {
                url = "https://dummyjson.com/";
            }
            return url;
        }

        public static HttpClient GetHttpClient()
        {
            if (httpClient == null)
            {
                httpClient = new HttpClient();
                httpClient.BaseAddress = new Uri(GetUrl());
            }

            return httpClient;
        }


    }
}
