package com.ecn.ei3info.sig_ar;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.hitlabnz.outdoorar.data.OADataManager;


public class DataManager extends OADataManager{
	
	private static DataManager singletonInstance = null;
	//protected static String workingPath="SIGAR";
	
	public DataManager(String workingPath){
		super(workingPath);
	}
	
	public static DataManager getInstance() {
		if(DataManager.singletonInstance == null)
			DataManager.singletonInstance = new DataManager("SIGAR");
		return DataManager.singletonInstance;
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
