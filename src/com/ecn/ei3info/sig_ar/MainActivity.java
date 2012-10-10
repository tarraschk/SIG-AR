package com.ecn.ei3info.sig_ar;

import com.hitlabnz.outdoorar.api.OAARComponentBase;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class MainActivity extends OAARComponentBase {

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
  */  
   
    
    @Override
	protected void setupUILayout(View arView) {		
		// set the plain AR view first
		setContentView(arView);
		
		// then load and add the custom UI on top of the AR view
		LayoutInflater controlInflater = LayoutInflater.from(getBaseContext());
		RelativeLayout sampleUILayout = (RelativeLayout)controlInflater.inflate(R.layout.activity_main, null);
		addContentView(sampleUILayout, 
			new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
    public void onMapActivity(View View){
    	Intent intent = new Intent(this, MapActivity.class);
    	startActivity(intent);
    }
    public void onListActivity(View View){
    	Intent intent = new Intent(this, ListActivity.class);
    	startActivity(intent);
    }
    public void onSettingsActivity(View View){
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
    }
    
    
}
