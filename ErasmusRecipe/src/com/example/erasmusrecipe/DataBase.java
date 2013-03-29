package com.example.erasmusrecipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.location.LocationListener;

public class DataBase extends  SQLiteOpenHelper {
		String sqlCreate = "CREATE TABLE Access (token TEXT PRIMARY KEY, tokenSecret TEXT)";
		String sqlCreate2 = "CREATE TABLE Recipes(title TEXT PRIMARY KEY, author TEXT, recipe TEXT)";
		
		public DataBase(Context contexto, String nombre,CursorFactory factory, int version) {
			super(contexto, nombre, factory, version);
		}
		
		public DataBase(LocationListener contexto, String nombre,CursorFactory factory, int version) {
			super((Context) contexto, nombre, factory, version);
		}
		
		@Override
	    public void onCreate(SQLiteDatabase db) {
	        //Execute SQL sentence to create the table.
	        db.execSQL(sqlCreate);
	        db.execSQL(sqlCreate2);
	    }
		@Override
	    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
	        //Delete last version of the table
	        db.execSQL("DROP TABLE IF EXISTS Access");//Usuarios
	        db.execSQL("DROP TABLE IF EXISTS Recipes");
	        //Create a new version of the table
	        db.execSQL(sqlCreate);
	        db.execSQL(sqlCreate2);
	    }
	}
