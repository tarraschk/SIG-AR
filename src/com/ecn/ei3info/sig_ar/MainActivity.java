package com.ecn.ei3info.sig_ar;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hitlabnz.outdoorar.api.OAARComponentBase;
import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.data.OADataManager;
//import fr.maraumax.customtabs.MainActivity;
//import fr.maraumax.customtabs.MapActivity;
//import fr.maraumax.customtabs.R;
//import fr.maraumax.customtabs.SettingsActivity;

//TODO Comment
//TODO Delete menu bar/tablette

/**
 * Main activity is activated after splashscreen. Show AR view with old selected scene. 
 * @author bastienmarichalragot
 *	@version 1
 */
@TargetApi(16)
public class MainActivity extends OAARComponentBase {

	public static int options=0x01;
	public static Boolean GPSAlert=false;
	/**
	 *  Called when the activity is first created. 
	 *  
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.GPSAlert = getIntent().getBooleanExtra("GPSAlert", false);
		//getActionBar().hide();
		// modifications 
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && GPSAlert==false ){
			buildAlertMessageNoGps();
		}
		final ConnectivityManager manager1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if( manager1.getActiveNetworkInfo()==null){
			buildAlertMessageNoNetwork();
		}
		//TODO ca ne marchera pas!!!!
		if(MapActivity.modificationMode==1){
			getSensorManager().enableMockLocation(MapActivity.mockLocationLat, MapActivity.mockLocationLong);
		}
		
		
		//Les 5 lignes suivantes sont pour la barre de navigation
		//setContentView(R.layout.activity_main);
        //this.tabHost = getTabHost();
        //setupTab("AR View", "tab1", new Intent().setClass(this, MainActivity.class));
        //setupTab("Map", "tab2", new Intent().setClass(this, MapActivity.class));
        //setupTab("Settings", "tab3", new Intent().setClass(this, SettingsActivity.class));
    }
	
	/**
	 * Show alert message to manage GPS positioning. 
	 * Detected if your GPS is activated and allow you to switch on GPS Positioning if is necessary.
	 */
	private void buildAlertMessageNoGps(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//LayoutInflater inflater = this.getLayoutInflater();
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		.setCancelable(false)
		//.setView(inflater.inflate(R.layout.dialog_custom, null))
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.cancel();
				GPSAlert=true;
			}
		})
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		final AlertDialog alert = builder.create();

		/*   Button button = (Button) findViewById(R.id.button1);

		// add button listener
		button.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View arg0) {
              startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
              }
		  });*/

		/*
		 * 

		 */
		//TODO Centrer les textes

		alert.show();
	}
	/**
	 * Show alert message to manage connectivity. 
	 * Detected if you are connected to network and allow you to activated it if necessary.
	 */
	private void buildAlertMessageNoNetwork() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Network seems to be disabled, do you want to enable it?")
		.setCancelable(false)
		.setPositiveButton("WIFI", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
			//TODO Centrer les textes

		})
		.setNeutralButton("DataRoaming", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				Toast.makeText(MainActivity.this, "No data will be downloaded", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	/*@Override
	protected void onRestart() {
		onCreate(null);
		//super.onRestart();
		//setupScenes(DataManager.getInstance(true));
	}*/
	
	
	/*@Override
	protected void onResume() {
		super.onRestart();
		setupDataManager();
	}*/
	
	/**
	 * Setup DataManager: load model data and activate scenes.
	 * this method simply use the getInstance of DataManager.
	 * @see DataManager
	 */
	@Override
	protected OADataManager setupDataManager() {
		// set custom working path instead of using the default path - "OutdoorAR"
		/*DataManager AR=new DataManager("SIGAR");
		for(OAScene c:DataManager.getInstance(false).getSceneList()){
			if(((Scene) c).isActivated()){
				AR.addScene(c);
			}
		}
		return AR;// DataManager.getInstance();*/
		return DataManager.getInstance(true);
	}	
	/**
	 * Set up options to the AR Layout
	 * @see com.hitlabnz.outdoorar.api.OAARComponentBase#setupOptions()
	 */
	@Override
	protected int setupOptions() {
		return ( options);
	}
	/**
	 * 
	 */
	public void updateOptions(){
		setupOptions();
	}
	/**
	 * @return the options
	 */
	public static int getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public static void setOptions(int options) {
		MainActivity.options = options;
	}


	//TODO Option d'affichage du compas et de la grille
	//TODO Modifier Interface

	/**
	 * Set up UI Layout for AR View.
	 * Add ControlButton
	 */
	@Override
	protected void setupUILayout(View arView) {		
		// set the plain AR view first
		setContentView(arView);

		// then load and add the custom UI on top of the AR view
		LayoutInflater controlInflater = LayoutInflater.from(getBaseContext());
		RelativeLayout sampleUILayout = (RelativeLayout)controlInflater.inflate(R.layout.activity_main, null);
		addContentView(sampleUILayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	} 
	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.api.OAARComponentBase#onSceneSelected(com.hitlabnz.outdoorar.api.OAScene)
	 */
	@Override
	protected void onSceneSelected(OAScene scene) {
		//TODO revoir ce code pour ouvrir une popp d'edition de choix
		super.onSceneSelected(scene);
		
		Toast.makeText(MainActivity.this, scene.getName()+" est seclectionn�e", Toast.LENGTH_SHORT).show();

	}
	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.api.OAARComponentBase#onTouchedScene(com.hitlabnz.outdoorar.api.OAScene)
	 */
/*	@Override
	protected void onTouchedScene(OAScene scene) {
		// TODO Auto-generated method stub
		super.onTouchedScene(scene);
		
		Toast.makeText(MainActivity.this, scene.getName()+" est seclect'tgtthththionn�e", Toast.LENGTH_SHORT).show();
	
	}*/
	/**
	 * Go to Map View
	 * @param View
	 */
	public void onMapActivity(View View){
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * Go to List View
	 * @param View
	 */
	public void onListActivity(View View){
		Intent intent = new Intent(this, ListActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * Go to Settings
	 * @param View
	 */
	public void onSettingsActivity(View View){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
        finish();
	}
	/**
	 * Go back and quit application
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setMessage("Are you sure you want to exit?")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		})
		//TODO Centrer les textes

		.setNegativeButton("No", null)
		.show();
	}
	/**
	 * Quit the application
	 * @param View
	 */
	public void onQuit(View View){
		new AlertDialog.Builder(this)
		.setMessage("Are you sure you want to exit?")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//TODO save data to xml (copy to back button)
				finish();
			}
		})
		.setNegativeButton("No", null)
		.show();
	}

}
