/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.data;

import java.io.File;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.data.management.DataLoader;
import com.hitlabnz.androidar.data.management.DataLoaderLocalSQL;
import com.hitlabnz.androidar.data.management.DataLoaderLocalXML;
import com.hitlabnz.androidar.data.representation.loader.MTLOBJLoader;
import com.hitlabnz.outdoorar.api.OAScene;

/**
 * A data manager class that loads scenes from XML or SQLite DB files on local storage (external storage on the device).
 * In default, scenes.xml or scenes.db files in the working path are used to read the scenes from.
 * @author Leigh Beattie, Gun Lee
 */
public class OADataManagerLocal extends OADataManager {
	
	private static final String DEFAULT_XML_SCENES_FILE = "scenes.xml";
	private static final String DEFAULT_DB_SCENES_FILE = "scenes.db";
	
	private String scenesFile = null;
	
	private DataLoader loader = null;
	
	/**
	 * Instantiate with the default working path. 
	 */
	public OADataManagerLocal() {
		super();
		init();
	}
	
	/**
	 * Instantiate with the given working path. 
	 * @param workingPath	The relative path in the local storage to the working directory
	 */
	public OADataManagerLocal(String workingPath) {
		super(workingPath);
		init();
	}
	
	/**
	 * Set the name of a scenes file.
	 * If a scenes file is not set through this method, the default scenes file (scenes.xml or scenes.db) will be used.
	 * The scenes file should be in the working path, and could be in XML (.xml) or SQLite DB (.db) formats.
	 * This method only takes effect before the scenes are loaded from the scenes file.
	 * @param fileName	The name of the scenes file.
	 * @return	true if succeeded, false if failed.
	 */
	public boolean setScenesFile(String fileName) {
		// check if file exists on the working path
		File scenesFile = new File(workingPath + fileName);
		if(!scenesFile.exists())
			return false;
		
		// instantiate loader according to the file type
		if(fileName.endsWith(".xml") || fileName.endsWith("XML")) {
			try {
				loader = new DataLoaderLocalXML(this.workingPath + fileName);
			} catch (Exception e) {
				e.printStackTrace();
				loader = null;
				return false;
			}
		} else if(fileName.endsWith(".db") || fileName.endsWith(".DB")) {
			try {
				loader = new DataLoaderLocalSQL(this.workingPath + fileName);
			} catch (Exception e) {
				e.printStackTrace();
				loader = null;
				return false;
			}
		} else {
			return false;
		}
		
		// only set the scenesFile if successful
		this.scenesFile = fileName;
		
		return true;
	}
	
	/**
	 * Set paths for the working directory, models, and icons.
	 * This method should not be called through public access.
	 * To set the working path, use constructor with proper argument when instantiating. 
	 * @param path	The path of the working directory
	 */
	@Override
	protected void setWorkingPath(String path) {
		super.setWorkingPath(Environment.getExternalStorageDirectory().toString() + "/" + path);
	}
	
	/**
	 * Getter of the data loader which loads scene from scenes file
	 * @return	The data loader
	 */
	protected DataLoader getDataLoader() {
		return loader;
	}
	
	private final void init(){
		// tell the model loader to read from the local file system 
		MTLOBJLoader.loadFromLocalFile();
		
		// try to set the scenes file to default files
		if(!setScenesFile(OADataManagerLocal.DEFAULT_XML_SCENES_FILE))
			setScenesFile(OADataManagerLocal.DEFAULT_DB_SCENES_FILE);
	}
	
	/**
	 * Called back when the library is ready for data manager to load up (or add) the scenes. 
	 * Populates the data manager by adding scenes.
	 */
	@Override
	protected void loadScenes() {
		if(getDataLoader() != null) {
			List<SceneData> scenes = getDataLoader().getSceneData();
			for(SceneData scene: scenes)
				addScene(new OAScene(scene));
		}
	}
	
	/**
	 * Returns a drawable of the icon for the map identified with given id in the icon directory at the working path. 
	 * @param id	string to identify the icon. (File name without extension.)
	 * @return		Drawable of the icon
	 */
	@Override
	public Drawable getIcon(String id) {
		Drawable drawable = BitmapDrawable.createFromPath(iconPath + id + ".png");
		return drawable;
	}
	
}
