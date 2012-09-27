/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.data.representation.loader.MTLOBJLoader;
import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.listeners.OADataEvent;
import com.hitlabnz.outdoorar.listeners.OADataListener;

/**
 * A basic data manager class that manages scene data.
 * In default, the working path is set to "OutdoorAR".
 * 3D model files should placed in the "3Dmodels" sub-directory.
 * Icon files should placed in the "icons" sub-directory.
 * @author Leigh Beattie, Gun Lee 
 */
public class OADataManager{
	
	protected ArrayList<OAScene> scenes = new ArrayList<OAScene>();;
	protected ArrayList<OADataListener> listeners = new ArrayList<OADataListener>();
	
	// These paths are set on construction of an instance.
	protected String workingPath = "";
	protected String modelPath = "";
	protected String iconPath = "";
	
	private static final String DEFAULT_WORKING_PATH = "OutdoorAR";
	private static final String MODELS_DIRECTORY = "3Dmodels";
	private static final String ICONS_DIRECTORY = "icons";
		
	private boolean loaded = false;
	
	private int sceneIdGenerator = 2^30 + 1; // base number of auto-generated Id 
	
	
	/**
	 * Instantiate with the default working path. 
	 */
	public OADataManager(){		
		setWorkingPath(DEFAULT_WORKING_PATH);
	}
	
	/**
	 * Instantiate with the given working path. 
	 * 
	 * @param workingPath	The path to the working directory to load the scenes
	 */
	public OADataManager(String workingPath){		
		setWorkingPath(workingPath);
	}
	
	/**
	 * Get the path of 3D models.
	 * 
	 * @return	The path to 3D models
	 */
	public final String getModelPath(){
		return this.modelPath;
	}
	
	/**
	 * Get the path to the working directory. 
	 * @return	The path to the working directory
	 */
	public final String getWorkingPath(){
		return this.workingPath;
	}
	
	/**
	 * Set paths for the working directory, models, and icons.
	 * This method should not be called through public access.
	 * To set the working path, use constructor with proper argument when instantiating. 
	 * @param path	The path of the working directory
	 */
	protected void setWorkingPath(String path){
		this.workingPath = path;
		
		if(!this.workingPath.endsWith("/"))
			this.workingPath += "/";
		
		this.modelPath = this.workingPath + MODELS_DIRECTORY + "/";
		this.iconPath  = this.workingPath + ICONS_DIRECTORY + "/";
	}
	
	/**
	 * Add a scene to the data manager. 
	 * @param scene The scene to add
	 */
	public void addScene(OAScene scene){
		scenes.add(scene);
		if(scene.id == SceneData.ID_UNKNOWN)
			scene.id = sceneIdGenerator++;
		fireSceneAddedEvent(scene);
	}
	
	/**
	 * Add a list of scenes to the data manager 
	 * @param scenes	A list of scenes to add
	 */
	public void addScenes(List<? extends OAScene> scenes){
		for(OAScene scene: scenes){
			addScene(scene);
		}		
		// scene change event is now deprecated. fireScenesChangedEvent(); // not necessary since addScene() fires event already
	}
	
	/**
	 * Remove a scene from the data manager 
	 * @param scene The scene to remove.
	 * @return Returns true if the scene was successfully removed, false otherwise.
	 */
	public boolean removeScene(OAScene scene){
		boolean success = scenes.remove(scene);
		if(success)
			fireSceneDeletedEvent(scene);
		return success;
	}
	
	/**
	 * Clear all the scenes out of the datamanager and start new. 
	 */
	public void clearScenes(){		
		while(scenes.size() > 0)
			removeScene(scenes.get(0));
		
		loaded = false;
	}
	
	/**
	 * Get the list of scenes associated with this data manager.
	 * @return A list of SceneData objects that have been loaded by this data manager.
	 */
	public List<OAScene> getSceneList(){
		return this.scenes;
	}
	
