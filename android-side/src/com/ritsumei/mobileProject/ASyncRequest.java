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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class ASyncRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String...cooridnate) {
    	String responseBody = "";
    	try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://cx0cjz7-afx-app000.c4sa.net/");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("lat", cooridnate[0]));
		    nameValuePairs.add(new BasicNameValuePair("lng", cooridnate[1]));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    
		    HttpResponse response = httpclient.execute(httppost);
		    if (response != null) { //there are some response
		    	responseBody = EntityUtils.toString(response.getEntity());
		    	int len = responseBody.length();
   		    	if (len > 13)
		    		return responseBody;
		    }
		} catch (Exception e) {
	        e.printStackTrace();
	    	return "";
	    }
    	return "";
    }
}