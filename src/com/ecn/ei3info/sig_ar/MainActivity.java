package com.ecn.ei3info.sig_ar;

import com.hitlabnz.outdoorar.api.OAARComponentBase;
import com.hitlabnz.outdoorar.data.OADataManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

//TODO Comment
//TODO Delete menu bar/tablette
@TargetApi(16)
public class MainActivity extends OAARComponentBase {
	
	public static int options=0x01;
	protected Boolean GPSAlert;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.GPSAlert = getIntent().getBooleanExtra("GPSAlert", false);
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
    }
   	
	private void buildAlertMessageNoGps(){
		LayoutInflater inflater = this.getLayoutInflater();
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setView(inflater.inflate(R.layout.dialog_custom, null));
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           //TODO Centrer les textes
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                    dialog.cancel();
	                    GPSAlert=true;
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	 }
	
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
	
	
	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.api.OAARComponentBase#setupOptions()
	 */
	@Override
	protected int setupOptions() {
		// TODO Auto-generated method stub
		return ( options);
	}

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

	/* (non-Javadoc)
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

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
   */
   
    //TODO Option d'affichage du compas et de la grille
	//TODO Modifier Interface
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
    /**
     * Go to Map View
     * @param View
     */
   // @TargetApi(16)
	public void onMapActivity(View View){
    	Intent intent = new Intent(this, MapActivity.class);
    //	  ActivityOptions options = ActivityOptions.makeScaleUpAnimation(View, 0,
    	//	      0, View.getWidth(), View.getHeight());
    	startActivity(intent);// , options.toBundle());
    }
    /**
     * Go to List View
     * @param View
     */
    public void onListActivity(View View){
    	Intent intent = new Intent(this, ListActivity.class);
    	startActivity(intent);
    }
    /**
     * Go to Settings
     * @param View
     */
    public void onSettingsActivity(View View){
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
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
