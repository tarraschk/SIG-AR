package com.ecn.ei3info.sig_ar;
/**
 *  
 *
 */

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hitlabnz.outdoorar.api.OAMapComponentBase;
import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.data.OADataManager;

//TODO add control
//TODO recalculer la position ˆ l'ouverture de l'activitŽ
/**
 * 
 * 
 * @author bastienmarichalragot
 * @version  1
 */
public class MapActivity extends OAMapComponentBase{
	
	protected boolean plot=true;
	protected static boolean toogler = false;
	
	protected double mockLocationLong;
	protected double mockLocationLat;
	protected double mockLocationAlt;
	
	protected static int modificationMode=0;
	// 0 --> modification mode disable
	// 1 --> modification user position 
	// 2 --> modification scene
	// 3 -->
	
	protected LinearLayout modificationControl;
	
	View buttonToogle ;
	
	/**
	 * Save your Google Map API Key.
	 */
	@Override
	protected String setupGoogleMapApiKey() {
		//TODO delete this key before finish project
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
		  buttonToogle = (View)findViewById(R.id.button_toggler);
		  
		  modificationControl= (LinearLayout) findViewById(R.id.modificationControlLayout);
		 //TODO verifier cet ligne de code currentfocus???
		  modificationControl.setVisibility(this.getCurrentFocus().GONE);
		  
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
	
	@Override
	protected void onSceneSelected(OAScene scene) {
		super.onSceneSelected(scene);
		Toast.makeText(this, "Votre scene est à la longitude" + scene.location.getLongitude() + "et à la latitude" + scene.location.getLatitude()  , Toast.LENGTH_LONG).show();
	
	}
	/**
	 * Method associate to imageButton to refocus the map on your position.
	 * @param view
	 */
	public void onCenterLocation(View view) {
		centerToUserLocation();		
		Toast.makeText(this, "Map refocused on your position.", Toast.LENGTH_LONG).show();

		/// TODO Supprimer au nettoyage 
		/*
		Log.w("myApp", Integer.toString(this.getDataManager().getSceneCount()));
		Log.w("myApp", this.getDataManager().getSceneList().get(0).name);
		Log.w("myApp", Integer.toString(this.getDataManager().getSceneList().get(0).getId()));
		*/
		//this.getDataManager().getSceneList().get(0).addExtraAttrib("a", "true");
		//Log.w("myApp", this.getDataManager().getSceneList().get(0).getExtraAttrib("a"));
		
	}
	/**
	 * Method called when you press on GoBackButton. 
	 * Quit mapActivity and return to AR view.
	 * @param view
	 */
	public void onGoBack(View view) {
		super.onBackPressed();
		this.plot=false;
	}	
	/**
	 * Method called when you press on BackButton of the tablet. 
	 * Quit mapActivity and return to AR view.
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(MapActivity.this, MainActivity.class);
		//intent.putExtra("GPSAlert", true); 
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
	

	//Toogle button view
	public void onDisplayButtons(View view) {
		toogler = !toogler;

		if(toogler)
			buttonToogle.setVisibility(View.VISIBLE);
		else
			buttonToogle.setVisibility(View.GONE);
	}


	boolean modify = false;
	public void onAddDesactivatedScenetoMap(View view) {
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

	
	//TODO change the xml to toggle button with custom style cf: http://www.mokasocial.com/2011/07/sexily-styled-toggle-buttons-for-android/
	
	public void onUserPositionModification(View view){
		modificationMode=1;
		modificationControl.setVisibility(view.VISIBLE);
		
		mockLocationLong= getSensorManager().getCurrentLocation().getLongitude();
		mockLocationLat= getSensorManager().getCurrentLocation().getLatitude();
		mockLocationAlt= getSensorManager().getCurrentLocation().getAltitude();

	}
	
	
	public void onEditScene(View view){
		modificationMode=2;
		modificationControl.setVisibility(view.VISIBLE);
		
	}
	
	
	
	
	public void onPositionUp(View view){
		double lat=0;
		double lng=0;
		
		
		mockLocationLat=mockLocationLat+0.1;
		// latitude augmente
		// ajouter un "booléen" différenciant : modif position, modèle ou rien du tout (boutons cachés)
		/*if(getSensorManager().isMockLocationEnabled())
			getSensorManager().disableMockLocation();
		else*/
			getSensorManager().enableMockLocation(mockLocationLat, mockLocationLong);
		
	}

	public void onPositionDown(View view){
		// latitude diminue
	}
	
	public void onPositionLeft(View view){
		// longitude augmente
	}
	
	public void onPositionRight(View view){
		// longitude diminue
	}
	

	
}
