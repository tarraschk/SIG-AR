/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;
import android.content.Context;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import com.hitlabnz.androidar.data.IconSource;
import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.tracking.TrackerEvent;
import com.hitlabnz.androidar.tracking.TrackerListener;
import com.hitlabnz.androidar.viewcomponents.MapViewComponent;
import com.hitlabnz.androidar.viewcomponents.map.view.MapMarkerFactory;
import com.hitlabnz.androidar.viewcomponents.map.view.MarkerFactory;
import com.hitlabnz.outdoorar.R;
import com.hitlabnz.outdoorar.data.OADataManager;
import com.hitlabnz.outdoorar.listeners.OADataEvent;
import com.hitlabnz.outdoorar.listeners.OADataListener;

/**
 * View for map visualization
 * @author Tim Hobbs
 */
public class OAMapViewComponent extends MapViewComponent {

	private OADataManager dataManager;
	private OASensorManager sensorManager;
	
	public OAMapViewComponent(Context context, String apiKey, OADataManager dataManager, OASensorManager sensorManager, IconSource iconSource){
		super(context);
		this.dataManager = dataManager;
		this.sensorManager = sensorManager;

	    NinePatchDrawable markerBorder = (NinePatchDrawable)context.getResources().getDrawable(R.drawable.markerborderdark);
		this.markerFactory = new MapMarkerFactory(context, markerBorder, iconSource, false);
		setupMapView(apiKey, R.drawable.compass_anim1);
		setClusterZoomEnabled(true);
		setupListeners();
	}
	
	public OAMapViewComponent(Context context, String apiKey, OADataManager dataManager, OASensorManager sensorManager, MarkerFactory markerFactory) {
		super(context, apiKey, markerFactory, R.drawable.compass);
		this.dataManager = dataManager;
		this.sensorManager = sensorManager;
		setClusterZoomEnabled(true);
		setupMapView(apiKey, R.drawable.compass_anim1);
		setupListeners();
	}
	
	private void setupListeners(){
		
		dataManager.addDataListener(new OADataListener() {
			
			@Override
			public void dataUpdate(OADataEvent event) {
				switch (event.getSceneAction()) {
					case OADataEvent.SCENE_ADD :
						Log.d("Map", "Scene add: " + event.getScene().location.toGeoPoint());
						addPOI(event.getScene());
					break;
				
					case OADataEvent.SCENE_DELETE :
						removePOI(event.getScene());
					break;
									
					case OADataEvent.SCENES_CHANGED :
						Log.d("Map", "Scenes changed:");
						for (OAScene scene: dataManager.getSceneList()){
							Log.d("Map", "Scene modified: " + scene.location.toGeoPoint());
						}
						usePOIs(dataManager.getSceneList());
					break;
				}
				
			}
		});
		
		sensorManager.addSensorListener(new TrackerListener() {
			@Override
			public void updatePose(TrackerEvent event) {
				if(event.getEventType() == TrackerEvent.EVENT_SENSOR_ORIENTATION){
					float bearing = event.getOrientation()[0];
					setUserBearing(bearing);
					
				}
				if(event.getEventType() == TrackerEvent.EVENT_SENSOR_LOCATION){
					Log.d("Map", "LOCATION");
					setUserPosition(event.getPosition().getLatitude(),event.getPosition().getLongitude());
					setUserAccuracy(event.getPosition().getAccuracy());
				}
			}
		});
	}

}
