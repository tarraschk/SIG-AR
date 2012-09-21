/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

import com.hitlabnz.outdoorar.data.OADataManager;

/**
 * A ready-to-use icon source class.
 * @author Tim Hobbs
 */
public class OAIconSource extends OAIconSourceBase {

	private OADataManager dataManager;
	private Map<String, Drawable> iconMap = new HashMap<String, Drawable>();
	
	public OAIconSource(OADataManager dataManager){
		this.dataManager = dataManager;
	}
	
	@Override
	public Drawable getIcon(String sceneCategory) {
		if(sceneCategory == null)
			return defaultIcon;
		
		if(iconMap.containsKey(sceneCategory)) return iconMap.get(sceneCategory);
		Drawable icon = dataManager.getIcon(sceneCategory);
		if(icon != null) {
			iconMap.put(sceneCategory, icon);
			return icon;
		}
		return defaultIcon;
	}
	
}
