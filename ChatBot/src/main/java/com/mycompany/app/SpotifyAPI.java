package com.mycompany.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * This class is used for reading new spotify releases from the 
 * Spotify API (new-releases endpoint).
 */

public class SpotifyAPI
{
	private String accessToken;
	
	// This function returns the access token
	public String getAccessToken() { return this.accessToken; }
		
	
	/* This function parses string content into a Json object
	 * Parameters: String containing content from an API URL
	 * Return: JsonObject containing the Json formatted version of the content
	 */
	public JsonObject JsonParse(String content) 
	{
		// Parse API string content into json object
		JsonObject jsonObj = new JsonParser().parse(content).getAsJsonObject();
		return jsonObj;
	}
	
	
	/* This function returns new spotify releases
	 * Parameters: number of releases to return
	 */
	public String[] getNewSpotifyReleases(int numReleases) throws IOException
	{
		String[] releases = new String[numReleases];
		
		// Check if the spotify API access token is valid
		boolean validToken = validateAccessToken();
		
		// If the access token is invalid, refresh authentication
		if(!validToken)
			refreshAuthentication();
		
		// Get new spotify releases (numSongs amount of releases)
		HttpResponse httpresponse = getRequest(numReleases);
		
		// Read in http response from GET
		String content = readHttpResponse(httpresponse);
	    
	    // Parse API string content into json object
	    JsonObject jsonObj = JsonParse(content);
		
	    // Fill releases array with new releases
		for(int i = 0; i < numReleases; i++)
		{
			releases[i] = (((JsonObject) jsonObj.getAsJsonObject("albums").getAsJsonArray("items").get(i)).get("name").getAsString() 
					+ " by " + ((JsonObject)((JsonObject) jsonObj.getAsJsonObject("albums").getAsJsonArray("items").get(i)).getAsJsonArray("artists").get(0)).get("name").getAsString());
		}
		
		return releases;
	}
	
	
	/* This function checks if the current access token for the Spotify API
	 * Client Credential Flow is authorized.
	 * Parameters: The access token
	 * Return: True if valid, false if invalid
	 */
	public boolean validateAccessToken() throws IOException
	{
		// Try to get 1 new Spotify release from Spotify API
		HttpResponse httpresponse = getRequest(1);
		
		// Check if HTTP 401 Error: Unauthorized
		if(httpresponse.getStatusLine().getStatusCode() == 401)
		{
			return false;
		}
		
		return true;
	}
	
	
	/* This function refreshes current connection to Spotify API if authentication token
	 * has expired.
	 */
	public void refreshAuthentication() throws IOException
	{
	    // Set up HttpResponse to read response from the POST
	    HttpResponse httpresponse = postRequest();

	    // Read in new access token
	    this.accessToken = readPostAccessToken(httpresponse);
	}
	
	
	/* This function sends a POST request for the 
	 * Spotify Client Credential flow token API.
	 * Return: HttpResponse from POST Request
	 */
	public HttpResponse postRequest() throws IOException
	{
		// Spotify client_id and client_secret
		String client_id = "afc6c3b25be04d4ca5543f9d71a11fe6";
		String client_secret = "555d5f0163cc497197d563c3a6ee4611";
		String userCredentials = client_id + ":" + client_secret;
		
		// Encode the user credentials into a basic OAth base64 code
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
		
		// Set up default CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// Set up HttpPost for the Spotify client credential flow API
		HttpPost httppost = new HttpPost("https://accounts.spotify.com/api/token");

			    
		// Load body parameters
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));
		httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			    
		// Load header parameters
		httppost.addHeader("Authorization", basicAuth);

		// Return HttpResponse to read response from the POST
		return httpclient.execute(httppost);
	}
	
	
	/* This function sends a GET request for the 
	 * Spotify Client Credential flow token API.
	 * Return: HttpResponse from POST Request
	 */
	public HttpResponse getRequest(int numSongs) throws IOException
	{
		// String to hold bearer authentication
		String bearerAuth = "Bearer " + this.accessToken;
		
		//Creating a CloseableHttpClient object
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	    
	    // Set up HttpGet request to spotify API "new releases" endpoint
		HttpGet httpget = new HttpGet("https://api.spotify.com/v1/browse/new-releases?country=US&limit=" + numSongs);
		
		// Add bearer authentication to header
		httpget.addHeader("Authorization",  bearerAuth);
		
		// Return the HttpResponse
		return httpclient.execute(httpget);
	}
	
	
	/* This function returns the contents of an http response
	 * as a string
	 * Parameters: HttpResponse
	 * Return: String content of http response
	 */
	public String readHttpResponse(HttpResponse httpresponse) throws IOException
	{
		// Set up a scanner to read content from http POST response
		Scanner scan = new Scanner(httpresponse.getEntity().getContent());

		// String to hold contents of http POST response
	    String content = new String();
	    
	    // Read in contents of http POST response to content
	    while(scan.hasNext()) {
	       content += scan.nextLine();
	    }
	    
	    // Close scanner
	    scan.close();
	    
	    return content;
	}
	
	
	/*
	 * This function retrieves a spotify access token 
	 * from POST HttpResponse.
	 */
	public String readPostAccessToken(HttpResponse httpresponse) throws IOException
	{
		// String to hold contents of http POST response
	    String content = readHttpResponse(httpresponse);
	    
	    // Parse API string content into json object
	    JsonObject jsonObj = JsonParse(content);
	    
	    // Return access token
	    return jsonObj.get("access_token").getAsString();
	}
}