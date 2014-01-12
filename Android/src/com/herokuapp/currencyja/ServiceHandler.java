package com.herokuapp.currencyja;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
 


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author Deano
 *
 *ServiceHandler class that actually runs the http request and can either be GET or POST
 */
public class ServiceHandler {
	static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    /**
     * Constructor
     */
    public ServiceHandler() {
 
    }
 
    /**
     * calls the makeServiceCall that actually does the function
     * @param url
     * @param method
     * @return a string that will contain the result of the makeServiceCall
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
 
    /**
     * makes http request to server to pull data from json
     * 
     * @param url
     * @param method
     * @param params
     * @return the string that is the result of the http request
     */
    public String makeServiceCall(String url, int method,List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
 
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
