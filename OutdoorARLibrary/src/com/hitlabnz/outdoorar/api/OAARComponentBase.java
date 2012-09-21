/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.androidar.data.representation.TouchEvent;
import com.hitlabnz.androidar.data.representation.TouchListener;
import com.hitlabnz.androidar.data.representation.loader.LoadingThread;
import com.hitlabnz.androidar.data.representation.loader.MTLOBJLoader;
import com.hitlabnz.androidar.data.representation.model.Light;
import com.hitlabnz.androidar.data.representation.vector.Vector4f;
import com.hitlabnz.androidar.viewcomponents.ARViewComponent;
import com.hitlabnz.outdoorar.R;
import com.hitlabnz.outdoorar.data.OADataManager;

/**
 * An abstract base class for AR visualization components
 * @author Gun Lee
 */
public abstract class OAARComponentBase extends OAComponent {
	
	// option values for setupOptions()
	/**
	 * option value for disabling the grid representing the ground plane - setupOptions()
	 */
	protected final static int OPTION_HIDE_GRID = 0x01;
	
	/**
	 * option value for disabling the compass view - setupOptions()
	 */
	protected final static int OPTION_HIDE_COMPASS_VIEW = 0x02;
	
	/**
	 * option value for disabling camera background from the beginning - setupOptions()
	 */
	protected final static int OPTION_DISABLE_CAMERA_BACKGROUND = 0x04;
	
	/**
	 * option value for disabling the default light - setupOptions()
	 */
	protected final static int OPTION_DISABLE_DEFAULT_LIGHT = 0x08;
	
	
	private OADataManager dataManager; // read-only through getDataManager(), customize/initialize through setupDataManager()
	private OASensorManager sensorManager; // read-only through getSensorManager(), customize/initialize through setupSensorManager()

	private OAARVRViewComponent sceneView; // read-only through getSceneView()
	private ARViewComponent arView;
	private RelativeLayout arViewPlaceHolder;
	//private ARSpatialRegistration registration;
	protected OACompassHelperViewComponent compassView; // TODO: should be made private ?
	
	private int options = 0;
	
	
	/**
	 * Getter of the data manager in use.
	 * 
	 * @return	The data manager in use.
	 */
	public final OADataManager getDataManager() {
		return dataManager;
	}
	
	/**
	 * Getter of the sensor manager in use
	 * 
	 * @return	The sensor manager in use
	 */
	public final OASensorManager getSensorManager() {
		return sensorManager;
	}
	
	public final OAARVRViewComponent getSceneView() {
		return sceneView;
	}
	
	public final void enableCameraBackground(boolean enable) {
		if(arView != null)
			arView.enableCameraBackground(enable);
	}
	
	/**
	 * This method is deprecated. Use the getViewCoordinatePoint() method instead.
	 */
	@Deprecated
	public void getScreenPoint(double latitude, double longitude, Point point) {
		getViewCoordinatePoint(latitude, longitude, point);
	}
	
	/**
	 * This method is deprecated. Use the getViewCoordinatePoint() method instead.
	 */
	@Deprecated
	public void getScreenPoint(OAScene scene, Point point) {
		getViewCoordinatePoint(scene, point);
	}
	
	
	/**
	 * This method is not implemented yet.
	 * Calculate the view coordinate point of the given latitude longitude.
	 * 
	 * @param latitude	Latitude of the point of interest
	 * @param longitude	Longitude of the point of interest
	 * @param point	Point to be assigned with the view coordinate point of the given geo-coordinate
	 */
	public void getViewCoordinatePoint(double latitude, double longitude, Point point) {
		SceneLocation loc = new SceneLocation();
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		
		//if(mapView != null)
		//	mapView.projectToPixels(loc, point);
		
		// TODO: NOT IMPLEMENTED

	}
	
	/**
	 * Calculate the view coordinate point of the given scene
	 * @param scene	The scene of interest
	 * @param point	Point to be assigned with the view coordinate point of the given scene
	 */
	public void getViewCoordinatePoint(OAScene scene, Point point) {
		if(sceneView != null)
			sceneView.getViewCoordinatePoint(scene, point);
	}
	
	
	// methods to override in subclasses
	/**
	 * Called back for setting up options for the AR component
	 * The default implementation returns a value with no options set  (0).
	 * Subclasses overriding this method must implement it by returning option values composed with bit OR(|). 
	 * For example: return (OPTION_A | OPTION_B);
	 * 
	 * @return	The value of options set
	 */
	protected int setupOptions() {
		return 0;
	}
	
	/**
	 * Called back for setting up the data manager.
	 * The default implementation returns a new instance of OADataManager with working directory set to "OutdoorAR".
	 * Subclasses overriding this method must implement it by returning an instance of data manager to use.
	 * The instance of data manager would be whether created within this method, or an already existing one could be reused.
	 * 
	 * @return	An instance of data manager to use in this component.
	 */
	protected OADataManager setupDataManager() {
		return new OADataManager("OutdoorAR");
	}
	
	/**
	 * Called back when the system is ready to load scenes initially.
	 * The default implementation triggers loadScenes() method of the data manager.
	 * Subclasses could override the behavior to setup custom scene data.
	 * 
	 * @param dataManager	The data manager where to populate the scene.	
	 */
	protected void setupScenes(OADataManager dataManager) {
		dataManager.startLoading();
	};
	
	
	/**
	 * Called back for setting up the sensor manager.
	 * The default implementation returns a new instance of OASensorManager with useCompass argument set to true.
	 * Subclasses overriding this method must implement it by returning an instance of sensor manager to use.
	 * The instance of sensor manager would be whether created within this method, or an already existing one could be reused.
	 * 
	 * @return	An instance of sensor manager to use in this component.
	 */
	protected OASensorManager setupSensorManager() {
		return new OASensorManager(this, true);
	}
	
	
	/**
	 * Called back for setting up a custom UI layout.
	 * Subclasses overriding this method must implement it by calling the setContentView() defined in the Android Activity class.
	 * The content view must include the arView in its view hierarchy. 
	 * Optionally, the subclass could also call the addContentView() method to add more UI layouts on top.
	 * 
	 * @param arView	The view for AR visualization needed to be included in the custom UI view hierarchy.
	 */
	protected void setupUILayout(View arView) {
		setContentView(arView);	
	}
	
