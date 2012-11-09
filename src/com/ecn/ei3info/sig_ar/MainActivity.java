package com.ecn.ei3info.sig_ar;

import com.hitlabnz.outdoorar.api.OAARComponentBase;
import com.hitlabnz.outdoorar.data.OADataManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

//TODO Comment
//TODO Delete menu bar/tablette
@TargetApi(16)
public class MainActivity extends OAARComponentBase implements OnTouchListener {
	
	public static int options=0x01;
	public RelativeLayout sampleUILayout;
	public Handler l;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        

        
        // modifications 
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
	           //TODO Centrer les textes
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
		 sampleUILayout = (RelativeLayout)controlInflater.inflate(R.layout.activity_main, null);
		addContentView(sampleUILayout, 
			new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// add listener to enable the UI with touch
		arView.setOnTouchListener(this);
		//Set time life of UI
		l= new Handler();
		
		AnimationSet set = new AnimationSet(true);

		  Animation animation = new AlphaAnimation(0.0f, 1.0f);
		  animation.setDuration(100);
		  set.addAnimation(animation);
		  
		  //bloc de translation conserver si besoin...
		 /* animation = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
		  );
		  animation.setDuration(500);
		  set.addAnimation(animation);*/

		  LayoutAnimationController controller =
		      new LayoutAnimationController(set, 0.25f);
		
		sampleUILayout.setLayoutAnimation(controller);
		l.postDelayed(new Runnable(){
			@Override
			public void run() {
				sampleUILayout.setVisibility(View.GONE);
			}
		}, 6000);
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

    
    //TODO modifier la durée et proposer une parametrisation dans la fentre des settings
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		sampleUILayout.setVisibility(View.VISIBLE);
		
		AnimationSet set = new AnimationSet(true);

		  Animation animation = new AlphaAnimation(0.0f, 1.0f);
		  animation.setDuration(100);
		  set.addAnimation(animation);

		 /* animation = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
		  );
		  animation.setDuration(500);
		  set.addAnimation(animation);*/

		  LayoutAnimationController controller =
		      new LayoutAnimationController(set, 0.25f);
		
		sampleUILayout.setLayoutAnimation(controller);
		
		l= new Handler();

		l.postDelayed(new Runnable(){
			@Override
			public void run() {
				sampleUILayout.setVisibility(View.GONE);
			}
		}, 3000);
		return false;
	}
    
}
