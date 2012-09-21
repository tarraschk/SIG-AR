/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.hitlabnz.androidar.data.renderer.Data3DRenderer;
import com.hitlabnz.androidar.data.representation.GeoCoords;
import com.hitlabnz.androidar.data.representation.SceneGraph;
import com.hitlabnz.androidar.data.representation.TouchListener;
import com.hitlabnz.androidar.data.representation.cameras.Camera;
import com.hitlabnz.androidar.data.representation.graph.Node;
import com.hitlabnz.androidar.data.representation.model.ColourRGBA;
import com.hitlabnz.androidar.data.representation.vector.Vector2f;
import com.hitlabnz.androidar.tracking.TrackerEvent;
import com.hitlabnz.androidar.tracking.TrackerListener;
import com.hitlabnz.androidar.utils.GLTools;
import com.hitlabnz.androidar.viewcomponents.ARViewComponent.FovListener;
import com.hitlabnz.androidar.viewcomponents.helpers.GLHelperComponent;
import com.hitlabnz.androidar.viewcomponents.helpers.GridGLHelperComponent;
import com.hitlabnz.androidar.viewcomponents.surface.ARSurface;
import com.hitlabnz.outdoorar.data.OADataManager;
import com.hitlabnz.outdoorar.listeners.OADataEvent;
import com.hitlabnz.outdoorar.listeners.OADataListener;

/**
 * View for 3D scene visualization
 * This class gives you access to controls for the 3D scene view.
 * @author  Leigh Beattie
 */
public class OAARVRViewComponent extends ARSurface implements FovListener { //com.hitlabnz.androidar.rendering.AndroidGLSurface{
	
	protected OADataManager dataManagerLocal;
	protected OASensorManager sensorManager;
	
	private Data3DRenderer renderer;
	private SceneGraph sceneGraph;
	private Camera camera;
	private GridGLHelperComponent gridComponent;
	private int width;
	private int height;

	
	private Context context;
	
	//private boolean gpsInitialised = false; // deprecated
	//private boolean paused = false; // deprecated
	//private boolean loaded = false; // deprecated
	
	private boolean touchEnabled = true;
	
	private boolean enableCameraOrientationTracking = true;
	
