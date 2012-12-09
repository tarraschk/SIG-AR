package com.ecn.ei3info.sig_ar;

import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.data.OADataManager;


public class DataManager extends OADataManager{
	
	static DataManager singletonInstance = null;
	
	protected static String workingPath="SIGAR";
	//protected static String modelPath = "";
	//protected static String iconPath = "";
	
	//private static final String MODELS_DIRECTORY = "3Dmodels";
	//private static final String ICONS_DIRECTORY = "icons";
	
	//protected ArrayList<Scene> scenes = new ArrayList<Scene>();;
	
	public DataManager(String Path){
		super(Path);
	}
	
	public static DataManager getInstance(boolean a) {
		if(DataManager.singletonInstance == null){
			DataManager.singletonInstance = new DataManager(workingPath);
		}
		if(a){
			DataManager MAP=new DataManager(workingPath);
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
	protected void setWorkingPath(String path) {
		// TODO Auto-generated method stub
		super.setWorkingPath(path);
		//workingPath=path;
		//super.setWorkingPath(Environment.getExternalStorageDirectory().toString() + "/" + path);
		
		/*workingPath = path;
		
		if(!workingPath.endsWith("/"))
			workingPath += "/";
		
		modelPath = workingPath + MODELS_DIRECTORY + "/";
		iconPath  = workingPath + ICONS_DIRECTORY + "/";
	*/
		
	}

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

	
	
	
	
		//TODO Generate Xml file
}
