/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.hitlabnz.androidar.utils.ObjectSerialisation;
import com.hitlabnz.outdoorar.data.OADataManager;

/**
 * A ready-to-use component for AR visualization.
 * This component should be started through OAAPI.  
 * @author  Leigh Beattie, Gun Lee
 */
public class OAARComponent extends OAARComponentBase {
	
	public final static String PARAM_GRID_VISIBLE = "PARAM_GRID_VISIBLE"; //this controls the visibility of the grid. Please include a boolean value with the parameter. True by default.
	public final static String PARAM_COMPASS_VISIBLE = "PARAM_COMPASS_VISIBLE"; //this controls the visibility of the compass. Please include a boolean value with the parameter. True by default.
	
	// setup options based on intent extras from OAAPI
	@Override
	protected int setupOptions() {
		int option = 0;
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			if(extras.containsKey("PARAMS")){
				
				HashMap<String, Object> parameters = null;

				try{
					ByteArrayInputStream inParams = new ByteArrayInputStream(extras.getByteArray("PARAMS"));
					parameters = (HashMap<String, Object>)ObjectSerialisation.deserializeObject(inParams);
				}
				catch(ClassCastException e){
					Log.e("Class cast Exception", "PARAMS is the wrong type, it should be a HashMasp<String, Object>");
				}
				
				if(parameters != null){
			        // The parameter for the grid visibility
					try{
						if(parameters.containsKey(PARAM_GRID_VISIBLE)){
							Boolean gridVisible = (Boolean)parameters.get(PARAM_GRID_VISIBLE);
							if(!gridVisible)
								option |= OAARComponentBase.OPTION_HIDE_GRID;							
						}
					}
					catch(ClassCastException e){
						Log.e("Class cast Exception", "PARAM_GRID_VISIBLE is the wrong type, it should be a Boolean value");
					}
					
			        // The parameter for the compass visibility
					try{
						if(parameters.containsKey(PARAM_COMPASS_VISIBLE)){
							Boolean compassVisible = (Boolean)parameters.get(PARAM_COMPASS_VISIBLE);
							if(!compassVisible)
								option |= OAARComponentBase.OPTION_HIDE_COMPASS_VIEW;
						}
					}
					catch(ClassCastException e){
						Log.e("Class cast Exception", "PARAM_COMPASS_VISIBLE is the wrong type, it should be a Boolean value");
					}
					
				}
			}
		}
		
		return option;
	}

	// setup scenes based on intent extras from OAAPI
	@Override
	protected void setupScenes(OADataManager dataManager) {
		super.setupScenes(dataManager);
		
		List<OAScene> scenes = null;
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			if(extras.containsKey("DATA_SCENES")){
				
				ByteArrayInputStream inScenes = new ByteArrayInputStream(extras.getByteArray("DATA_SCENES"));
				scenes = (List<OAScene>)ObjectSerialisation.deserializeObject(inScenes);
			}
			else{
				Log.e("Bundle Error", "No data manager scenes recieved in bundle.");
			}
		}
		
		if(scenes != null) {
			for(OAScene scene: scenes)
				dataManager.addScene(scene);
		}
	}
	
	// show details of the selected scene
	@Override
	protected void onSceneSelected(OAScene scene) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(scene.name);
		builder.setMessage("Category: " + scene.category + "\n" + scene.description);
		builder.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setCancelable(true);
		builder.create().show();
		
		super.onSceneSelected(scene);
	}
	
}
