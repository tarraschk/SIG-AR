package com.ecn.ei3info.sig_ar;

import android.content.Context;
import android.util.Log;

import com.hitlabnz.outdoorar.data.OADataManagerAssets;

public class DataManagerAssets extends OADataManagerAssets{

	private static DataManagerAssets instance = null;

	
	public DataManagerAssets(Context context) {
		super(context);
	}
	
	public DataManagerAssets(String workingPath, Context context){
		super(workingPath,context);
	}
	
	public static DataManagerAssets getInstance(Context context) {
		if (instance == null){
			instance = new DataManagerAssets("SIGAR",context);
		}
		Log.w("myApp", Integer.toString(instance.getSceneCount()));
		return instance;
	}
	
	
	public int getSceneCount(){
		return this.scenes.size();
	}
	
	/**
	 * Called back when the library is ready for data manager to load up (or add) the scenes.
	 * Populates the data manager by adding scenes. 
	 */
	/*@Override
	protected void loadScenes() {
		if(getDataLoader() != null) {
			List<SceneData> scenes = getDataLoader().getSceneData();
			for(SceneData scene: scenes)
				if (scene.id){
					addScene(new OAScene(scene));
				}
		}
	}*/
	
}
