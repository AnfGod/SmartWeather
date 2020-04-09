package com.api.smartweather;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.TestApiModel;

@RestController
public class testApi{
	
	public testApi() {
		super();
	}
	
	@RequestMapping(value = "/api/testApi", method = RequestMethod.GET,
			 produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String helloWorld() {
	
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = null;
		String response = null;
	    try {
	      
	      HttpGet httpGetRequest = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=Raleigh,us&appid=e7693b306c83405b1c8cb4290fd60a5e&units=imperial");

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
	          int bytesRead = 0;
	          BufferedInputStream bis = new BufferedInputStream(inputStream);
	          while ((bytesRead = bis.read(buffer)) != -1) {
	            response = new String(buffer, 0, bytesRead);
	          }
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