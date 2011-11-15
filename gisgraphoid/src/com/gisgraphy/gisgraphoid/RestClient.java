package com.gisgraphy.gisgraphoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RestClient {

	private static final String ENCODING = "UTF-8";
	private String webServiceUrl;

	public RestClient(String webServiceUrl) {
		if (webServiceUrl == null) {
			throw new IllegalArgumentException("can not create a restclient for null URL");
		}
		if (!webServiceUrl.endsWith("/")) {
			webServiceUrl = webServiceUrl + "/";
		}
		this.webServiceUrl = webServiceUrl;

	}


	public <T> T get(String methodName, Class<T> classToBeBound, Map<String, String> params) {
		if (methodName == null) {
			methodName = "";
		}
		if (methodName.startsWith("/")) {
			methodName = methodName.substring(1);
		}
		String getUrl = webServiceUrl + methodName;

		int i = 0;
		StringBuffer sb = new StringBuffer(getUrl);
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}

				try {
					sb.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue(), ENCODING));
				} catch (UnsupportedEncodingException e) {
					Log.e("RestClient", "can not encode REST URL : " + e);
				}
				i++;
			}
		}
		InputStream in ;
		Log.d("RestClient: ", "getUrl = " + getUrl);
			try {
				    in = getRemoteContent(getUrl);
					Type returnedType = (Type) new TypeToken<T>() {
					}.getType();
					T returnObjects = new Gson().fromJson(new BufferedReader(new InputStreamReader(in)),  returnedType);
					Log.d("result", String.valueOf(returnObjects));
					return returnObjects;

				} catch (Exception e) {
					String errorMessage= "Error during parsing of Gisgraphy response (has Gisgraphy API changed or feed is not json ?) : "+e.getMessage();
					Log.d("RestClient",errorMessage,e);
					throw new RuntimeException(errorMessage);
				}
		/*DefaultHttpClient httpClient = new DefaultHttpClient(myParams);
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(getUrl);

		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (Exception e) {
			Log.e("RestClient:", "error getting URL '" + getUrl + "' : " + e.getMessage());
		}
		if (httpResponse != null && httpResponse.getEntity() != null) {
			try {
				responseString = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				Log.e("RestClient", "can not execute POST request" + e.getMessage());
			}
		}
*/
		
	}

	/*
	 * public static JSONObject Object(Object o) { try { return new
	 * JSONObject(new Gson().toJson(o)); } catch (JSONException e) {
	 * e.printStackTrace(); } return null; }
	 */

	private InputStream getRemoteContent(String urlString) throws IOException {
		if (urlString==null){
			throw new IOException("can not retrieve the content of a null url");
		}
		InputStream in = null;
		int responseCode = -1;

		URL url;
		try {
			url = new URL(urlString);
		} catch (Exception e) {
			throw new IOException(urlString+" is not a valid url");
		}
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			responseCode = httpConn.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
				//String responseAsString = Helper.inputStreamToString(in);
			} else {
				String errorMessage = "calling "+urlString+" return an error : "+responseCode;
				Log.e("Restclient", errorMessage);
				throw new IOException(errorMessage);
			}

		return in;
	}

	/*
	 * public void clearCookies() { httpClient.getCookieStore().clear(); }
	 */

	/*
	 * public void abort() { try { if (httpClient != null) { Log.i("restClient",
	 * "aborting HTTP post method " + httpPost); httpPost.abort(); } } catch
	 * (Exception e) { Log.e("restClient", "Error aborting HTTP method : " +
	 * e.getMessage()); } }
	 */

	public String getWebServiceUrl() {
		return webServiceUrl;
	}

}
