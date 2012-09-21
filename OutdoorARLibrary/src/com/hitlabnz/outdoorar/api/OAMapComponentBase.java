/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import java.util.List;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.hitlabnz.androidar.components.MapComponent;
import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.androidar.tracking.TrackerEvent;
import com.hitlabnz.androidar.tracking.TrackerListener;
import com.hitlabnz.androidar.viewcomponents.map.view.MapItemizedOverlay;
import com.hitlabnz.androidar.viewcomponents.map.view.MapOverlayEvent;
import com.hitlabnz.androidar.viewcomponents.map.view.MapOverlayItem;
import com.hitlabnz.outdoorar.R;
import com.hitlabnz.outdoorar.data.OADataManager;

/**
 * An abstract base class for map visualization components
 * @author Gun Lee
 */
public abstract class OAMapComponentBase extends MapComponent {
	
	// arguments for setMapStyle()
	/**
	 * map style of default stylized map - setMapStyle()
	 */
	public final static int MAP_STYLE_DEFAULT = 0;
	
	/**
	 * map style of satellite image map - setMapStyle()
	 */
	public final static int MAP_STYLE_SATELLITE_IMAGE = 1;
	
	// option values for setupOptions()
	/**
	 * option value for disabling default zoom control buttons - setupOptions()
	 */
	protected final static int OPTION_HIDE_ZOOM_CONTROL = 0x01;
	
	/**
	 * option value for starting with satellite image map style - setupOptions()
	 */
	protected final static int OPTION_START_WITH_SATELLITE_IMAGE = 0x02;
	
	/**
	 * option value for disabling scene icon clustering feature - setupOptions()
	 */
	protected final static int OPTION_DISABLE_CLUSTERING = 0x04;
	
	/**
	 * option value for disabling centering to user location on start - setupOptions()
	 */
	protected final static int OPTION_DISABLE_CENTER_TO_USER_ON_START = 0x08;
	
	
	
	private OADataManager dataManager; // read-only through getDataManager(), customize/initialize through setupDataManager()
	private OASensorManager sensorManager; // read-only through getSensorManager(), customize/initialize through setupSensorManager()
	
	private OAMapViewComponent mapView;
	private OAIconSourceBase iconSource;
	
	private boolean keepCenteredToUserLocation = false; // write-only through setCenterMapToUserLocation()
	private boolean centerToUserLocationOnce = true;
	private Location userLocation;
	
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
	
	/**
	 * Sets whether to center the map to user's location update with sensors.
	 * To center the map to the user's location only for once, use centerToUserLocation() method.
	 * 
	 * @param on	A flag to turn this feature on.
	 */
	public final void keepCenteredToUserLocation(boolean on) {
		keepCenteredToUserLocation = on;
		if(keepCenteredToUserLocation)
			centerToUserLocation();
	}
	
	/**
	 * Center the map to user's location for one time.
	 * To make the map continuously follow the user's location, use keepCenteredToUserLocation(boolean on) method.
	 */
	public final void centerToUserLocation() {
		if(userLocation != null)
			centerTo(userLocation.getLatitude(), userLocation.getLongitude(), true);
		else
			centerToUserLocationOnce = true;
	}
	
	/**
	 * Center the map to given location
	 * @param latitude	Latitude in degrees
	 * @param longitude	Longitude in degrees
	 */
	public final void centerTo(double latitude, double longitude, boolean animate) {
		mapView.setCentre(latitude, longitude, animate);
	}
	
	/**
	 * Set the zoom level of the map
	 * @param level	Zoom level to set (1: max zoom out, 21: max zoom in)
	 * @return	the new zoom level
	 */
	public final int zoom(int level) {
		return mapView.getMapView().getController().setZoom(level);
	}
	
	/**
	 * Zoom in one level
	 * @return	true if managed to zoom in, false if hit the limit
	 */
	public final boolean zoomIn() {
		return mapView.getMapView().getController().zoomIn();
	}
	
	/**
	 * Zoom out one level
	 * @return	true if managed to zoom out, false if hit the limit
	 */
	public final boolean zoomOut() {
		return mapView.getMapView().getController().zoomOut();
	}
	