	/**
	 * Called back when a user selects (touches on a 3D model of) a scene
	 * The default implementation does nothing.
	 * Use onTouchedScene() method instead to be called back whenever user touches the view (even when no scene is selected).
	 *  
	 * @param scene A scene that is selected.
	 */
	protected void onSceneSelected(OAScene scene) {
		// does nothing in default
	}
	
	/**
	 * Called back when user touches the view to select a scene.
	 * The default implementation does nothing
	 * 
	 * @param scene	A scene that is touched, null if no scene found at the touched point.
	 */
	protected void onTouchedScene(OAScene scene) {
		// does nothing in default
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		options = setupOptions();
		
		LoadingThread.OLD_MODEL_ORIENTATION_FIX = false;
		MTLOBJLoader.loadFromLocalAssets(getApplicationContext());
		dataManager = setupDataManager();
		sensorManager = setupSensorManager();
		
		setupScenes(dataManager);
		
		arViewPlaceHolder = new RelativeLayout(this);		
		setupUILayout(arViewPlaceHolder);
		
		initARViews();
		
		// setup compass
		setupCompass(); // we create it anyway to show if needed later
		if((options & OAARComponentBase.OPTION_HIDE_COMPASS_VIEW) != 0)
			compassView.setWillNotDraw(true);
		
		initScenes();
	}
	
	
	
	@Override
	protected void onPause() {
		Log.d("ARComponentBase", "onPause()");
		
		sensorManager.pause();
		sceneView.onPause();
		arView.onPause();
		
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		Log.d("ARComponentBase", "onResume()");
		super.onResume();
		
		arView.onResume();
		sceneView.onResume();
		sensorManager.resume();
		
		/*/ // no use
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				sceneView.bringToFront();
				if(compassView != null) compassView.bringToFront();
			}
			
		}, 2500);
		/**/
	}
	
	@Override
	protected void onDestroy() {
		Log.d("ARComponentBase", "onDestroy()");
		sceneView.onDestroy(); // should call to destroy gl context which was intentionally left in onPause
		sceneView.getSceneGraph().destroySceneGraph(); // necessary to make work again when started again
		//registration.releaseCamera();
		
		super.onDestroy();
		System.gc();
	}
	
	private final void initARViews() {
		sceneView = new OAARVRViewComponent(this , dataManager, sensorManager);
		
		initTouch();
		
		//Setup the ar view component, this implements our new vision of the way components work in outdoor ar.
		arView = new ARViewComponent(this);
		if((options & OPTION_DISABLE_CAMERA_BACKGROUND) != 0)
			arView.enableCameraBackground(false);
		arView.setDelayOnResume(700);
		arView.setSceneView(sceneView);
		arViewPlaceHolder.addView(arView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}
	
	private final void initScenes() {
		// setup light
		if((options & OAARComponentBase.OPTION_DISABLE_DEFAULT_LIGHT) == 0) {
			Light light = new Light(0, 2.0f, 0, 1.0f, 
					new Vector4f(0.0823f, 0.3294f, 0.580f, 1.0f), // Ambient
					new Vector4f(0f, 0f, 0f, 1.0f), // Specular
					new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)); // Diffuse
			sceneView.getSceneGraph().addLight(light);
		}
		
		// setup grid
		if((options & OAARComponentBase.OPTION_HIDE_GRID) == 0)
			setupGrid();
		
		// load up scenes in the data manager : this is placed here to match sceneView.unload() in onPause()
		List<OAScene> initialScenes = dataManager.getSceneList();
		for(OAScene scene: initialScenes) {
			sceneView.getSceneGraph().addSceneToNodeTree(scene);
		}
	}
		
	private final void initTouch() {
		sceneView.addTouchListener(new TouchListener() {
			@Override
			public void touched(TouchEvent event) {
				Message msg = Message.obtain(touchHandler, 0, event.getScene());
				touchHandler.sendMessage(msg);
			}
			
		});
	}
	
	private final Handler touchHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.what == 0) {
    			if(msg.obj != null) {
    				onSceneSelected( (OAScene)msg.obj );
    				onTouchedScene( (OAScene)msg.obj );
    			} else
    				onTouchedScene( null );
    		}
    	}
	};
	
	private void setupGrid() {
		sceneView.setGridVisible(true);
	}
	
	private void setupCompass() {
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		compassView = new OACompassHelperViewComponent(sensorManager, dataManager, this,25,true,true,display.getWidth(),display.getHeight());
		
		Resources r = getResources();
		compassView.setDrawableCompass(r.getDrawable(R.drawable.compass));
		compassView.setDrawableLeftArrow(r.getDrawable(R.drawable.arrow_blue_left));
		compassView.setDrawableMiddleArrow(r.getDrawable(R.drawable.arrow_blue_down));
		compassView.setDrawableRightArrow(r.getDrawable(R.drawable.arrow_blue_right));	
		
		arViewPlaceHolder.addView(compassView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));//add the view
		
		compassView.bringToFront();
		compassView.setWillNotDraw(false);
	}
	
	public void showCompass(boolean show) {
		compassView.setWillNotDraw(!show);
	}
	
}
