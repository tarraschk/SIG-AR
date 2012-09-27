/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hitlabnz.androidar.data.representation.loader.MTLOBJLoader;
import com.hitlabnz.androidar.utils.ObjectSerialisation;
import com.hitlabnz.outdoorar.data.OADataManager;
import com.hitlabnz.outdoorar.data.OADataManagerAssets;

/**
 * API class for using ready-to-use components.
 * For the map component, a valid Google Map API key must be defined as "map_api_key" string resource.
 * @author  Leigh Beattie
 */
public class OAAPI{

	
	// DEFINE DATA

	private List<OAScene> worldscenes;
	private OADataManager dataManager;
	private Context context;
	
	//DEFINE COMPONENT PARAMETERS
	
	private Map<String, Object> viewParamsAR;
	private Map<String, Object> viewParamsMap;
	
	
	public OAAPI(Context context){
		
		this.context = context;
		
		worldscenes = new ArrayList<OAScene>();
		dataManager = new OADataManagerAssets(context);
		dataManager.startLoading();
		
		viewParamsAR = new HashMap<String, Object>();
		viewParamsMap = new HashMap<String, Object>();
		
		MTLOBJLoader.loadFromLocalAssets(context);
	}

	
	public void setTitleImageURL(String URL){
		
	}
	
	public void setTitleText(String text){
		
	}

	public void setTitleImageResource(String resource){
		
	}
	
	/**
	 * This method sets the dictionary of parameters to be used by the AR View.
	 * @param params The dictionary of parameters
	 */
	public void setARViewParams(Map<String, Object> params){
		this.viewParamsAR = params;
	}
	
	/**
	 * This method gets the dictionary of parameters to be used by the AR View.
	 * @return The dictionary of parameters for this object.
	 */
	public Map<String, Object> getARViewParams(){
		return this.viewParamsAR;
	}
	
	/**
	 * This method sets the dictionary of parameters to be used by the Map View.
	 * @param params The dictionary of parameters.
	 */
	public void setMapViewParams(Map<String, Object> params){
		this.viewParamsMap = params;
	}
	
	/**
	 * This method gets the dictionary of parameters to be used by the AR View.
	 * @return The dictionary of parameters for this object.
	 */
	public Map<String, Object> getMapViewParams(){
		return this.viewParamsMap;
	}
	
	/**
	 * Sets the list of world scenes
	 * @param worldScenes  the new list of world scenes to be used
	 * @uml.property  name="worldscenes"
	 */
	public void setWorldScenes(List<OAScene> worldScenes){
		
	}
	
	/**
	 * Gets a list of all world scenes
	 * @return  List containing all world scenes
	 * @uml.property  name="worldscenes"
	 */
	public final List<OAScene> getWorldScenes(){
		return this.worldscenes;
	}
	
	/**
	 * Use this method to remove a world scene from the database of scenes. Any
	 * scenes you remove will no longer show up in the respective activities that you start
	 * using this API.
	 * @param worldScene
	 */
	public void removeWorldScene(OAScene worldScene){
		this.worldscenes.remove(worldScene);
	}
	
	/**
	 * Use this method to add a world scene into the database of scenes. Any
	 * scenes you add will be used in any respective activitys that you start
	 * using this API.
	 * @param worldscene The scene you want to add to the database of scenes.
	 */
	public void addWorldScene(OAScene worldscene){
		this.worldscenes.add(worldscene);
		dataManager.addScene(worldscene);
	}
	
	/**
	 * Enable the list view mode. This will start a new activity showing the scenes that 
	 * you added in a list view.
	 */
	public void startListComponent(){
		
		//////////////////////////////////////////////////
        // Start the OAListView							//
        //////////////////////////////////////////////////
		
		//while(!dataManager.finishedLoading()); //block till the dataManager has finished loading.
		
		Intent intent = new Intent(context, OAListComponent.class);
		Bundle extras = new Bundle();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ObjectSerialisation.serializeObject(dataManager.getSceneList(), out);
		
		extras.putByteArray("DATA_SCENES", out.toByteArray());
		
		intent.putExtras(extras);
		
		context.startActivity(intent);
		
	}
	
	/**
	 * Enable the AR view mode. This will start a new activity showing the scenes
	 * that you added in an AR visualisation.
	 */
	public void startARComponent(){
		
		//////////////////////////////////////////////////
        // Start the OAWorld with ARView enabled		//
        //////////////////////////////////////////////////
		
		//while(!dataManager.finishedLoading()); //block till the dataManager has finished loading.
		
		Intent intent = new Intent(context, OAARComponent.class);
		Bundle extras = new Bundle();
		
		ByteArrayOutputStream outScenes = new ByteArrayOutputStream();
		ObjectSerialisation.serializeObject(dataManager.getSceneList(), outScenes);
		
		extras.putByteArray("DATA_SCENES", outScenes.toByteArray());
		
		//////////////////////////////////////////////////
        // Extract parameters from the dictionary to send// 
		//				off to the world activity		//
        //////////////////////////////////////////////////
		
		//if(viewParamsAR.containsKey(OAWorldActivity.PARAM_ICON_FADE)){
		ByteArrayOutputStream outView = new ByteArrayOutputStream();
		ObjectSerialisation.serializeObject(viewParamsAR, outView);
		
		extras.putByteArray("PARAMS", outView.toByteArray());
		//}
		
		intent.putExtras(extras);
		
		context.startActivity(intent);
	}

	/**
	 * Enable the Map view mode. This will start a new activity showing the scenes that
	 * you added in a map visualisation.
	 */
	public void startMapComponent(){
		
		//////////////////////////////////////////////////
        // Start the Map View							//
        //////////////////////////////////////////////////
		
	//	while(!dataManager.finishedLoading());
		
		Bundle extras = new Bundle();
		
		ByteArrayOutputStream outScenes = new ByteArrayOutputStream();
		ObjectSerialisation.serializeObject(dataManager.getSceneList(), outScenes);
		extras.putByteArray("DATA_SCENES", outScenes.toByteArray());
		//extras.putString("type_ar", "TYPE_AR_BUNDLE");
		
		Intent intent = new Intent(context, OAMapComponent.class);
		intent.putExtras(extras);
		
		context.startActivity(intent);
	}
	
	/**
	 * Get the data manager if you absolutely need to.
	 * @return  The datamanager associated with the API.
	 * @uml.property  name="dataManager"
	 */
	public OADataManager getDataManager(){
		return this.dataManager;
	}
	
}
