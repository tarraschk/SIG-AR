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
}
