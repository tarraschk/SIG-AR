package com.ecn.ei3info.sig_ar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hitlabnz.outdoorar.R;
import com.hitlabnz.outdoorar.api.OAMapComponentBase;

public class MapActivity extends OAMapComponentBase{
	@Override
	protected String setupGoogleMapApiKey() {
		return "0-uPrjI4lrnXjC_4g9gP5Scy7hauxOZEVlkGBvw";
		
	}
	
	@Override
	protected void setupUILayout(View mapView) {
		// load the custom UI layout
		LayoutInflater controlInflater = LayoutInflater.from(getBaseContext());
		LinearLayout sampleUILayout = (LinearLayout)controlInflater.inflate(R.layout.activity_map, null);
		
		// embed the plain map view within the custom UI
		sampleUILayout.addView(mapView);
		
		// then set the custom UI view
		setContentView(sampleUILayout);
	}
	
	// handler for the button in the custom UI layout
	boolean satellite = true;
	public void onButton(View view) {
		//TODO modify position
		//TODO change text
		//TODO style
		//TODO changer l'eau du poisson
		
		Toast.makeText(this, "Clicked custom UI button", Toast.LENGTH_SHORT).show();

		satellite = !satellite;
		if(satellite)
			this.setMapStyle(MAP_STYLE_SATELLITE_IMAGE);
		else
			this.setMapStyle(MAP_STYLE_DEFAULT);
	}
	
	protected int setupOptions(){
		return OAMapComponentBase.OPTION_START_WITH_SATELLITE_IMAGE;
	}
	
	
}
