package com.ecn.ei3info.sig_ar;

import com.hitlabnz.outdoorar.api.OAARComponentBase;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

//TODO Comment
//TODO Add "do you want to quit?"

public class MainActivity extends OAARComponentBase {

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
	       buildAlertMessageNoGps();
	    }
	    final ConnectivityManager manager1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    if( manager1.getActiveNetworkInfo()==null){
	    	buildAlertMessageNoNetwork();
	    }
    }
   	
	private void buildAlertMessageNoGps(){
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	 }
	
	private void buildAlertMessageNoNetwork() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your WIFI seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                    dialog.cancel();
	                    //TODO add warning can't add new modele.
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
/*
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
