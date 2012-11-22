package com.ecn.ei3info.sig_ar;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.hitlabnz.outdoorar.api.OAMapComponentBase;
import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.data.OADataManager;

//TODO add control
//TODO Add Back button
//TODO recalculer la position à l'ouverture de l'activité

public class MapActivity extends OAMapComponentBase{
	protected boolean plot=true;
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
		/*if(a){
			DataManager MAP=new DataManager("SIGAR");
			for(OAScene c:DataManager.singletonInstance.getSceneList()){
				if(((Scene) c).isActivated()){
					MAP.addScene(c);
				}
			}
			return MAP;
		}else{
			return DataManager.singletonInstance;	
		}
		//Log.w("myApp", Integer.toString(dm.getSceneCount()));
		//Log.w("myApp", Integer.toString(this.getDataManager().getSceneCount()));

		//return MAP;*/
		return DataManager.getInstance(plot);
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
	
	protected int setupOptions(){
		return OAMapComponentBase.OPTION_START_WITH_SATELLITE_IMAGE;
	}
	
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
		//Log.w("myApp", this.getDataManager().getSceneList().get(0).getExtraAttrib("a"));
		
	}
	
	public void onGoBack(View view) {
		super.onBackPressed();
		this.plot=false;
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(MapActivity.this, MainActivity.class);
		intent.putExtra("GPSAlert", true); 
		startActivity(intent);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.api.OAMapComponentBase#setupScenes(com.hitlabnz.outdoorar.data.OADataManager)
	 
	@Override
	protected void setupScenes(OADataManager dataManager) {
		// TODO Auto-generated method stub
		for(OAScene c:this.getDataManager().getSceneList()){
			if(((Scene) c).isActivated()){
				MAP.addScene(c);
			}
		}
	
	}*/

	boolean modify = false;
	public void onMapModification(View view) {
		if (modify){
			this.plot=true;
			//this.setupDataManager();
			//DataManager.getInstance(plot).startLoading();
			for(OAScene c:DataManager.singletonInstance.getSceneList()){
				if(!((Scene) c).isActivated()){
					this.getDataManager().removeScene(c);
				}
			}
			//this.setupScenes(DataManager.getInstance(plot));
			this.sceneUpdated();
		}else{
			this.plot=false;
			//this.setupDataManager();
			//this.setupScenes(DataManager.getInstance(plot));
			for(OAScene c:DataManager.singletonInstance.getSceneList()){
				if(!((Scene) c).isActivated()){
					this.getDataManager().addScene(c);
				}
			}
			this.sceneUpdated();

		}
		Log.w("myScene", Boolean.toString(this.plot));

		modify=!modify;
	}
	 
	public void onEditModel(View view){
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_modifymodel);
		dialog.setTitle("Title...");

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.modelname);
		text.setText(DataManager.singletonInstance.getSceneList().get(0).getName());
	
		
		//NumberPicker latitude=(NumberPicker) dialog.findViewById(R.id.numberPickerLatitude);
		//latitude.setDisplayedValues(DataManager.singletonInstance.getSceneList().get(0).getLatitude());
		
		final EditText latitude= (EditText) dialog.findViewById(R.id.latitude); 
		latitude.setText( String.valueOf(DataManager.singletonInstance.getSceneList().get(0).getLatitude()));
		
		final EditText longitude= (EditText) dialog.findViewById(R.id.longitude); 
		latitude.setText( String.valueOf(DataManager.singletonInstance.getSceneList().get(0).getLongitude()));
		
		Button okButton = (Button) dialog.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//TODO completer les donnée modifées
				DataManager.singletonInstance.getSceneList2().get(0).setLatitude(Double.parseDouble(latitude.getText().toString()));
				DataManager.singletonInstance.getSceneList2().get(0).setLongitude(Double.parseDouble(longitude.getText().toString()));
				
				
				//geere les exception
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
		// if button is clicked, close the custom dialog
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
		
		
	}
	
}
