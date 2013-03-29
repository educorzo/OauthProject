package com.example.erasmusrecipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*
 * The purpose of this class is to use some methods of the server without Access Tokens
 * 
 */
public class Petitions {
	static final String URL_LIST = "http://itks545.it.jyu.fi/edjocorz/system.php/list";
	static final String URL_RECIPE ="http://itks545.it.jyu.fi/edjocorz/system.php/recipe/";

	
	/*
	 * Return one array of string with the name of the recipes from the server
	 */
	String[] getListRecipes() {
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL_LIST);
			HttpResponse response;
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String result = convertStreamToString(entity.getContent());
			JSONArray jsonObject = new JSONArray(result); //Create a JSON object and pass the string
			//Log.wtf("HOLA",jsonObject.getJSONObject(0).getString("title"));
			String[] recipes = new String[jsonObject.length()];
			for(int i=0;i<jsonObject.length();i++){
				recipes[i]=jsonObject.getJSONObject(i).getString("title");
			}
			return recipes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		String[] nulo = new String[0];
		return nulo;
	}

	/*
	 * GEt the recipe from the server
	 */
public void getRecipe(StringBuilder title, StringBuilder author, StringBuilder recipe){
	String realUrl=URL_RECIPE+":"+title;
	try{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(realUrl);
		HttpResponse response;
		response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String result = convertStreamToString(entity.getContent());
		Log.wtf("Recipe Result", result);
		JSONObject jsonObject = new JSONObject(result);
		//title.append(jsonObject.getString("title"));
		author.append(jsonObject.getString("author"));
		recipe.append(jsonObject.getString("recipe"));
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	}
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

}
