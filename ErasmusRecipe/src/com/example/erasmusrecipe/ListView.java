package com.example.erasmusrecipe;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;

public class ListView extends ListActivity{
	ArrayList<HashMap<String,String>> Eventos;
	String[] from=new String[] {"title"};
	int[] to=new int[]{R.id.titleRecipe};
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_list_view);
		
		 Petitions petition = new Petitions();
		 String[] recipes=petition.getListRecipes();
		 Eventos =new ArrayList<HashMap<String,String>>();
		 //Show all the recipes form the server
		 for(int i=0;i<recipes.length;i++){
			 HashMap<String,String> datosEvento=new HashMap<String,String>();
			 datosEvento.put("title",recipes[i]);
			 Eventos.add(datosEvento);
		 }
		 SimpleAdapter ListadoAdapter=new SimpleAdapter(this, Eventos, R.layout.row,from, to);
		  setListAdapter(ListadoAdapter);
	 }
	 
	 

	    /*
	     * 
	     *Some row has been clicked and it show the all recipe
	     * 
	     */
	    protected void onListItemClick(android.widget.ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        Intent intent = new Intent(this,RecipeView.class);
	        Log.wtf("TITLE", Eventos.get(position).get("title"));
	        intent.putExtra("title",Eventos.get(position).get("title"));
	        startActivity(intent);
	    }
}
