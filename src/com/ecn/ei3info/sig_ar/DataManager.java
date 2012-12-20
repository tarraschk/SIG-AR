package com.ecn.ei3info.sig_ar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hitlabnz.androidar.data.Coordinate;
import com.hitlabnz.androidar.data.ModelData;
import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.androidar.data.representation.model.Transform;
import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.data.OADataManager;
import com.hitlabnz.outdoorar.data.OADataManagerAssets;
import com.hitlabnz.outdoorar.data.OADataManagerLocal;


public class DataManager extends OADataManagerLocal{
	
	static DataManager singletonInstance = null;
	
	//protected static String workingPath="SIGAR";
	//protected static String modelPath = "";
	//protected static String iconPath = "";
	
	//private static final String MODELS_DIRECTORY = "3Dmodels";
	//private static final String ICONS_DIRECTORY = "icons";
	
	//protected ArrayList<Scene> scenes = new ArrayList<Scene>();;

	//public DataManager(String Path){
		//super(Path);
		//init();
		//super(Path);
	//}
	
	/**
	 * 
	 */
	public DataManager() {
		super();
	}

	/**
	 * @param workingPath
	 */
	public DataManager(String workingPath) {
		super(workingPath);
	}

	public static DataManager getInstance(boolean a) {
		if(DataManager.singletonInstance == null){
			DataManager.singletonInstance = new DataManager("SIGAR");
		}
		if(a){
			DataManager MAP=new DataManager("SIGAR");
			for(OAScene c:DataManager.singletonInstance.getSceneList()){
				if(((Scene) c).isActivated()){
					MAP.addScene(c);
				}
			}
			return MAP;
		}else{
			return DataManager.singletonInstance;	
		}

	}

	/** 
	 * @see com.hitlabnz.outdoorar.data.OADataManager#setWorkingPath(java.lang.String)
	 */
	/*@Override
	protected void setWorkingPath(String path) {
		//super.setWorkingPath(path);
		//workingPath=path;
		//super.setWorkingPath(Environment.getExternalStorageDirectory().toString() + "/" + path);
		
		/*workingPath = path;
		
		if(!workingPath.endsWith("/"))
			workingPath += "/";
		
		modelPath = workingPath + MODELS_DIRECTORY + "/";
		iconPath  = workingPath + ICONS_DIRECTORY + "/";
	
		
	}*/

	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.data.OADataManager#loadScenes()
	 */
	@Override
	protected void loadScenes() {
		super.loadScenes();
	}

	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.data.OADataManager#getSceneList()
	 */

	public List<Scene> getSceneList2(){
		List<Scene> ls= new ArrayList<Scene>(); 
		for (OAScene s : this.getSceneList()){
			if(s.getClass().equals(Scene.class)){
				ls.add((Scene) s);
			}
		}
		return ls;
	}
	
	public void addSceneFromSQLiteDB(Context c){
		//
		ArrayList<Scene> resultsScene = new ArrayList<Scene>();
		
		//open db
		SigarDB database= new SigarDB(c);
		SQLiteDatabase sqlDB = database.getReadableDatabase();
		
		// query to get all scene information
		Cursor cursor= sqlDB.rawQuery("SELECT * FROM scene;",null);
		
		if (cursor != null ) {
            if  (cursor.moveToFirst()) {
                  do {
                	  int ID= cursor.getInt(cursor.getColumnIndex("id_scene"));
                	  String Name = cursor.getString(cursor.getColumnIndex("name_scene"));
                	  String Description = cursor.getString(cursor.getColumnIndex("description"));
                	  String Category = Integer.toString(cursor.getInt(cursor.getColumnIndex("id_category")));
                	  boolean Activated= (cursor.getInt(cursor.getColumnIndex("activation")) != 0);
                	  
                	  double gps_longitude = cursor.getDouble(cursor.getColumnIndex("gps_longitude")); 
                	  double gps_latitude = cursor.getDouble(cursor.getColumnIndex("gps_latitude")); 
                	  double gps_altitude  = cursor.getDouble(cursor.getColumnIndex("gps_altitude")); 
                	  SceneLocation Location= new SceneLocation();
                	  Location.setAltitude(gps_altitude);
                	  Location.setLatitude(gps_latitude);
                	  Location.setLongitude(gps_longitude);                	  
                	
                	  float translation_x=cursor.getFloat(cursor.getColumnIndex("translation_x")); 
                	  float translation_y=cursor.getFloat(cursor.getColumnIndex("translation_y")); 
                	  float translation_z=cursor.getFloat(cursor.getColumnIndex("translation_z")); 
                    
                	  float rotation_x=cursor.getFloat(cursor.getColumnIndex("rotation_x")); 
                	  float rotation_y=cursor.getFloat(cursor.getColumnIndex("rotation_y")); 
                	  float rotation_z=cursor.getFloat(cursor.getColumnIndex("rotation_z")); 

                	  float scale_x=cursor.getFloat(cursor.getColumnIndex("scale_x")); 
                	  float scale_y=cursor.getFloat(cursor.getColumnIndex("scale_y")); 
                	  float scale_z=cursor.getFloat(cursor.getColumnIndex("scale_z")); 

                	  
                	  Transform modelTransform = new Transform();
                	  modelTransform.setScale(new Coordinate(scale_x, scale_y, scale_z));
                	  modelTransform.setRotation(new Coordinate(rotation_x, rotation_y, rotation_z));
                	  modelTransform.setTranslation(new Coordinate(translation_x,translation_y,translation_z));
                	  
                	  
                	  int id_author = cursor.getInt(cursor.getColumnIndex("id_author"));
//query tio get name....
                	  String Creator = "a developper";

                	  ModelData model= new ModelData();
                	  model.name=Integer.toString(cursor.getInt(cursor.getColumnIndex("id_object3d")));
                	  model.id=Integer.toString(cursor.getInt(cursor.getColumnIndex("id_object3d")));
                	  List<ModelData> models=new ArrayList<ModelData>();
                	  models.add(model);
                	  
                	  String ModelVisible = "XXX a supprimer inutile" ;
                		//dataSource.get(pos).models.get(0).addTransform(modelTransform);
  						/*	+"id_icon integer NOT NULL, "
  							
  							+"id_author integer NOT NULL, "
  							+"date_creation text, "
  							+"id_object3d integer NOT NULL, "
  							+;*/
  								
                	  
                	  Log.w("dbchare",Name);
                	 resultsScene.add(new Scene( ID, Name, Description,  Category, Creator, Activated, ModelVisible, Location, models, modelTransform));
                  }while (cursor.moveToNext());
            	} 
		}
		// add all scenes to DataManager
		DataManager.getInstance(false).addScenes(resultsScene);
		
	}
	
		//TODO Generate Xml file
}
