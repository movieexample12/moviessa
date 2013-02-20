/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.movie.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Provides utility methods for communicating with the server.
 */
final public class NetworkUtilities {
    /** The tag used to log to adb console. */
    private static final String TAG = "NetworkUtilities";
    private static final String AUTHENTICATION_CALLBACK_KEY = "Authentication-Callback";
    
    public static final String REQUEST_TOKEN_KEY = "request_token";
    public static final String AUTH_URL_KEY = "auth";
    public static final String SESSION_ID_KEY = "session_id";
    
    /** POST parameter name for the user's account name */
    public static final String API_KEY = "api_key";
    /** POST parameter name for the user's account name */
    public static final String USERNAME_KEY = "username";
    /** POST parameter name for the user's password */
    public static final String PASSWORD_KEY = "password";

    public static final String PER_PAGE_KEY = "per_page";
    
    public static final String PAGE_KEY = "page";
        
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    
    public static String TOKEN_URL = "http://api.themoviedb.org/3/authentication/token/new";
    
    public static String LOGIN_URL = "https://www.themoviedb.org/login";
    
    public static String SESSION_ID_URL = "http://api.themoviedb.org/3/authentication/session/new";

    public static String POPULAR_MOVIES_LIST_URL = "http://api.themoviedb.org/3/movie/popular";
    
    public static String TOP_RATED_MOVIES_LIST_URL = "http://api.themoviedb.org/3/movie/top_rated";

    public static String CONFIGURATION_URL = "http://api.themoviedb.org/3/configuration";
    
    private NetworkUtilities() {
    }

