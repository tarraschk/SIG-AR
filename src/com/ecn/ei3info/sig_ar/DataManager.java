package com.ecn.ei3info.sig_ar;

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
	
		//TODO Generate Xml file
}
