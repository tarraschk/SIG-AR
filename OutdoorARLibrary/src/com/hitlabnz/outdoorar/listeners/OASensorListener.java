/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.listeners;

/**
 * Interface for Sensor Listeners
 * @author Leigh Beattie
 * @deprecated
 */
public interface OASensorListener {
	
	/**
	 * Implement this method to handle sensor events
	 * @param event the event to be handled
	 */
	public void sensorUpdate(OASensorEvent event);
}
