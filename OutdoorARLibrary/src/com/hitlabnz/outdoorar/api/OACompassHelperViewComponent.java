/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import android.content.Context;
import android.location.Location;

import com.hitlabnz.androidar.data.PointsOfInterest;
import com.hitlabnz.androidar.tracking.TrackerEvent;
import com.hitlabnz.androidar.tracking.TrackerListener;
import com.hitlabnz.androidar.viewcomponents.helpers.CompassHelperViewComponent;
import com.hitlabnz.outdoorar.data.OADataManager;
import com.hitlabnz.outdoorar.listeners.OADataEvent;
import com.hitlabnz.outdoorar.listeners.OADataListener;

/**
 * @author  Leigh Beattie  This class extends the CompassView class from the compiling library.
 */
public class OACompassHelperViewComponent extends CompassHelperViewComponent{

	/**
	 * @uml.property  name="dataManagerLocal"
	 * @uml.associationEnd  
	 */
	private OADataManager dataManagerLocal;
	
	/**
	 * @uml.property  name="dataListener"
	 * @uml.associationEnd  
	 */
	private OADataListener dataListener;
	
	/**
	 * @uml.property  name="sensorManagerLocal"
	 * @uml.associationEnd  
	 */
	private OASensorManager sensorManagerLocal;
	
	/**
	 * @uml.property  name="sensorListener"
	 * @uml.associationEnd  
	 */
	private TrackerListener sensorListener;
	
	private Location lastLocation;
	
	/**
	 *  Initialise the compass view
	 * 
	 * @param context
	 * @param widthScreen
	 * @param showArrowObjectSelectedPosition
	 * @param showInformationSceneSelected
	 * @param heightScreen
	 */
	public OACompassHelperViewComponent(OASensorManager sensorManager, OADataManager dataManager, Context context, int widthScreen,
			boolean showArrowObjectSelectedPosition,
			boolean showInformationSceneSelected, int heightScreen) {
		super(context, widthScreen, showArrowObjectSelectedPosition,
				showInformationSceneSelected, heightScreen);
		this.dataManagerLocal = dataManager;
		this.sensorManagerLocal = sensorManager;
		init();           
	}

	/**
	 * Initialise the compass view
	 * 
	 * @param context
	 * @param sizeCompassPercentageWidthScreen
	 * @param showInformationSceneSelected
	 * @param showArrowObjectSelectedPosition
	 * @paraouble lastLatitude;
	private double lastLongitude;m widthScreen
	 * @param heightScreen
	 */
	public OACompassHelperViewComponent(OASensorManager sensorManager,OADataManager dataManager,Context context,
			int sizeCompassPercentageWidthScreen,
			boolean showInformationSceneSelected,
			boolean showArrowObjectSelectedPosition, int widthScreen,
			int heightScreen) {
		super(context, sizeCompassPercentageWidthScreen, showInformationSceneSelected,
				showArrowObjectSelectedPosition, widthScreen, heightScreen);
		this.dataManagerLocal = dataManager;
		this.sensorManagerLocal = sensorManager;
		init();
	}
	/**
	 * Initialise the compass view
	 * 
	 * @param context
	 * @param sizeCompassPercentageWidthScreen
	 * @param showInformationSceneSelected
	 * @param showArrowObjectSelectedPosition
	 * @paraouble lastLatitude;
				private double lastLongitude;m widthScreen
	 * @param heightScreen
	 *  @param changePositionX
	 * 			  values of changes on X
	 * @param changePositionY
	 * 			  value of changes on Y
	 * @param arrowsOnTop
	 * 			  represent whether we gonna show top arrows
	 */
	public OACompassHelperViewComponent(OASensorManager sensorManager,OADataManager dataManager,Context context,
			int sizeCompassPercentageWidthScreen,
			boolean showInformationSceneSelected,
			boolean showArrowObjectSelectedPosition, int widthScreen,
			int heightScreen, int changePositionX, int changePositionY, boolean arrowsOnTop) {
		super(context, sizeCompassPercentageWidthScreen, showInformationSceneSelected,
				showArrowObjectSelectedPosition, widthScreen, heightScreen, changePositionX, changePositionY, arrowsOnTop);
		this.dataManagerLocal = dataManager;
		this.sensorManagerLocal = sensorManager;
		init();
	}

	/**
	 * Initialise a few objects in the compassview class, setup listeners for data updates.
	 */
	
	private void init(){
		dataListener = new OADataListener() {
			
			@Override
			public void dataUpdate(OADataEvent event) {
				if(event.getSceneAction() == OADataEvent.SCENE_ADD || event.getSceneAction() == OADataEvent.SCENE_DELETE){
					objects3d = new PointsOfInterest(); // we recreate everytime to make sure the scene gets deleted 
					objects3d.loadGPSPositionObjects(dataManagerLocal.getSceneList());
					if(lastLocation != null)objects3d.updatePostionObjects(lastLocation.getLatitude(), lastLocation.getLongitude()); 
					//Update all the objects in the list of objects the compass has.
				}
			}
		};
		
		sensorListener = new TrackerListener() {
			
			@Override
			public void updatePose(TrackerEvent event) {
				if(event.getEventType() == TrackerEvent.EVENT_SENSOR_ORIENTATION){
					setOrientation(event.getOrientation());
				}
				if(event.getEventType() == TrackerEvent.EVENT_SENSOR_LOCATION){
					if(objects3d == null)objects3d = new PointsOfInterest();
					objects3d.updatePostionObjects(event.getPosition().getLatitude(), event.getPosition().getLongitude());
					lastLocation = event.getPosition(); //The current version of the compass is a little silly with the way it calculates GPS positions of the user an objects.
				}
				invalidate(); //Invalidate the current drawn compass, causing a redraw.
				
			}
		};
		//Sending Scene List to PointsOfInterest objects
		if(objects3d == null)objects3d = new PointsOfInterest();
		objects3d.loadGPSPositionObjects(dataManagerLocal.getSceneList());
		
		dataManagerLocal.addDataListener(dataListener);
		sensorManagerLocal.addSensorListener(sensorListener);
	}
	
	/**
	 * Force compass view to update POIs from scene data
	 */
	public void updateScene()
	{
		//if(objects3d == null)
			objects3d = new PointsOfInterest();
		objects3d.loadGPSPositionObjects(dataManagerLocal.getSceneList());
		if(lastLocation != null)objects3d.updatePostionObjects(lastLocation.getLatitude(), lastLocation.getLongitude()); 
		//Update all the objects in the list of objects the compass has.
	}

}
