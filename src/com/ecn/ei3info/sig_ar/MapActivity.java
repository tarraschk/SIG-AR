package com.ecn.ei3info.sig_ar;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hitlabnz.outdoorar.api.OAMapComponentBase;
import com.hitlabnz.outdoorar.data.OADataManager;

//TODO add control
//TODO Add Back button
//TODO recalculer la position à l'ouverture de l'activité

public class MapActivity extends OAMapComponentBase{
	@Override
	protected String setupGoogleMapApiKey() {
		return "0-uPrjI4lrnXjC_4g9gP5Scy7hauxOZEVlkGBvw";
	}
	
	@Override
	protected OADataManager setupDataManager() {
		// use a singleton instance of a custom data manager
		// in this way, you can share the same data manager with other components
		//return DataManager.getInstance();
		//return new DataManager("SIGAR");
		//OADataManagerAssets dm = new OADataManagerAssets("SIGAR", this);
		//dm.setScenesFile("sample_scenes.db"); // you can also choose a custom scene file
		DataManager dm=DataManager.getInstance();

		Log.w("myApp", Integer.toString(dm.getSceneCount()));
//		Log.w("myApp", Integer.toString(this.getDataManager().getSceneCount()));

		return dm;
	}
	
	public void onCreate(Bundle bundle) {
		  super.onCreate(bundle);
		  //automatic sleep mode deactivated
		  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
		//TODO Add toast indication of centering
			
		Log.w("myApp", Integer.toString(this.getDataManager().getSceneCount()));
		Log.w("myApp", this.getDataManager().getSceneList().get(0).name);
		Log.w("myApp", Integer.toString(this.getDataManager().getSceneList().get(0).getId()));
		
		//this.getDataManager().getSceneList().get(0).addExtraAttrib("a", "true");
		Log.w("myApp", this.getDataManager().getSceneList().get(0).getExtraAttrib("a"));
		
	}
	public void onGoBack(View view) {
		super.onBackPressed();
	}	
	protected int setupOptions(){
		return OAMapComponentBase.OPTION_START_WITH_SATELLITE_IMAGE;
	}
}