	/**
	 * Construct a World View. Sets up data sources, OpenGL render surface and sensor reactions
	 * @param context The context to use for the renderer. Usually that is the activity that launches the renderer.
	 * @param dataManager The data manager to use
	 * @param sensorManager The sensor manager for getting location and orientation
	 */
	public OAARVRViewComponent(Context context, OADataManager dataManager, OASensorManager sensorManager) {
		super(context);
		this.context = context;
		
		this.dataManagerLocal = dataManager;
	
		this.sensorManager = sensorManager;
		
		camera = new Camera();
		sceneGraph = new SceneGraph(dataManager.getModelPath(), camera);
		renderer = new Data3DRenderer(false, sceneGraph);
		//glSurface.setFPSMeter(true);
		
		if(!sensorManager.usesGyro())camera.setInterpolated(true);
		
		sensorManager.addSensorListener(new TrackerListener() {
			
			private GeoCoords bufferCoords = new GeoCoords();
			
			@Override
			public void updatePose(TrackerEvent event) {
				//Use the sensor manager to send sensor events to the GL surface for orientation and location.
				if(event.getEventType() == TrackerEvent.EVENT_SENSOR_ORIENTATION){
					if(enableCameraOrientationTracking)
						camera.updateRotation(event.getQuaternion());
				} else if(event.getEventType() == TrackerEvent.EVENT_SENSOR_LOCATION){
					//gpsInitialised = true; deprecated
					bufferCoords.setLatitude(event.getPosition().getLatitude());
					bufferCoords.setLongitude(event.getPosition().getLongitude());
					camera.setGeoLocation(bufferCoords);
				}
			}
		});
		
		dataManager.addDataListener(new OADataListener() {
			
			@Override
			public void dataUpdate(OADataEvent event) {
				switch (event.getSceneAction()) {
					case OADataEvent.SCENE_ADD :
						sceneGraph.addSceneToNodeTree(event.getScene());
						//sceneGraph.setScenes(dataManagerLocal.getSceneList());
					break;
				
					case OADataEvent.SCENE_DELETE :
						sceneGraph.removeSceneFromNodeTree(event.getScene());
					break;
				}
				
			}
		});
		//Attach a listener to the view so we can send touch events to the scene graph for processing. The scene graph takes the coordinate data 
		//tells objects listening to it if there's a scene under that screen position.
		View.OnTouchListener touchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//Log.i("Touch COORD", "" + event.getRawX() + " " + event.getRawY() );
				
				if(touchEnabled)
					sceneGraph.touch((int)event.getRawX(), height - (int)event.getRawY());
				return false;
			}
		};
		
		this.setOnTouchListener(touchListener);
		
		//mPreview = new CameraView(this);//instantiate the camera view
		//int[] config = glSurface.getConfigSpec();
		
		//If GL ES 2 is present then we should enable it
		if(GLTools.getGLVersion() == Node.NODE_ANDROID_GLES20)setEGLContextClientVersion(2);
		
		//Tegra 2 does not support a depth buffer larger than 16bits.

		/**EGL10 egl = (EGL10)EGLContext.getEGL();
		
		EGLDisplay dsply = egl.eglGetCurrentDisplay();
		
		 int[] configSpec = { 
	                EGL10.EGL_DEPTH_SIZE,   16, 
	                EGL10.EGL_NONE 
	        }; 
		 
		EGLConfig[] configs = new EGLConfig[32]; 
        int[] num_config = new int[1]; 
        egl.eglChooseConfig(dsply, configSpec, configs, 1, num_config); 
        EGLConfig config = configs[0]; 
        
        for(int i = 0; i < configs.length; i++){
        	if(configs[i] != null)Log.i("EGL", "Config " + i +" " + configs[i].toString());
        }*/
		
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);//important part to set up for the phone(working on most of the recent phones
		//setEGLConfigChooser(new ARConfigChooser());

		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setKeepScreenOn(true);
		//setPreserveEGLContextOnPause(true); // only supported from API level 11, we work around to not pause the GLSurface view instead see onPause()
		setRenderer(renderer);//bind the scene renderer to surfaceView
		
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();//get the information of the user's screen size 
		width = display.getWidth();
		height = display.getHeight();
	}
	 
	@Deprecated
	public void startLoading(){
		/*
		Thread loadingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!paused && !gpsInitialised); // wait for the GPS to initialise
				//sceneGraph.startLoading();
				//while(!sceneGraph.geometryLoaded());
				loaded = true;
			}
		});
		
		//loadingThread.start(); //This caused problems when GPS signals and network connections couldn't be made. The whole thread would literally cause the program to lag.
		loaded = true;
		*/
	}
	
	/**
	 * Set the visibility of the grid in the AR view.
	 * @param visible Set to true to have the grid visible and false if you don't want to see the grid.
	 */
	public void setGridVisible(boolean visible){
		if(visible){
			gridComponent = new GridGLHelperComponent(50, 50, 10, new ColourRGBA(100, 100, 100, 255));
			gridComponent.setTranslation(0,-2.0f,0);
			
			sceneGraph.queueLodForNodeTree(gridComponent);
		}
	}
	
	/**
	 * Adds a GL Helper component to the scene graph
	 * @param helperComponent
	 */
	public void addGLHelperComponent(GLHelperComponent helperComponent) {
		sceneGraph.queueLodForNodeTree(helperComponent);
	}
	
	/**
	 * Enable the overview mode for the AR view. This will change the camera so that it centers it's view on
	 * the currently selected point of interest and the accelerometers/gyroscope and GPS control the camera's 
	 * position around the point of interest.
	 */
	@Deprecated
	public void enableOverviewMode(){
		//glSurface.enableOverviewMode();
	}
	
	/**
	 * Enable the AR mode. This will change the camera so it is positioned at the user's GPS coordinates and uses the
	 * devices orientation to change the camera's point of view. This view mode will use the camera as the background 
	 * image for the 3D scene.
	 */
	@Deprecated
	public void enableARMode(){
		//glSurface.disableOverviewMode();
	}
	
	@Deprecated
	public void setDistanceFadeToIcon(int distance){
		//glSurface.setFadeToIconDistance(distance);
	}
	
	/**
	 * Assign a data manager to the World view. The datamanager is used to tell the world view where to fetch data from.
	 * @param dataManager  The DataManger related to your project.
	 * @uml.property  name="dataManager"
	 */
	public void setDataManager(OADataManager dataManager){
		this.dataManagerLocal = dataManager;
	}
	
	@Deprecated
	public boolean initialised(){
		/*
		boolean value = false;
		if(loaded && gpsInitialised) value = true;
		return value;
		*/
		return true;
	}

	
	public SceneGraph getSceneGraph(){
		return this.sceneGraph;
	}
		
	/**
	 * This method is deprecated. Use the getViewCoordinatePoint() method instead.
	 */
	@Deprecated
	public void getScreenPoint(OAScene scene, Point point) {
		getViewCoordinatePoint(scene, point);
	}
	
	/**
	 * Calculate the screen coordinate point of the given scene
	 * @param scene	The scene of interest
	 * @param point	Point to be assigned with the screen coordinate point of the given scene
	 */
	public void getViewCoordinatePoint(OAScene scene, Point point) {
		Vector2f viewPoint = sceneGraph.getViewCoordinatePosition(scene);
		point.x = (int)( getWidth() * (viewPoint.x + 1.0f) / 2.0f );
		point.y = (int)( getHeight() * (1.0f - (viewPoint.y + 1.0f) / 2.0f) );
	}

	@Override
	public void onPause() {
		//super.onPause(); // This is intentionally blocked and moved to onDestroy() to prevent GL context being trashed.
		renderer.stopRendering();
		//this.paused = true; // deprecated
	}

	@Override
	public void onResume() {
		super.onResume();
		renderer.startRendering();
		//this.paused = false; // deprecated
	}
	
	public void onDestroy() {
		super.onPause();
	}
	
	public void addTouchListener(TouchListener listener){
		sceneGraph.addTouchListener(listener);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		super.surfaceChanged(holder, format, width, height);
		this.width = width;
		this.height = height;
	}	
	
	/**
	 * If you need the camera object you can get it with this method.
	 * @return The virtual camera used in the AR scene.
	 */
	public Camera getCamera(){
		return this.camera;
	}
	
	public Data3DRenderer getRenderingSurface(){
		return this.renderer;
	}
	
	/**
	 * Trigger touch event at given screen coordinate to get called back with a scene picked.
	 * This works even if enableTouch is set to false. 
	 * @param x
	 * @param y
	 */
	public void triggerTouch(int x, int y){
		sceneGraph.touch(x, height - y);
	}
	
	/**
	 * Enable or disable picking a scene when the user taps on the screen
	 * @param enable
	 */
	public void enableTouch(boolean enable){
		this.touchEnabled = enable;
	}
	
	/**
	 * Set clear (background) colour  of the GL scene
	 * @param r		red component of the colour (0.0 - 1.0)
	 * @param g		green component of the colour (0.0 - 1.0)
	 * @param b		blue component of the colour (0.0 - 1.0)
	 * @param a		alpha component of the colour (0.0 - 1.0)
	 */
	public void setClearColour(float r, float g, float b, float a) {
		sceneGraph.setClearColour(r, g, b, a);
	}
	
	// called back from the ARViewComponent
	@Override
	public void updateFov(float horizontalAngle, float verticalAngle) {
		setFov(horizontalAngle, verticalAngle);
	}
	
	/**
	 * Set FOV of the camera
	 * @param horizontalAngle
	 * @param verticalAngle
	 */
	public void setFov(float horizontalAngle, float verticalAngle) {
		sceneGraph.setFov(horizontalAngle, verticalAngle);
	}
	
	/**
	 * Set near and far plane of the camera
	 * @param near
	 * @param far
	 */
	public void setNearFar(float near, float far) {
		sceneGraph.setNearFar(near, far);
	}
	
	/**
	 * Whether to use fov information to calculate aspect ratio.
	 * This helps better registration in AR, although the aspect ratio could appear not natural. 
	 * @param enabled
	 */
	public void useFovAspectRatio(boolean enable) {
		sceneGraph.useFovAspectRatio(enable);
	}
	
	/**
	 * Whether to user tracking sensor information to update the camera orientation.
	 * This is enabled by default for the AR view.
	 * Disable it for VR mode.
	 * @param enable
	 */
	public void enableCameraOrientationTracking(boolean enable) {
		enableCameraOrientationTracking = enable;
	}
}
