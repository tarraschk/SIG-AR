package com.ecn.ei3info.sig_ar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 20 nov 2012
 * Activity class to manage model resources, connection to DB and 
 * 
 * @author bastienmarichalragot
 * @version 1
 */
public class ManageModelActivity extends Activity {
	
	/**
	 * Called when the activty is first created
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 setContentView(R.layout.managemodelactivity);

		
        ArrayList<String> results = new ArrayList<String>();
        
		SigarDB database= new SigarDB(this);
	    SQLiteDatabase sqlDB = database.getReadableDatabase();

        Cursor c = sqlDB.rawQuery("SELECT * FROM category ;", null);
        Log.w("myApp",c.toString());


       // if (c != null ) {


        c.moveToFirst();
        Log.w("myApp",c.toString());
		Log.w("myApp",Integer.toString(c.getCount()));


        while (c.isAfterLast() == false) {
        		
        		String firstName = c.getString(c.getColumnIndex("id_category"));
        		Log.w("myApp",firstName);
        		Log.w("myApp",Integer.toString(c.getCount()));
        		String age = c.getString(c.getColumnIndex("name_category"));
        		results.add("" + firstName + ",Age: " + age);
        		Log.w("myApp",(age));

        		c.moveToNext();
        	};
        //}
    	
        //ListView lv= (ListView) this.findViewById(R.id.list);
    	//lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.test,results));

	}
}