    /**
     * Configures the httpClient to connect to the URL provided.
     */
    public static HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        final HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }
    
    private static Map<String, String> getSessionIdMap(final ArrayList<NameValuePair> params) {
    	Map<String, String> map = new HashMap<String, String>();
    	HttpResponse response = getGetResponse(params, SESSION_ID_URL);
    	try {
    		 String content = getStringResponse(response);
             JSONObject json_object = new JSONObject(content);
             map.put(SESSION_ID_KEY, json_object.getString(SESSION_ID_KEY));
    	} catch (JSONException ex) {
			Log.e(TAG, "No request_token " + ex);
		} catch (Exception e1) {
			Log.e(TAG, "Exception while get query" + e1);
		} 
    	return map;
    }

    private static Map<String, String> getRequestTokenMap(final ArrayList<NameValuePair> params) {
    	Map<String, String> map = new HashMap<String, String>();
    	HttpResponse response = getGetResponse(params, TOKEN_URL);
    	try {
    		 String content = getStringResponse(response);
    		 JSONObject request_token = new JSONObject(content);
     		 map.put(REQUEST_TOKEN_KEY, request_token.getString(REQUEST_TOKEN_KEY));
     		 map.put(AUTH_URL_KEY, response.getHeaders(AUTHENTICATION_CALLBACK_KEY)[0].getValue());
    	} catch (JSONException ex) {
			Log.e(TAG, "No request_token " + ex);
		} catch (Exception e1) {
			Log.e(TAG, "Exception while get query" + e1);
		} 
    	return map;
    }
    
    private static HttpResponse getGetResponse(final List<NameValuePair> params, String url) {
    	HttpGet get = new HttpGet();
    	HttpResponse response = null;
    	url = url + "?" + URLEncodedUtils.format(params, "utf-8");
    	try {
	    	get = new HttpGet(new URI(url));
	        get.addHeader("Accept", "application/json");
	        get.addHeader("Content-Encoding", "UTF-8");
	        response = getHttpClient().execute(get);
    	} catch (URISyntaxException ex) {
    		Log.e(TAG, "getGetResponse. Check uri: " + url);
    	} catch (IOException ex) {
    		Log.e(TAG, "getGetResponse. " + ex);
    	}
        return response;
    }
    
    private static String getStringResponse(HttpResponse resp) {
        BufferedReader ireader = null; 
        String authToken = null;	
        try {
            InputStream istream = (resp.getEntity() != null) ? resp.getEntity().getContent()
                    : null;
            if (istream != null) {
                ireader = new BufferedReader(new InputStreamReader(istream));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = ireader.readLine()) != null) {
                	sb.append(line.trim());
                }
                authToken = sb.toString();
            }
	        if ((resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) && (authToken != null) && (authToken.length() > 0)) {
	            Log.v(TAG, "Successful authentication");
	        } else {
	            Log.e(TAG, "Error authenticating" + resp.getStatusLine());
	            authToken = null;
	        }
	    } catch (Exception e) {
	         Log.e(TAG, "Exception when getting authtoken", e);
	    } finally {
	        if (ireader != null) {
	        	try {
	        		ireader.close();
				} catch (IOException e) {
					Log.e(TAG, "Exception when try close ireader " + e);
			    }
	        }
	    }
        if (authToken == null) {
            throw new IllegalStateException("Can not obtain session_id");
        }
        return authToken;
    }
    
    /**
     * Connects to the SampleSync test server, authenticates the provided
     * username and password.
     *
     * @param username The server account username
     * @param password The server account password
     * @return String The authentication token returned by the server (or null)
     */
    public static  Map<String, String> getRequestTokenId(final String username, final String password, String api) {
    	final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(API_KEY, api));
        params.add(new BasicNameValuePair(USERNAME_KEY, username));
        params.add(new BasicNameValuePair(PASSWORD_KEY, password));
        Map<String, String> request_token_map = getRequestTokenMap(params);
    	return request_token_map;
    }
    
    private static List<NameValuePair> getApiParamsMap(String api) {
    	final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(API_KEY, api));
        return params;
    }
    
    public static  Map<String, String> getSessionId(final String username, final String password, String api, String allowed_request_token) {
        List<NameValuePair> params = getApiParamsMap(api);
        Map<String, String> map = new HashMap<String, String>();
//        params.add(new BasicNameValuePair(REQUEST_TOKEN_KEY, allowed_request_token));
//        Log.i(TAG, "before call session_id");
//        map = getSessionIdMap(params);
//        Log.i(TAG, " session_id " + map.get(SESSION_ID_KEY));
        map.put(NetworkUtilities.SESSION_ID_KEY, "3ddc1f1913e8113bc65b89006d93c1d476c8a917");
        return map;
    }
    
    public static List<RawMovie> sync(Account account, String authtoken,
            long lastSyncMarker, List<RawMovie> dirtyMovies) {
    	return new ArrayList<RawMovie>();
    }
    
    public static Object performGetRequest(String api, String url, @SuppressWarnings("rawtypes") Class T) {
    	
        List<NameValuePair> params = getApiParamsMap(api);
        try {
	        final HttpResponse resp = getGetResponse(params, url);
	        final String response = EntityUtils.toString(resp.getEntity());
	        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        	Gson gson = new Gson();
	            @SuppressWarnings("unchecked")
				Object listElement = gson.fromJson(response, T);
	            return listElement;
	        } else {
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
	                Log.e(TAG, "Authentication exception in performGetRequest");
	                throw new AuthenticationException();
	            } else {
	                Log.e(TAG, "Server error in performGetRequest: " + resp.getStatusLine());
	                throw new IOException();
	            }
	        }
        } catch (Exception ex) {
        	Log.e(TAG, "Exception " + ex.getMessage());
        }

        return null;
    }
    
    public static List<RawMovie> getPopularMoviesList(String api, int page, int per_page) {
        List<RawMovie> serverDirtyList = new ArrayList<RawMovie>();
        RawMovieList listElement = (RawMovieList)performGetRequest(api, TOP_RATED_MOVIES_LIST_URL, RawMovieList.class);
        serverDirtyList = new ArrayList<RawMovie>(listElement.results);
        return serverDirtyList;
    }
    
    public static RawConfigurationList getConfiguration(String api) {
    	RawConfigurationList listElement = (RawConfigurationList)performGetRequest(api, CONFIGURATION_URL, RawConfigurationList.class);
        return listElement;
    }
}
