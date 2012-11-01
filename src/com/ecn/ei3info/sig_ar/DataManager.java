package com.ecn.ei3info.sig_ar;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.hitlabnz.outdoorar.api.OAScene;
import com.hitlabnz.outdoorar.data.OADataManager;


public class DataManager extends OADataManager{
	
	static DataManager singletonInstance = null;
	//protected static String workingPath="SIGAR";
	
	public DataManager(String workingPath){
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

	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.data.OADataManager#setWorkingPath(java.lang.String)
	 */
	@Override
	protected void setWorkingPath(String path) {
		// TODO Auto-generated method stub
		super.setWorkingPath(path);
	}

	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.data.OADataManager#loadScenes()
	 */
	@Override
	protected void loadScenes() {
		super.loadScenes();
			
		
		
	}

	
	
	
		//TODO Generate Xml file
}
