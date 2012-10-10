/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.listeners;

/**
 * Interface for data listeners
 * @author Leigh Beattie
 */
public interface OADataListener {
	
	/**
	 * Implement this method to handle data events
	 * @param event the data event to be handled
	 */
	public void dataUpdate(OADataEvent event);
}
