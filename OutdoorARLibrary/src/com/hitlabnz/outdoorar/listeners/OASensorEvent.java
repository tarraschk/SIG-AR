/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.listeners;

import java.util.EventObject;

import android.location.Location;

/**
 * Represents a sensor event such as a device orientation change or GPS update
 * @author Leigh Beattie
 * @deprecated
 */
public class OASensorEvent extends EventObject {
	
	public static int EVENT_SENSOR_GPS = 0;
	public static int EVENT_SENSOR_ORIENTATION = 1;
	
	
	private float[] orientation;
	private Location location;
	private int screenOrientation;
	private int eventType;
	
	/**
	 * Constructs a sensor event
	 * @param source 
	 * @param orientation An array containing orientation values where orientation[0] is azimuth, orientation[1] is pitch, and orientation[2] is roll
	 * @param location A GPS fix of the device's location
	 * @param screenOrientation The orientation of the screen
	 * @param eventType The type of event
	 */
	public OASensorEvent(Object source, float[] orientation, Location location, int screenOrientation, int eventType) {
		super(source);
		this.orientation = orientation;
		this.location = location;
		this.screenOrientation = screenOrientation;
		this.eventType = eventType;
	}
	
	/**
	 * Get the orientation of the device
	 * @return the device orientation
	 */
	public float[] getDeviceOrientation(){
		return this.orientation;
	}
	
	/**
	 * Get the location of the device
	 * @return the location of the device
	 */
	public Location getDeviceLocation(){
		return this.location;
	}
	
	/**
	 * Get the screen orientation of the device
	 * @return
	 */
	public int getScreenOrientation(){
		return this.screenOrientation;
	}
	
	/**
	 * Get the type of the event
	 * @return the type of the event
	 */
	public int getEventType(){
		return this.eventType;
	}
	
}
