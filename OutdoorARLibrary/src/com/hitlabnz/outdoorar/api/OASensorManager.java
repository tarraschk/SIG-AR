/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import android.content.Context;
import android.location.Location;

import com.hitlabnz.androidar.sensors.gps.filters.GPSFilter;
import com.hitlabnz.androidar.sensors.gps.filters.GPSFilterSpoofed;
import com.hitlabnz.androidar.sensors.inertial.filters.OrientationFilter;
import com.hitlabnz.androidar.sensors.inertial.filters.OrientationFilterAverager;
import com.hitlabnz.androidar.sensors.inertial.filters.OrientationFilterGeomagnetic;
import com.hitlabnz.androidar.tracking.GPSInertialTracker;

/**
 * A class that manages sensor information for tracking user's location and motion.
 * The Sensor manager is used to monitor GPS and orientation sensors and report any changes
 * to the respective components that are listening to it. You can add a listener to the 
 * sensor manager
 * @author Leigh Beattie
 */
public class OASensorManager extends GPSInertialTracker{
		
	private GPSFilterSpoofed mockLocationFilter = null; // should be null if disabled
	
	public OASensorManager(Context context, boolean useCompass){
		super(context, useCompass);		
	}
	
	public void addGPSFilter(GPSFilter<?> filter){
		gpsManager.addFilter(filter);
	}
	
	public void removeGPSFilter(GPSFilter<?> filter){
		gpsManager.removeFilter(filter);
	}
	
	public void addInertialFilter(OrientationFilter<?> filter){
		inertialManager.addOrientationFilter(filter);
	}
	
	public void enableGyro(){
		inertialManager.useGyro(true);
	}
	
	public void disableGyro(){
		inertialManager.useGyro(false);
	}
	
	public void enableCompass(){
		inertialManager.useCompass(true);
	}
	
	public void disableCompass(){
		inertialManager.useCompass(false);
	}
	
	public void enableAccelerometer(){
		inertialManager.useAccel(true);
	}
	
	public void disableAccelerometer(){
		inertialManager.useAccel(false);
	}
	
	public void useFastAccelerometer(){
		inertialManager.filterAcceleromter(false);
	}
	
	public void useAccurateAccelerometer(){
		inertialManager.filterAcceleromter(true);
	}
	
	public boolean isMockLocationEnabled() {
		return (mockLocationFilter != null);
	}
	
	public void enableMockLocation(double latitude, double longitude) {
		Location location = new Location("MOCK_LOCATION");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		location.setAccuracy(0.1f);
		location.setTime(System.currentTimeMillis());
		
		if(mockLocationFilter != null)
			removeGPSFilter(mockLocationFilter);
		
		mockLocationFilter = new GPSFilterSpoofed(location);
		addGPSFilter(mockLocationFilter);
	}
	
	public void disableMockLocation() {
		if(mockLocationFilter != null) {
			removeGPSFilter(mockLocationFilter);
			mockLocationFilter = null;
		}
	}
	
	/**
	 * Get current location.
	 * This function returns filtered value. (e.g., mocked value if enabled.)
	 * Use getCurrentRawLocation() method instead to get non-filtered value.
	 * @return	The current location after filtering.
	 */
	public Location getCurrentLocation() {
		return gpsManager.getUserLocation();
	}
	
	/**
	 * Get current non-filtered location.
	 * This function returns raw value from the GPS sensor. (e.g. non-mocked value even if mock location is enabled.)
	 * Use getCurrentLocation() method instead to get filtered value.
	 * @return	The current location before filtering.
	 */
	public Location getCurrentRawLocation() {
		return gpsManager.getRawUserLocation();
	}
	
	/**
	 * Get current heading
	 * @return	heading in degrees (0:north / +:east / -:west)
	 */
	public float getCurrentHeading() {
		return inertialManager.getHeading();
	}
	
	/**
	 * Get current pitch in degrees
	 * @return	pitch in degrees (0:horizon / +:down / -:up) 
	 */
	public float getCurrentPitch() {
		return inertialManager.getPitch();
	}
	
	/**
	 * Get current roll in degrees
	 * @return	roll in degrees (0:upright / +:clockwise / -:counter-clockwise)
	 */
	public float getCurrentRoll() {
		return inertialManager.getRoll();
	}
}
