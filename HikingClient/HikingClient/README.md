# Hiking client

Application that would read info about hike activities, generated with smart watches, and match them with pictures taken on the path. Specifically, it would read GPX files, extracts coordingates and match pictures from cloud services. Images would have to be included in a specific collection in order for this to work.

## Initial investigation

As a part of initial investigation, I found out that APIs of specific smart watch manufacturers (such as Huwavei) were not available to hobby developers. I noticed that majority of manufacturers allow their watches to be connected to a third party services. Strava is the one that is mostly used.

## Strava API

Strava has an api that allows retrieval of activities - these include all of the information that I need. All their endpoints use OAuth 2.0 authentication. The following endpoint is the one that is relevant:

    [GET] /athlete/activities

Strava has Swagger page, where developers can easily make test calls. I managed to successfully register my app, set redirect URL and make a test call on Swagger page.

Registration is possible on the following url: https://www.strava.com/settings/api

## Microsoft Graph API

Images would be retrieved from OneDrive cloud service. Microsoft requires all developers to register their app and have it approved, which could be a problem since this is a hobby project. On the other side, they support server app flow (confidential client type) -  client token authentication. 

For C#, dependency can be installed using this command:

    dotnet add package Microsoft.Graph

Alternatively, application would accept data on endpoints or local directory. It would process GPX (activity) files and Images separatelly from each directory / API. 

## Roadmap

After initial investigaion I concluded that the best approach would be to first design interfactes and data models. Altrough the best approach would be to call endpoints.

Made using C# .NET framework.