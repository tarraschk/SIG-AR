/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import android.graphics.drawable.Drawable;

import com.hitlabnz.androidar.data.IconSource;

/**
 * A base class for icon sources used in map components.
 * @author Gun Lee
 */
public class OAIconSourceBase implements IconSource {
	
	protected Drawable defaultIcon = null;
	
	public void setDefaultIcon(Drawable icon) {
		defaultIcon = icon;
	}
	
	@Override
	public Drawable getIcon(String id) {
		return defaultIcon;
	}
}
