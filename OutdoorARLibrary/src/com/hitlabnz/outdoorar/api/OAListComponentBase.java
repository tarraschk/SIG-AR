/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.viewcomponents.ListViewComponent;
import com.hitlabnz.androidar.viewcomponents.ListViewComponentListener;
import com.hitlabnz.outdoorar.data.OADataManager;
import com.hitlabnz.outdoorar.listeners.OADataEvent;
import com.hitlabnz.outdoorar.listeners.OADataListener;

/**
 * An abstract base class for list visualization components
 * @author Gun Lee
 */
public class OAListComponentBase extends OAComponent {

	private OADataManager dataManager; // read-only through getDataManager(), customize/initialize through setupDataManager()
	private OASensorManager sensorManager; // read-only through getSensorManager(), customize/initialize through setupSensorManager()
	
	private ListViewComponent listView;
	
	private int options = 0;

	/**
	 * Getter of the data manager in use.
	 * 
	 * @return The data manager in use.
	 */
	public final OADataManager getDataManager() {
		return dataManager;
	}
	
	/**
	 * Getter of the sensor manager in use
	 * 
	 * @return	The sensor manager in use
	 */
	public final OASensorManager getSensorManager() {
		return sensorManager;
	}

	// Methods to override/implement in custom map view activities
	/**
	 * Called back for setting up options for the Map component The default
	 * implementation returns a value with no options set (0). Subclasses
	 * overriding this method must implement it by returning option values
	 * composed with bit OR(|). For example: return (OPTION_A | OPTION_B);
	 * 
	 * @return
	 */
	protected int setupOptions() {
		return 0;
	}
	
	/**
	 * Called back for setting up the data manager.
	 * The default implementation returns a new instance of OADataManager with working directory set to "OutdoorAR".
	 * Subclasses overriding this method must implement it by returning an instance of data manager to use.
	 * The instance of data manager would be whether created within this method, or an already existing one could be reused.
	 * 
	 * @return	An instance of data manager to use in this component.
	 */
	protected OADataManager setupDataManager() {
		return new OADataManager("OutdoorAR");
	}
	
	/**
	 * Called back when the system is ready to load scenes initially.
	 * The default implementation triggers loadScenes() method of the data manager.
	 * Subclasses could override the behavior to setup custom scene data.
	 * 
	 * @param dataManager	The data manager where to populate the scene.	
	 */
	protected void setupScenes(OADataManager dataManager) {
		dataManager.startLoading();
	};
	
	/**
	 * Called back for setting up the sensor manager.
	 * The default implementation returns a new instance of OASensorManager with useCompass argument set to true.
	 * Subclasses overriding this method must implement it by returning an instance of sensor manager to use.
	 * The instance of sensor manager would be whether created within this method, or an already existing one could be reused.
	 * 
	 * @return	An instance of sensor manager to use in this component.
	 */
	protected OASensorManager setupSensorManager() {
		return new OASensorManager(this, true);
	}
	
	/**
	 * Called back for setting up a custom UI layout.
	 * Subclasses overriding this method must implement it by calling the setContentView() defined in the Android Activity class.
	 * The content view must include the listView in its view hierarchy. 
	 * Optionally, the subclass could also call the addContentView() method to add more UI layouts on top.
	 * @param listView	The view for list which is needed to be included in the custom UI view hierarchy.
	 */
	protected void setupUILayout(View listView) {
		setContentView(listView);
	}
	
	/**
	 * Called back for setting up a list adapter.
	 * Subclasses overriding this method must return an instance of subclass of OAListSceneAdapter. 
	 * @return
	 */
	protected ArrayAdapter<SceneData> setupAdapter() {
		return new OAListSceneAdapter(this);
	}
	
	/**
	 * Called back when a user selects a scene
	 * The default implementation does nothing.
	 *  
	 * @param scene A scene that is selected.
	 */
	protected void onSceneSelected(OAScene scene) {
		// does nothing in default
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		options = setupOptions();
		
		dataManager = setupDataManager();
		sensorManager = setupSensorManager();
		
		listView = new ListViewComponent(this, setupAdapter());
		listView.addListViewListener(
				new ListViewComponentListener() {
					@Override
					public void onListEvent(SceneData data) {
						onSceneSelected((OAScene)data);
					}
				});
		
		dataManager.addDataListener(
			new OADataListener() {
				@Override
				public void dataUpdate(OADataEvent event) {
					if (event.getScene() != null) {
						listView.addItem(event.getScene());
					}
				}
			});
		
		// load up scenes already in the data manager
		List<OAScene> initialScenes = dataManager.getSceneList();
		for(OAScene scene: initialScenes) {
			listView.addItem(scene);
		}
		
		// trigger to start loading up new scenes to the data manager
		setupScenes(dataManager);

		setupUILayout(listView);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.resume();
	}
	
}
