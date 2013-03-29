package com.example.erasmusrecipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class Oauth {
	static final String CONSUMER_KEY = "a6ecb6c63d46228898afe03206f8cf1a05144777b"; // CLIENT
	static final String CONSUMER_SECRET = "632eafc6ce66f8052690f1880472b6d0"; // CLIENT
	static final String REQUEST_TOKEN_URL = "http://itks545.it.jyu.fi/edjocorz/system.php/request_token";
	static final String ACCESS_TOKEN_URL = "http://itks545.it.jyu.fi/edjocorz/system.php/access_token";
	static final String AUTHORIZE_URL = "http://itks545.it.jyu.fi/edjocorz/authorize.php";
	static final String HELLO_URL = "http://itks545.it.jyu.fi/edjocorz/require-auth/:name";
	static final String URL = "http://itks545.it.jyu.fi/edjocorz/system.php/upload";
	static final String UPLOAD_JSON_RECIPE_URL = "http://itks545.it.jyu.fi/edjocorz/system.php/uploadJSON";
	private String TOKEN = "";
	private String TOKEN_SECRET = "";
	private String ACCESS_TOKEN = "";
	private String ACCESS_TOKEN_SECRET = "";
	private long timeStamp;

	/*
	 * Sets the time in the object
	 */
	public long setTimeStamp() {
		timeStamp = System.currentTimeMillis() / 1000;// Must be in seconds not
														// in milliseconds
		return timeStamp;
	}

	/*
	 * Return a Nonce. I'm using the same Nonce.
	 */
	public String getNonce() {
		return "1b1b110f24cad2d78db10f0f7661def3";
	}

	public String getAccessToken() {
		return ACCESS_TOKEN;
	}

	public String getAccessTokenSecret() {
		return ACCESS_TOKEN_SECRET;
	}

	public void setAccessTokens(String token, String secret) {
		ACCESS_TOKEN = token;
		ACCESS_TOKEN_SECRET = secret;
	}

	/*
	 * Create a signature with HMAC_SHA1 method
	 */
	private static String hmac_sha1(String value, String key) {
		try {
			SecretKey secretKey = null;
			byte[] keyBytes = key.getBytes();
			secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(secretKey);
			byte[] text = value.getBytes();
			return new String(Base64.encode(mac.doFinal(text), 0)).trim();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This method obtain Token and token_secret from the server and return a
	 * URL for the user. The purpose of this url is to get the authorization of
	 * the user.
	 */
	public String authorization() {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String get = "";
		try {
			baseString = "GET&" + URLEncoder.encode(REQUEST_TOKEN_URL, "UTF-8")
					+ "&";
			parameters = "oauth_consumer_key=" + CONSUMER_KEY + "&oauth_nonce="
					+ getNonce() + "&oauth_signature_method=HMAC-SHA1"
					+ "&oauth_timestamp=" + setTimeStamp()
					+ "&oauth_version=1.0";
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			signature = URLEncoder.encode(
					hmac_sha1(baseString, CONSUMER_SECRET + "&"), "UTF-8");
			get = REQUEST_TOKEN_URL + "?oauth_version=1.0&oauth_nonce="
					+ getNonce() + "&oauth_timestamp=" + timeStamp
					+ "&oauth_consumer_key=" + CONSUMER_KEY
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result;
		// Log.wtf("GET", get);
		try { // Make the http conection
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(get);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = convertStreamToString(entity.getContent());
			Log.wtf("Ver", result);
			setTokens(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.wtf("ERROR CONECTION", "HTTP");
		}
		return AUTHORIZE_URL + "?oauth_token=" + TOKEN; // Return the url for
														// the user.
	}

	/*
	 * This method makes a petition to get acess tokens and set them
	 */
	public void obtainAccessToken() {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String get = "";
		try {// Builds a baseString, makes the signature and construct the get
			baseString = "GET&" + URLEncoder.encode(ACCESS_TOKEN_URL, "UTF-8")
					+ "&";
			parameters = "oauth_consumer_key=" + CONSUMER_KEY + "&oauth_nonce="
					+ getNonce() + "&oauth_signature_method=HMAC-SHA1"
					+ "&oauth_timestamp=" + setTimeStamp() + "&oauth_token="
					+ TOKEN + "&oauth_version=1.0";
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			signature = URLEncoder
					.encode(hmac_sha1(baseString, CONSUMER_SECRET + "&"
							+ TOKEN_SECRET), "UTF-8");
			get = ACCESS_TOKEN_URL + "?oauth_version=1.0&oauth_nonce="
					+ getNonce() + "&oauth_timestamp=" + timeStamp
					+ "&oauth_consumer_key=" + CONSUMER_KEY + "&oauth_token="
					+ TOKEN
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result;
		try { // Realize a connection with the server
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(get);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = convertStreamToString(entity.getContent());
			setAccessTokens(result); // Set the Access Token and token secret
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * This function is for try that all the oauth system works
	 */
	public String exampleAccess() {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String get = "";
		try {
			baseString = "GET&" + URLEncoder.encode(HELLO_URL, "UTF-8") + "&";
			parameters = "oauth_consumer_key=" + CONSUMER_KEY + "&oauth_nonce="
					+ getNonce() + "&oauth_signature_method=HMAC-SHA1"
					+ "&oauth_timestamp=" + setTimeStamp() + "&oauth_token="
					+ ACCESS_TOKEN + "&oauth_version=1.0";
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			Log.wtf("base", baseString);
			signature = URLEncoder.encode(
					hmac_sha1(baseString, CONSUMER_SECRET + "&"
							+ ACCESS_TOKEN_SECRET), "UTF-8");
			get = HELLO_URL + "?oauth_version=1.0&oauth_nonce=" + getNonce()
					+ "&oauth_timestamp=" + timeStamp + "&oauth_consumer_key="
					+ CONSUMER_KEY + "&oauth_token=" + ACCESS_TOKEN
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.wtf("Direct_messages", get);
		return get;
	}

	/*
	 * This method extracts the access token and the access token secret from
	 * one string and sets them in the object
	 */
	private void setAccessTokens(String result) {
		Log.wtf("RESULT", result);
		ACCESS_TOKEN = result.substring(12,
				result.indexOf("&oauth_token_secret="));// 12
		ACCESS_TOKEN_SECRET = result.substring(result
				.indexOf("&oauth_token_secret=") + 20); // IT INCLUDES A SPACE
														// AT THE END !!! (1
														// hour to discover it!
														// I love this game! )
		ACCESS_TOKEN_SECRET = ACCESS_TOKEN_SECRET.substring(0,
				ACCESS_TOKEN_SECRET.length() - 1); // IT ERASE THE SPACE !!
		// I feel that compiler does not consider my comments :(
	}

	/*
	 * This method extracts the oauth_token and the oauth_token_secret from one
	 * string and sets them in the object.
	 */
	private void setTokens(String result) {

		TOKEN = result.substring(result.indexOf("&oauth_token=") + 13,
				result.indexOf("&oauth_token_secret="));
		TOKEN_SECRET = result.substring(
				result.indexOf("&oauth_token_secret=") + 20,
				result.indexOf("&xoauth_token_ttl="));
	}

	/*
	 * Convert a inputStream in a String type
	 */
	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/*
	 * Upload a recipe. It is necessary to have access tokens
	 */
	public void uploadRecipe(String title, String recipe) {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String titleCode = "";
		String recipeCode = "";
		try {
			// To encode the title and the recipe is necessary or we will have
			// problems with spaces and other characters
			titleCode = URLEncoder.encode(title, "UTF-8");
			titleCode = URLEncoder.encode(titleCode, "UTF-8");
			recipeCode = URLEncoder.encode(recipe, "UTF-8");
			recipeCode = URLEncoder.encode(recipeCode, "UTF-8");
			// Construct the baseString in order to do the signature
			baseString = "POST&" + URLEncoder.encode(URL, "UTF-8") + "&";
			parameters = "oauth_consumer_key=" + CONSUMER_KEY + "&oauth_nonce="
					+ getNonce() + "&oauth_signature_method=HMAC-SHA1"
					+ "&oauth_timestamp=" + setTimeStamp() + "&oauth_token="
					+ ACCESS_TOKEN + "&oauth_version=1.0" + "&recipe="
					+ recipeCode + "&title=" + titleCode;
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			Log.wtf("base", baseString);
			signature = hmac_sha1(baseString, CONSUMER_SECRET + "&"
					+ ACCESS_TOKEN_SECRET); // THE SIGNATURE WILL ENCODE LATER
											// (in the post)
			// Let's construct the POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
					.add(new BasicNameValuePair("oauth_nonce", getNonce()));
			nameValuePairs.add(new BasicNameValuePair("oauth_timestamp", Long
					.toString(timeStamp)));
			nameValuePairs.add(new BasicNameValuePair("oauth_consumer_key",
					CONSUMER_KEY));
			nameValuePairs.add(new BasicNameValuePair("oauth_token",
					ACCESS_TOKEN));
			nameValuePairs.add(new BasicNameValuePair("oauth_signature_method",
					"HMAC-SHA1"));
			nameValuePairs.add(new BasicNameValuePair("oauth_signature",
					signature));
			nameValuePairs.add(new BasicNameValuePair("oauth_version", "1.0"));
			nameValuePairs.add(new BasicNameValuePair("title", title));
			nameValuePairs.add(new BasicNameValuePair("recipe", recipe));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			httppost.setHeader("Authorization", "Oauth"); // Set Header
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String result = convertStreamToString(entity.getContent());
			Log.wtf("RESULT", result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Upload a recipe. It is necessary to have access tokens. We send the
	 * recipe and his title with just one parameter with JSON format.
	 */
	public void uploadRecipeJson(String title, String recipe) {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String data = "";
		String dataCode = "";
		JSONObject object = new JSONObject();
		try {
			object.put("title", title);// Assign the title and the recipe to the
										// JSONObject
			object.put("recipe", recipe);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.wtf("ERROR JSON", "Building JSONObject problem");
		}
		data = object.toString();
		// data=data.replace("\"","'");
		Log.wtf("DATA", data);
		try {
			// To encode the title and the recipe is necessary or we will have
			// problems with spaces and other characters
			dataCode = URLEncoder.encode(data, "UTF-8");
			dataCode = URLEncoder.encode(dataCode, "UTF-8");

			// Construct the baseString in order to do the signature
			baseString = "POST&" + URLEncoder.encode(URL, "UTF-8") + "&";
			parameters = "data=" + dataCode + "&oauth_consumer_key="
					+ CONSUMER_KEY + "&oauth_nonce=" + getNonce()
					+ "&oauth_signature_method=HMAC-SHA1" + "&oauth_timestamp="
					+ setTimeStamp() + "&oauth_token=" + ACCESS_TOKEN
					+ "&oauth_version=1.0";
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			Log.wtf("base", baseString);
			signature = hmac_sha1(baseString, CONSUMER_SECRET + "&"
					+ ACCESS_TOKEN_SECRET); // THE SIGNATURE WILL ENCODE LATER
											// (in the post)
			// Let's construct the POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
					.add(new BasicNameValuePair("oauth_nonce", getNonce()));
			nameValuePairs.add(new BasicNameValuePair("oauth_timestamp", Long
					.toString(timeStamp)));
			nameValuePairs.add(new BasicNameValuePair("oauth_consumer_key",
					CONSUMER_KEY));
			nameValuePairs.add(new BasicNameValuePair("oauth_token",
					ACCESS_TOKEN));
			nameValuePairs.add(new BasicNameValuePair("oauth_signature_method",
					"HMAC-SHA1"));
			nameValuePairs.add(new BasicNameValuePair("oauth_signature",
					signature));
			nameValuePairs.add(new BasicNameValuePair("oauth_version", "1.0"));
			nameValuePairs.add(new BasicNameValuePair("data", data));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(UPLOAD_JSON_RECIPE_URL);
			httppost.setHeader("Authorization", "Oauth"); // Set Header
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String result = convertStreamToString(entity.getContent());
			 Log.wtf("RESULT", result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
