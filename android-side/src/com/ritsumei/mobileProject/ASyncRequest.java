/**
* Async task to handle request with server.
* @author huydx 
* @version 0.9
*/
package com.ritsumei.mobileProject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class ASyncRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String...coordinate) {
    	String responseBody = "";
    	try {
    		if (coordinate.length == 3) { //get by lat and lng
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(coordinate[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("lat", coordinate[1]));
			    nameValuePairs.add(new BasicNameValuePair("lng", coordinate[2]));
			    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    
			    HttpResponse response = httpclient.execute(httppost);
			    if (response != null) { //there are some response
			    	responseBody = EntityUtils.toString(response.getEntity());
			    	int len = responseBody.length();
	   		    	if (len > 13)
			    		return responseBody;
			    }
    		}
    		else if (coordinate.length == 1) { //get all events
    			HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(coordinate[0]);
				
			    HttpResponse response = httpclient.execute(httpget);
			    if (response != null) { //there are some response
			    	responseBody = EntityUtils.toString(response.getEntity());
			    	int len = responseBody.length();
	   		    	if (len > 13)
			    		return responseBody;
			    }
    		}
		} catch (Exception e) {
	        e.printStackTrace();
	    	return "";
	    }
    	return "";
    }
}