	/**
	 * Zooms and centers the map to show all of the scenes
	 * @return	the new zoom level (current zoom level if no scene available)
	 */
	public final int zoomToScenes() {
		List<OAScene> scenes = getDataManager().getSceneList();
		if(scenes.isEmpty())
			return mapView.getMapView().getZoomLevel();
		
		// if there's only one scene, center without zooming 
		if(scenes.size() == 1) {
			OAScene scene = scenes.get(0);
			centerTo(scene.getLatitude(), scene.getLongitude(), true);
			return mapView.getMapView().getZoomLevel();
		}
		
		double minLat = 91, minLon = 181, maxLat = -91, maxLon = -181;
		for(OAScene scene: scenes) {
			double lat = scene.getLatitude();
			double lon = scene.getLongitude();
			
			if(lat < minLat) minLat = lat;
			if(lat > maxLat) maxLat = lat;
			if(lon < minLon) minLon = lon;
			if(lon > maxLon) maxLon = lon;
		}
		
		// get slightly bigger region
		double latOffset = (maxLat - minLat) * 0.05;
		double lonOffset = (maxLon - minLon) * 0.05;
		minLat -= latOffset;
		minLon -= lonOffset;
		maxLat += latOffset;
		maxLon += lonOffset;
		
		if(minLat < -90) minLat = -90;
		if(maxLat > 90) maxLat = 90;
		if(minLon < -180) minLon = -180;
		if(maxLon > 180) maxLon = 180;
		
		return zoomToRegion(minLat, minLon, maxLat, maxLon);
	}
	
	/**
	 * Zooms and centers the map to the given extent of region
	 * @param lat1	Latitude of upper left corner
	 * @param lon1	Longitude of upper left corner
	 * @param lat2	Latitude of lower right corner
	 * @param lon2	Longitude of lower right corner
	 * @return	the new zoom level
	 */
	public final int zoomToRegion(double lat1, double lon1, double lat2, double lon2) {
		double latSpan = lat2 - lat1;
		if(latSpan < 0)
			latSpan = -latSpan;
		
		double lonSpan = lon2 - lon1;
		if(lon1 > 0 && lon2 < 0)
			latSpan += 360; //((lon2 + 360) - lon1)
		if(lonSpan < 0)
			lonSpan = -lonSpan;
		
		double centerLat = (lat1 + lat2) / 2;
		double centerLon = (lon1 + lon2) / 2; 
		
		mapView.getMapView().getController().zoomToSpan((int)(latSpan*1E6), (int)(lonSpan*1E6));
		centerTo(centerLat, centerLon, false);
		return mapView.getMapView().getZoomLevel();
	}
	
	/**
	 * Sets the style of the map image.
	 * This method should be called after the map view is set up properly in the onCreate() method of this class.
	 * 
	 * @param style	One of the MAP_STYLE_* constants defined in OAMapComponentBase class.
	 */
	public final void setMapStyle(int style) {
		if(mapView == null)	return;
		
		if(style == OAMapComponentBase.MAP_STYLE_SATELLITE_IMAGE)
			mapView.setSatellite(true);
		else if(style == OAMapComponentBase.MAP_STYLE_DEFAULT)
			mapView.setSatellite(false);
	}
	
