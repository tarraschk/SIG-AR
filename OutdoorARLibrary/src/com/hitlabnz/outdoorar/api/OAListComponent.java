/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import java.io.ByteArrayInputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.hitlabnz.androidar.utils.ObjectSerialisation;
import com.hitlabnz.outdoorar.data.OADataManager;

/**
 * A ready-to-use component for list visualization.
 * This component should be started through OAAPI.
 * @author Leigh Beattie
 */
public class OAListComponent extends OAListComponentBase {
	
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
				Log.e("Bundle Error", "No data manager recieved in bundle.");
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
