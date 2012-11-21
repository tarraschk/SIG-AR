package com.ecn.ei3info.sig_ar;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * 20 nov 2012
 * Activity class to manage model resources, connection to DB and 
 * 
 * @author bastienmarichalragot
 * @version 1
 */
public class ManageModelActivity extends ListActivity {
	
	/**
	 * Called when the activty is first created
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        ArrayList<String> results = new ArrayList<String>();
        
		SigarDB database= new SigarDB(this);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();

        Cursor c = sqlDB.rawQuery("SELECT id_category, nom_category FROM category", null);
    	
    	if (c != null ) {
    		if  (c.moveToFirst()) {
    			do {
    				String firstName = c.getString(c.getColumnIndex("id_category"));
    				int age = c.getInt(c.getColumnIndex("nom_category"));
    				results.add("" + firstName + ",Age: " + age);
    			}while (c.moveToNext());
    		} 
    	}
    	
        
    	this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));

	}
}
