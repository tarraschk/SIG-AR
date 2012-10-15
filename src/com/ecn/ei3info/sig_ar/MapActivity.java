package com.ecn.ei3info.sig_ar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hitlabnz.outdoorar.R;
import com.hitlabnz.outdoorar.api.OAMapComponentBase;

//TODO add control
//TODO Add Back button

public class MapActivity extends OAMapComponentBase{
	@Override
	protected String setupGoogleMapApiKey() {
		return "0-uPrjI4lrnXjC_4g9gP5Scy7hauxOZEVlkGBvw";
		
	}
	
	//TODO Add Map Control View
	//TODO Add slide Layout
	/**
	 * Define the Custom UI layout
	 */
	@Override
	protected void setupUILayout(View mapView) {
		// load the custom UI layout
		LayoutInflater controlInflater = LayoutInflater.from(getBaseContext());
		FrameLayout sampleUILayout = (FrameLayout)controlInflater.inflate(R.layout.activity_map, null);
		//Set the plain Map View
		setContentView(mapView);
		//Set the control button
		addContentView(sampleUILayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));	
	}
	
	// handler for the button in the custom UI layout
	boolean satellite = true;
	
	public void onMapView(View view) {
		satellite = !satellite;
		if(satellite){
			this.setMapStyle(MAP_STYLE_SATELLITE_IMAGE);
			Toast.makeText(this, "Satellite Image Activated", Toast.LENGTH_SHORT).show();
		}
		else{
			this.setMapStyle(MAP_STYLE_DEFAULT);
			Toast.makeText(this, "Satellite Image Deactivated", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onCenterLocation(View view) {
		centerToUserLocation();
	}
	public void onGoBack(View view) {
		super.onBackPressed();
	}	
	protected int setupOptions(){
		return OAMapComponentBase.OPTION_START_WITH_SATELLITE_IMAGE;
	}
	
	
}
