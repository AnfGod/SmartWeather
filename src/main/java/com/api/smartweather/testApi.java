package com.api.smartweather;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testApi{
	
	public testApi() {
		super();
	}
	
	@RequestMapping(value = "/api/weather/{lat}/{lon:.+}", method = RequestMethod.GET,
			 produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String helloWorld(@PathVariable("lat") String lat, 
            @PathVariable("lon") String lon) {
	
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = null;
		String response = null;
	    try {
	      String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&appid=e7693b306c83405b1c8cb4290fd60a5e&units=imperial";
	      HttpGet httpGetRequest = new HttpGet(url);

	      // Execute HTTP request
	      httpResponse = httpClient.execute(httpGetRequest);

	      // Get hold of the response entity
	      HttpEntity entity = httpResponse.getEntity();

	      // If the response does not enclose an entity, there is no need
	      // to bother about connection release
	      byte[] buffer = new byte[1024];
	      if (entity != null) {
	        InputStream inputStream = entity.getContent();
	        try {
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            response = sb.toString();
	        } catch (IOException ioException) {
	          // In case of an IOException the connection will be released
	          // back to the connection manager automatically
	          ioException.printStackTrace();
	        } catch (RuntimeException runtimeException) {
	          // In case of an unexpected exception you may want to abort
	          // the HTTP request in order to shut down the underlying
	          // connection immediately.
	          httpGetRequest.abort();
	          runtimeException.printStackTrace();
	        } finally {
	          // Closing the input stream will trigger connection release
	          try {
	            inputStream.close();
	          } catch (Exception ignore) {
	          }
	        }
	      }
	    } catch (ClientProtocolException e) {
	      // thrown by httpClient.execute(httpGetRequest)
	      e.printStackTrace();
	    } catch (IOException e) {
	      // thrown by entity.getContent();
	      e.printStackTrace();
	    } finally {
	      // When HttpClient instance is no longer needed,
	      // shut down the connection manager to ensure
	      // immediate deallocation of all system resources
	      httpClient.getConnectionManager().shutdown();
	    }
	    return response;
	  }
}