	/**
	 * Get the number of scenes in the data manager.
	 * @return		number of scenes
	 */
	public int getSceneCount() {
		return scenes.size();
	}
	
	/**
	 * Gets a scene in the data manager with the given id.
	 * 
	 * @param id	id of the scene to get 
	 * @return 		The scene with the given id, null if no such scene.
	 */	
	private OAScene getSceneCache = null;
	public OAScene getScene(int id)
	{
		if(getSceneCache != null)
			if(getSceneCache.id == id)
				return getSceneCache;
				
		for(OAScene scene: scenes)
		{
			if(scene.id == id)
			{
				getSceneCache = scene;
				return scene;
			}
		}
		
		return null;
	}
	
	/**
	 * Add a data listener 
	 * @param listener Data Listener to be added
	 */
	public void addDataListener(OADataListener listener){
		this.listeners.add(listener);
	}
	
	/**
	 * Remove a data listener 
	 * @param listener DataListener to be removed
	 */
	public void removeDataListener(OADataListener listener){
		this.listeners.remove(listener);
	}
	
	
	private void fireSceneAddedEvent(OAScene scene){
		OADataEvent event = new OADataEvent(scene, OADataEvent.SCENE_ADD);
		for(OADataListener listener : listeners){
			listener.dataUpdate(event);
		}
	}
	
	private void fireScenesChangedEvent(){
		OADataEvent event = new OADataEvent(null, OADataEvent.SCENES_CHANGED);
		for(OADataListener listener : listeners){
			listener.dataUpdate(event);
		}
	}
	
	private void fireSceneDeletedEvent(OAScene scene){
		OADataEvent event = new OADataEvent(scene, OADataEvent.SCENE_DELETE);
		for(OADataListener listener : listeners){
			listener.dataUpdate(event);
		}
	}
	
	/**
	 * Returns a drawable of the icon for the map identified with given id in the icon directory at the working path.
	 * Looks for assets or local storage, depending on where the 3D models are read from. 
	 * @param id	string to identify the icon. (File name without extension.)
	 * @return		Drawable of the icon
	 */
	public Drawable getIcon(String id) {
		Drawable drawable = null;
		
		if(MTLOBJLoader.currentAssetContext() == null)
			drawable = BitmapDrawable.createFromPath(iconPath + id + ".png");
		else {
			try {
				drawable = Drawable.createFromStream(MTLOBJLoader.currentAssetContext().getAssets().open(iconPath + id + ".png"), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return drawable;
	}
	
	/**
	 * Called back when the library is ready for data manager to load up (or add) the scenes. 
	 * Subclasses should override this method, 
	 * and populate the data manager by adding scenes using the addScene() or addScenes() methods.
	 * Never call this method directly in subclasses, but should use startLoading() method instead.
	 */
	protected void loadScenes() {
		// Nothing to do in this base data manager class.
		// Subclasses would call addScenes() here.
	}
	
	
	/**
	 * Called back when the library is ready for the data manager to load up (or add) the scenes.
	 * This method triggers the loadScenes() method, and makes sure it is not called multiple times.
	 * Subclasses should override loadScenes() method, and never override this method. 
	 */
	final public void startLoading(){
		if(!loaded) {
			loadScenes();
			loaded = true;
		}
	}
	
	public static boolean copyFileFromAssetToInternalStorage(String assetPath, String fileName, Context context) {
		
		// open output file in internal storage
		OutputStream localFile = null;
		
		try {
			localFile = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		} 
		catch (FileNotFoundException e) {
			// ignore this error. it will create one if not found anyway.
		}
		
		// just to make sure
		if(localFile == null){
			return false;
		}

		// open input file in asset
		InputStream assetFile;
		try {
			assetFile = context.getAssets().open(assetPath + fileName);
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// transfer bytes from dbAsset to localDb
		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = assetFile.read(buffer)) > 0){
				localFile.write(buffer, 0, length);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// close the streams
		try {
			assetFile.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			localFile.flush();
			localFile.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	  }
}
