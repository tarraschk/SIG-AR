/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.data;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.data.management.DataLoader;
import com.hitlabnz.androidar.data.management.DataLoaderLocalSQL;
import com.hitlabnz.androidar.data.management.DataLoaderLocalXML;
import com.hitlabnz.androidar.data.representation.loader.MTLOBJLoader;
import com.hitlabnz.outdoorar.api.OAScene;

/**
 * A data manager class that loads scenes from XML or SQLite DB files in the assets of the application.
 * In default, scenes.xml or scenes.db files in the working path are used to read the scenes from.
 * @author Gun Lee
 *
 */
public class OADataManagerAssets extends OADataManager {
	
	private Context assetsContext = null;
	
	private static final String DEFAULT_XML_SCENES_FILE = "scenes.xml";
	private static final String DEFAULT_DB_SCENES_FILE = "scenes.db";
	
	private String scenesFile = null;
	private String localDbPath = null;
	
	private DataLoader loader = null;
	
	
	/**
	 * Instantiate with the default working path.
	 * @param context	The context to access the assets from
	 */
	public OADataManagerAssets(Context context) {
		super();
		init(context);
	}
	
	/**
	 * Instantiate with the given working path.
	 * @param workingPath	The relative path in the assets to the working directory
	 * @param context		The context to access the assets from
	 */
	public OADataManagerAssets(String workingPath, Context context) {
		super(workingPath);
		init(context);
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
		List<String> assetList = null;
		try {
			assetList = Arrays.asList(assetsContext.getAssets().list(workingPath.substring(0, workingPath.length() - 1)));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		if(!assetList.contains(fileName))
			return false;
		
		// instantiate loader according to the file type
		if(fileName.endsWith(".xml") || fileName.endsWith("XML")) {
			try {
				loader = new DataLoaderLocalXML(this.workingPath + fileName, assetsContext);
			} catch (Exception e) {
				e.printStackTrace();
				loader = null;
				return false;
			}
		} else if(fileName.endsWith(".db") || fileName.endsWith(".DB")) {
			try {
				if(setupLocalDB(fileName))
					loader = new DataLoaderLocalSQL(localDbPath);
				else
					return false;
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
	 * Getter of the path to the local copy of the database file originally in the assets
	 * @return	The path to the local database file
	 */
	public String getLocalDbPath() {
		return localDbPath;
	}
	
	/**
	 * Getter of the data loader which loads scene from scenes file
	 * @return	The data loader
	 */
	protected DataLoader getDataLoader() {
		return loader;
	}
	
	// initialization called from constructors
	private final void init(Context context) {
		// set context to access assets from
		assetsContext = context;
		
		// tell the model loader to read from the assets
		MTLOBJLoader.loadFromLocalAssets(assetsContext);
		
		// try to set the scenes file to default files
		if(!setScenesFile(OADataManagerAssets.DEFAULT_XML_SCENES_FILE))
			setScenesFile(OADataManagerAssets.DEFAULT_DB_SCENES_FILE);
	}
	
	// creates a local copy of db file from the working path in assets
	private final boolean setupLocalDB(String dbFileName) {
		// private class for help creating database file in setupLocalDB()
		class DBHelper extends SQLiteOpenHelper {
			public DBHelper(Context context, String name) {
				super(context, name, null, 1);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}			
		};
		
		try {
			// Open then close a SQLite database to prepare for copying
			DBHelper dbHelper = new DBHelper(assetsContext, dbFileName);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			localDbPath = db.getPath();
			db.close();
			dbHelper.close();
			
			// Open the empty db as the output stream
			OutputStream localDb = new FileOutputStream(localDbPath);

			// Open your local db as the input stream
			InputStream dbAsset = assetsContext.getAssets().open(workingPath + dbFileName);
			
			// transfer bytes from dbAsset to localDb
			byte[] buffer = new byte[1024];
			int length;
			while ((length = dbAsset.read(buffer)) > 0)
				localDb.write(buffer, 0, length);
			
			// Close the streams
			dbAsset.close();
			localDb.flush();
			localDb.close();
		} catch (Exception e) {
			e.printStackTrace();
			localDbPath = null; // reset to null if failed
			return false;
		}
		
		return true;
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
		Drawable drawable = null;
		try {
			drawable = Drawable.createFromStream(assetsContext.getAssets().open(iconPath + id + ".png"), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return drawable;
	}
}
