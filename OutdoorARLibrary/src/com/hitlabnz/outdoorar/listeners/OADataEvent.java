/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.listeners;

import com.hitlabnz.outdoorar.api.OAScene;

/**
 * Represents a data event such as the addition or removal of a scene, or a manifest update
 * @author Leigh Beattie
 */
public class OADataEvent {
	
	private OAScene scene;
	private int eventType;
	
	public static final int SCENE_ADD = 0;
	public static final int SCENE_DELETE = 1;
	public static final int SCENES_CHANGED = 2;
	
	/**
	 * Create a data event relating to a scene addition or removal
	 * @param scene the relevant scene
	 * @param eventType the type of event
	 */
	public OADataEvent(OAScene scene, int eventType){
		this.scene = scene;
		this.eventType = eventType;
	}
		
	/**
	 * Get the scene affected by the event
	 * @return the affected scene if the event is a scene addition or removal, or null if the event is a manifest update
	 */
	public OAScene getScene(){
		return this.scene;
	}
	
	/**
	 * Gets the action of the event
	 * @return the action of the event
	 */
	public int getSceneAction(){
		return this.eventType;
	}
	
}
