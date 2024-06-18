# ChatBot
A java program incorporating RESTful Web services that creates a chat bot for a server channel. This bot displays information about the weather in a given city as well as new Spotify releases. The program pulls in and parses JSON-formatted information from an OpenWeatherMap API corresponding to the city as well as from a Client-Credential Spotify API endpoint for new releases and returns the information through the chat bot to the server channel.

# How to Connect
Navigate to https://webchat.freenode.net/ and connect to the server that the bot will connect to (#demobot by default).

In order to use the Spotify New Releases command, the user must input their personal Spotify Client_ID and Client_Secret in the postRequest() function found in SpotifyAPI.java. Client identification can be found on https://developer.spotify.com/dashboard/login

# Commands
!commands  - Displays a list of commands currently available.

!version   - Displays the version number and date

disconnect - Disconnects the bot from the server.

Say hi to the bot! It will greet you back.


Type "weather" followed by a zipcode/city name to get the weather! Ex. **weather Richardson** or **weather 75080** 

**NOTE:** *THIS ONLY WORKS FOR UNITED STATES CITIES*

Type "Spotify New Releases" followed by the number of releases to get a list of new spotify releases! Ex. **Spotify New Releases 5**

**NOTE:** *MAX NUMBER OF RELEASES IS 10*
