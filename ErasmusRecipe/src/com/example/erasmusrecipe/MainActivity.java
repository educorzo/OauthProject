package com.example.erasmusrecipe;

import com.example.erasmusrecipe.DataBase;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Oauth conection;
    @SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conection = new Oauth();
        if(!areTokens()){
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setData(Uri.parse(conection.authorization()));
        	this.startActivity(intent);
        //	Log.wtf("Conection",conection.authorization());
        	Button resetButton=(Button)findViewById(R.id.button1);
        	resetButton.setVisibility(0);
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void authorize(View view) {
    	conection.obtainAccessToken();
    	insertDB(conection.getAccessToken(),conection.getAccessTokenSecret());
    		Log.wtf("I HAVE", "ACCESS_TOKEN");
    		view.setVisibility(View.GONE);
    }
    

    
    /*
	 * Save a position and his date in the Data base
	 */
	public void insertDB(String accessToken, String accessTokenSecret) {
		DataBase usdbh = new DataBase(this, "DB", null, 1);
		SQLiteDatabase db = usdbh.getWritableDatabase();
		if (db != null) {
				db.execSQL("INSERT INTO Access (token, tokenSecret) VALUES ('"+ accessToken + "', '"  + accessTokenSecret + "')");
				Log.wtf("DATABASE", "SAVED TOKENS");
			db.close();
		}
	}
	
	/*
	 * 
	 * Erase all tokens tokens
	 */
	public void eraseTokens(View view){
		DataBase usdbh = new DataBase(this, "DB", null, 1);
		SQLiteDatabase db = usdbh.getWritableDatabase();
		if (db != null) {
				db.execSQL("DELETE FROM Access");
				Log.wtf("DATABASE", "TOKENS WERE DELETED");
			db.close();
		}
		
	}
	
	/*
	 * Return True if there are AccessToken in the dataBase.
	 * If there are access token, it includes in the Oauth object conection
	 */
    public boolean areTokens(){
    	boolean result=false;
    	DataBase usdbh = new DataBase(this, "DB", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        if(db != null){
        	String[] args = new String[] {"1"};
        	Cursor c= db.rawQuery("SELECT * FROM Access WHERE '1'=?",args);
        	if(c.getCount()>0){
        		result=true;
        		c.moveToFirst();
        		conection.setAccessTokens(c.getString(0),c.getString(1));
        	}
        }
        db.close();
    	return result;
    }
    
	/*
	 * Go to activity ListView
	 */
	public void goToList(View view) {
		Intent intent = new Intent(this, ListView.class);
		startActivity(intent);
	}
	/*
	 * Go to activity UploadRecipe
	 */
	public void goToUpload(View view) {
		Intent intent = new Intent(this, UploadRecipe.class);
		startActivity(intent);
	}
}
