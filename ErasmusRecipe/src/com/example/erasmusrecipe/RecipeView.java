package com.example.erasmusrecipe;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class RecipeView extends Activity {

	/*
	 * Shows the recipe on the screen.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_view);
		if (this.getIntent().getStringExtra("title") != null) {
			Petitions petition = new Petitions();
			StringBuilder title = new StringBuilder(this.getIntent().getStringExtra("title"));
			StringBuilder author = new StringBuilder();
			StringBuilder recipe = new StringBuilder();
			petition.getRecipe(title, author, recipe);
			Log.wtf("author", author.toString());
			Log.wtf("title", title.toString());
			Log.wtf("recipe", recipe.toString());
			
			TextView txt = (TextView) findViewById(R.id.authorW);  
		    txt.setText(author);
		    TextView txt2 = (TextView) findViewById(R.id.recipeW);  
		    txt2.setText(recipe);
		    TextView txt3 = (TextView) findViewById(R.id.titleW);  
		    txt3.setText(title);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_recipe_view, menu);
		return true;
	}
}
