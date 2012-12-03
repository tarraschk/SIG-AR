package com.ecn.ei3info.sig_ar;
/**
 *  
 *
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hitlabnz.androidar.viewcomponents.MapViewComponent;
import com.hitlabnz.outdoorar.api.OAMapComponentBase;
import com.hitlabnz.outdoorar.api.OAMapViewComponent;
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
	
	protected static double mockLocationLong;
	protected static double mockLocationLat;
	protected static double mockLocationAlt;
	
	protected static int modificationMode=0;
	// 0 --> modification mode disable
	// 1 --> modification user position 
	// 2 --> modification scene
	// 3 -->
	protected int idSceneOnEdition=0;
	
	protected MapViewComponent mapview;
	protected LinearLayout modificationControl;
	protected TextView modifInfo;
	
	
	private Handler repeatUpdateHandler = new Handler();
	//TODO regler la vitesse des appuiye long... 
	private int REP_DELAY =200;
	private boolean mUp = false;
	private boolean mDown = false;
	private boolean mRight = false;
	private boolean mLeft = false;
	
	
	protected float[] googleZoom={0,156542,78271,39135,19567,9783,4891,2445,1222,611,305,152,76,38,19,(float) 9.55,(float) 4.77,(float) 2.38,(float) 1.19,(float) 0.59,(float) 0.29,(float) 0.15,(float)0.07};
		//{21282,16355,10064,5540,2909,1485,752,378,190,95,48,24,12,6,3,(float) 1.48,(float) 0.74,(float) 0.37,(float) 0.19};
	
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
		  //automatic sleep mode deactivated
		  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		  buttonToogle = (View)findViewById(R.id.button_toggler);
		  
		  modificationControl= (LinearLayout) findViewById(R.id.modificationControlLayout);
		 //TODO verifier cet ligne de code currentfocus???
		  modificationControl.setVisibility(this.getCurrentFocus().GONE);
		  
		  modifInfo= (TextView) findViewById(R.id.modificationInfo);
		  
		  final ImageButton up=(ImageButton) findViewById(R.id.button_up);
		  final ImageButton down=(ImageButton) findViewById(R.id.button_down);
		  final ImageButton left=(ImageButton) findViewById(R.id.button_left);
		  final ImageButton right=(ImageButton) findViewById(R.id.button_right);
		 
		  
		  
		  class RptUpdater implements Runnable {
		      public void run() {
		          if( mUp){
		              onPositionUp(mapview);
		              repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
		          } else if( mDown ){
		        	  onPositionDown(mapview);
		              repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
		          } else if (mLeft){
		        	  onPositionLeft(mapview);
		              repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
		          } else if (mRight){
		        	  onPositionRight(mapview);
		              repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
		          }
		      }
		  }

		  up.setOnLongClickListener( 
				  new View.OnLongClickListener(){
					  public boolean onLongClick(View arg0) {
						  mUp = true;
						  repeatUpdateHandler.post( new RptUpdater() );
						  return false;
					  }
				  }
				  );   

		  up.setOnTouchListener( new View.OnTouchListener() {
			  public boolean onTouch(View v, MotionEvent event) {
				  if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL) 
						  && mUp){
					  mUp = false;
				  }
				  return false;
			  }
		  });  

		  

		  down.setOnLongClickListener( 
				  new View.OnLongClickListener(){
					  public boolean onLongClick(View arg0) {
						  mDown = true;
						  repeatUpdateHandler.post( new RptUpdater() );
						  return false;
					  }
				  }
				  );   

		  down.setOnTouchListener( new View.OnTouchListener() {
			  public boolean onTouch(View v, MotionEvent event) {
				  if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL) 
						  && mDown){
					  mDown = false;
				  }
				  return false;
			  }
		  });  


		  right.setOnLongClickListener( 
				  new View.OnLongClickListener(){
					  public boolean onLongClick(View arg0) {
						  mRight = true;
						  repeatUpdateHandler.post( new RptUpdater() );
						  return false;
					  }
				  }
				  );   

		  right.setOnTouchListener( new View.OnTouchListener() {
			  public boolean onTouch(View v, MotionEvent event) {
				  if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL) 
						  && mRight){
					  mRight = false;
				  }
				  return false;
			  }
		  });  


		  left.setOnLongClickListener( 
				  new View.OnLongClickListener(){
					  public boolean onLongClick(View arg0) {
						  mLeft = true;
						  repeatUpdateHandler.post( new RptUpdater() );
						  return false;
					  }
				  }
				  );   

		  left.setOnTouchListener( new View.OnTouchListener() {
			  public boolean onTouch(View v, MotionEvent event) {
				  if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL) 
						  && mLeft){
					  mLeft = false;
				  }
				  return false;
			  }
		  });  

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
		mapview=(MapViewComponent) mapView;
		//Set the control button
		addContentView(sampleUILayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));	
	}
	
	protected int setupOptions(){
		return OAMapComponentBase.OPTION_START_WITH_SATELLITE_IMAGE;
	}
	
	/**
	 * Button to control map view, enable sattelite view
	 * @param view
	 */
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
		if (modificationMode==2){
			idSceneOnEdition= scene.getId();
			modifInfo.setText("Edit scene: "+  DataManager.getInstance(false).getScene(idSceneOnEdition).getName());
			Log.w("myApp",Integer.toString(idSceneOnEdition));
		}
		
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
		//TODO enable les autres bouton 
		
		modificationMode=1;
		modificationControl.setVisibility(view.VISIBLE);
		
		modifInfo.setText("You can modify your current positon.");
		
		mockLocationLong= getSensorManager().getCurrentLocation().getLongitude();
		mockLocationLat= getSensorManager().getCurrentLocation().getLatitude();
		mockLocationAlt= getSensorManager().getCurrentLocation().getAltitude();
		
		Toast.makeText(MapActivity.this, "You can change your position", Toast.LENGTH_SHORT).show();

	}
	
	
	public void onEditScene(View view){
		modificationMode=2;
		modificationControl.setVisibility(view.VISIBLE);
		
		modifInfo.setText("Edit scene: ??");
		
		Toast.makeText(MapActivity.this, "Selected a scene on map then edit it with control buttons.", Toast.LENGTH_SHORT).show();
		Log.w("myApp","Modificationmode"+Integer.toString(modificationMode));
		
	}
	
	public void onPositionUp(View view){
		//Modify Latitude of object
		double deltaalt=calculateDeltaGps();
		//Modify position with MockLocation
		if (modificationMode==1){
			mockLocationLat=mockLocationLat+deltaalt;
			// latitude augmente
			// ajouter un "booléen" différenciant : modif position, modèle ou rien du tout (boutons cachés)
			/*if(getSensorManager().isMockLocationEnabled())
			getSensorManager().disableMockLocation();
		else*/
			getSensorManager().enableMockLocation(mockLocationLat, mockLocationLong);
		}else if (modificationMode==2){
			if(idSceneOnEdition==0){
				Toast.makeText(MapActivity.this, "Please select a scene to edit.", Toast.LENGTH_SHORT).show();
			}else{
			 
				((Scene) DataManager.getInstance(false).getScene(idSceneOnEdition)).setLatitude(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()+deltaalt);
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()+deltaalt));
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()));
				Log.w("myApp","delta alt: "+Double.toString(deltaalt));
			}
		}
		sceneUpdated();
	}

	public void onPositionDown(View view){
		//Modify Latitude of object
		double deltaalt=calculateDeltaGps();
		//Modify position with MockLocation
		if (modificationMode==1){
			mockLocationLat=mockLocationLat-deltaalt;
			getSensorManager().enableMockLocation(mockLocationLat, mockLocationLong);
		}else if (modificationMode==2){
			if(idSceneOnEdition==0){
				Toast.makeText(MapActivity.this, "Please select a scene to edit.", Toast.LENGTH_SHORT).show();
			}else{
			 
				((Scene) DataManager.getInstance(false).getScene(idSceneOnEdition)).setLatitude(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()-deltaalt);
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()-deltaalt));
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()));
				Log.w("myApp","delta alt: "+Double.toString(deltaalt));
			}
		}
		sceneUpdated();
	}
	
	public void onPositionLeft(View view){
		//Modify Latitude of object
		double deltaalt=calculateDeltaGps();
		//Modify position with MockLocation
		if (modificationMode==1){
			mockLocationLong=mockLocationLong-deltaalt;
			// latitude augmente
			// ajouter un "booléen" différenciant : modif position, modèle ou rien du tout (boutons cachés)
			/*if(getSensorManager().isMockLocationEnabled())
			getSensorManager().disableMockLocation();
		else*/
			getSensorManager().enableMockLocation(mockLocationLat, mockLocationLong);
		}else if (modificationMode==2){
			if(idSceneOnEdition==0){
				Toast.makeText(MapActivity.this, "Please select a scene to edit.", Toast.LENGTH_SHORT).show();
			}else{
			 
				((Scene) DataManager.getInstance(false).getScene(idSceneOnEdition)).setLongitude(DataManager.getInstance(false).getScene(idSceneOnEdition).getLongitude()-deltaalt);
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()-deltaalt));
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()));
				Log.w("myApp","delta alt: "+Double.toString(deltaalt));
			}
		}
		sceneUpdated();	
	}
	
	public void onPositionRight(View view){
		//Modify Latitude of object
		double deltaalt=calculateDeltaGps();
		//Modify position with MockLocation
		if (modificationMode==1){
			mockLocationLong=mockLocationLong+deltaalt;
			getSensorManager().enableMockLocation(mockLocationLat, mockLocationLong);
		}else if (modificationMode==2){
			if(idSceneOnEdition==0){
				Toast.makeText(MapActivity.this, "Please select a scene to edit.", Toast.LENGTH_SHORT).show();
			}else{
			 
				((Scene) DataManager.getInstance(false).getScene(idSceneOnEdition)).setLongitude(DataManager.getInstance(false).getScene(idSceneOnEdition).getLongitude()+deltaalt);
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()-deltaalt));
				Log.w("myApp",Double.toString(DataManager.getInstance(false).getScene(idSceneOnEdition).getLatitude()));
				Log.w("myApp","delta alt: "+Double.toString(deltaalt));
			}
		}
		sceneUpdated();
	}
	
	public void onValidationofMofication(View view){
		
	}
	
	public void onCancellationofMofication(View view){
		
	}
	
	//TODO centralizer la taille de l'ecran dans la fonction ci dessous
	/**
	 * method call to estimate the GPS coordinate delta to add to  
	 * @return
	 */
	private double calculateDeltaGps(){
		double delta=0;
		double R=6371000;
		Log.w("myApp",Integer.toString( mapview.getMapView().getZoomLevel()));

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;

		int zoomlevel=mapview.getMapView().getZoomLevel();

		float realscale= googleZoom[zoomlevel]*height;
		Log.w("myApp","taille reelle"+Float.toString( realscale));
		
		//get 63 click to browse the screen
		delta=2*Math.atan(realscale/(63*R));
		
		return delta;
	}
}