	/**
	 * Force the map to be redrawn with updated scene 
	 */
	public final void sceneUpdated() {
		mapView.usePOIs(dataManager.getSceneList());
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
	 * Calculate the view coordinate point of the given latitude longitude
	 * @param latitude	Latitude of the point of interest
	 * @param longitude	Longitude of the point of interest
	 * @param point	Point to be assigned with the view coordinate point of the given geo-coordinate
	 */
	public void getViewCoordinatePoint(double latitude, double longitude, Point point) {
		SceneLocation loc = new SceneLocation();
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		
		if(mapView != null)
			mapView.projectToPixels(loc, point);
	}
	
	/**
	 * Calculate the view coordinate point of the given scene
	 * @param scene	The scene of interest
	 * @param point	Point to be assigned with the view coordinate point of the given scene
	 */
	public void getViewCoordinatePoint(OAScene scene, Point point) {
		if(mapView != null)
			mapView.projectToPixels(scene.location, point);
	}
	
	
	// Methods to override/implement in custom map view activities
	/**
	 * Called back for setting up options for the List component
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
	 * Called back asking for the Google Map API key.
	 * Subclasses must override this method and implement it by returning the key.
	 * 
	 * @return	The Google Map API key
	 */
	protected abstract String setupGoogleMapApiKey();
	
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
	 * The content view must include the mapView in its view hierarchy. 
	 * Optionally, the subclass could also call the addContentView() method to add more UI layouts on top.
	 * @param mapView	The view for map visualization needed to be included in the custom UI view hierarchy.
	 */
	protected void setupUILayout(View mapView) {
		setContentView(mapView);
	}
	
	/**
	 * Called back when a user selects (touches on an icon of) a scene
	 * The default implementation centers the map to the selected scene.
	 * Use onTouchedScene() method instead to be called back whenever user touches the view (even when no scene is selected).
	 * 
	 * @param scene A scene that is selected.
	 */
	protected void onSceneSelected(OAScene scene) {
		// Center the map to the selected scene
		mapView.setCentre(scene.location.getLatitude(), scene.location.getLongitude(), true);
	}
	
	/**
	 * Called back when user touches the view to select a scene.
	 * The default implementation does nothing.
	 * 
	 * @param scene	A scene that is touched, null if no scene found at the touched point.
	 */
	protected void onTouchedScene(OAScene scene) {
		// does nothing in default
	}
	
	/**
	 * Called back for setting up the icon source.
	 * The default implementation returns a new instance of OAIconSource initialized with the data manager in use.
	 * Subclasses overriding this method must implement it by returning an instance of OAIconSource to use.
	 * 
	 * @return	An instance of icon source to use in this component.
	 */
	protected OAIconSourceBase setupIconSource() {
		OAIconSource iconSource = new OAIconSource(dataManager);
		iconSource.setDefaultIcon(this.getResources().getDrawable(R.drawable.icon));
		return iconSource;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		options = setupOptions();
		
		if((options & OPTION_DISABLE_CENTER_TO_USER_ON_START) != 0)
			centerToUserLocationOnce = false;
		
		dataManager = setupDataManager();
		sensorManager = setupSensorManager();
		
		String apiKey = setupGoogleMapApiKey();
		iconSource = setupIconSource();
		
		mapView = new OAMapViewComponent(this, apiKey, dataManager, sensorManager, iconSource);
		mapView.setSatellite((options & OAMapComponentBase.OPTION_START_WITH_SATELLITE_IMAGE) != 0);
		//mapView.getMapView().getController().setZoom(1);
		mapView.setClusterZoomEnabled((options & OAMapComponentBase.OPTION_DISABLE_CLUSTERING) == 0);
        mapView.setPOITappedListener(new MapItemizedOverlay.OverlayTouchListener() {
			@Override
			public void overlayHasChanged(MapOverlayEvent event) {				
				if (event.item != null) {
					// Set selected scene
					if (event.item instanceof MapOverlayItem) {
						MapOverlayItem mapOverlayItem = (MapOverlayItem) event.item;
						OAScene scene = (OAScene)mapOverlayItem.getPOI();
						
						// call back the handlers
						onSceneSelected(scene);
						onTouchedScene(scene);
					}
				} else {
					onTouchedScene(null);
				}
			}
		});
        
        // setup sensor listener for centering map to user's location
        sensorManager.addSensorListener(new TrackerListener() {
			@Override
			public void updatePose(TrackerEvent event) {
				if(event.getEventType() == TrackerEvent.EVENT_SENSOR_LOCATION){
					userLocation = event.getPosition();
					if(keepCenteredToUserLocation || centerToUserLocationOnce) {
						centerToUserLocation();
						centerToUserLocationOnce = false;
					}
				}
			}
		});
		
		setupUILayout(mapView);
		
		// initialize map scrolling and zoom to the scenes
		List<OAScene> scenes = dataManager.getSceneList();
		if(scenes.size() > 0)
			mapView.setCentre(scenes.get(0).location.getLatitude(), 
        		scenes.get(0).location.getLongitude(), true);
		
		// hide zoom controls if option set
		if((options & OAMapComponentBase.OPTION_HIDE_ZOOM_CONTROL) != 0)
			mapView.getMapView().setBuiltInZoomControls(false);
		
		// load up scenes already in the data manager
		List<OAScene> initialScenes = dataManager.getSceneList();
		for(OAScene scene: initialScenes) {
			mapView.addPOI(scene);
		}
		
		// trigger to start loading up new scenes to the data manager
		setupScenes(dataManager);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.resume();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
