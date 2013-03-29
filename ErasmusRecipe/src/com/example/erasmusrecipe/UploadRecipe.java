package com.example.erasmusrecipe;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class UploadRecipe extends Activity {
	Oauth conection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_upload_recipe, menu);
        return true;
    }
    
    /*
     * When the button Upload is clicked, upload a recipe.
     */
    public void upload(View view){
    	 EditText txt1 = (EditText) findViewById(R.id.editText1);
         String title = txt1.getText().toString();
         EditText txt2 = (EditText) findViewById(R.id.editText2);
         String recipe = txt2.getText().toString();
         conection = new Oauth();
         if(areTokens()){
        	 conection.uploadRecipe(title, recipe);
        	// conection.uploadRecipeJson(title, recipe);
         }
         else{
        	 Log.wtf("ERROR UPLOAD", "There are not access token");
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
    
}
