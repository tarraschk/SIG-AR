package com.ecn.ei3info.sig_ar;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hitlabnz.androidar.data.ModelData;
import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.outdoorar.api.OAScene;

public class Scene extends OAScene implements Comparable<Scene>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public boolean activated;
	public String modelVisible;

	//TODO ajouter les informations supplŽmentaires des sc�nes.
	
	/**
	 * 
	 */
	public Scene(){
		//TODO completer les constructeurs de scene
		super();
	}
	
	
	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}


	/**
	 * @param activated the activated to set
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}


	/**
	 * @return the modelVisible
	 */
	public String getModelVisible() {
		return modelVisible;
	}


	/**
	 * @param modelVisible the modelVisible to set
	 */
	public void setModelVisible(String modelVisible) {
		this.modelVisible = modelVisible;
	}


	/**
	 * @param sceneData
	 */
	public Scene(SceneData sceneData) {
		super(sceneData);
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param location
	 * @param _id
	 * @param _name
	 * @param _description
	 * @param _creator
	 */
	public Scene(SceneLocation location, int _id, String _name, String _description, String _creator) {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param Name
	 * @param ID
	 * @param Description
	 * @param Category
	 * @param Creator
	 * @param Activated
	 */
	public Scene( int ID,String Name, String Description, String Category, String Creator, boolean Activated, String ModelVisible, SceneLocation Location, List<ModelData> models){
		this.id=ID;
		this.name=Name;
		this.description=Description;
		this.category=Category;
		this.creator=Creator;
		this.activated=Activated;
		this.modelVisible=ModelVisible;
		this.location=Location;
		this.models=models;
		//TODO add transfrom!!!!!!!!!!
		
	}

	// methode de developpement ASUPPRIMER
	public void logScene(){
		Log.w("myScenes", "name: "+name);
		Log.w("myScenes", "id: "+Integer.toString(id));
		Log.w("myScenes", "description: "+description);
		Log.w("myScenes", "activated: "+activated);
		Log.w("myScenes", "category: "+category);
		Log.w("myScenes", "creator: "+creator);
		Log.w("myScenes", "location");
	//	Log.w("myScenes", "  longitude: "+Double.toString(location.getLongitude()));
		//Log.w("myScenes", "  latitude: "+Double.toString(location.getLatitude()));
		//Log.w("myScenes", "  altitude: "+Double.toString(location.getAltitude()));
		Log.w("myScenes", "Model visible: "+modelVisible);
		Log.w("myScenes", "Models:");
		for(ModelData m:models){
			Log.w("myScenes", "Model: "+m.name);
			Log.w("myScenes", "Model id: "+m.id);
		}
		


	}
	/**
	 * Set the latitude of the scene
	 * @param latitude
	 */
	public void setLatitude(double latitude){
		this.location.setLatitude(latitude);
	}
	/**
	 * Set the longitude of the scene
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.location.setLongitude(longitude);
	}
	/**
	 * Set the altitude of the scene 
	 * @param altitude
	 */
	public void setAltitude(double altitude) {
		this.location.setAltitude(altitude);
	}
	/**
	 * get the altitude of the scene
	 * @return altitude
	 */
	public double getAltitude(){
		return this.location.getAltitude();
	}
	/**
	 * get the icon of scene
	 * @return Drawable icon
	 */
	public Drawable getIcon(){
		return DataManager.getInstance(false).getIcon(category);
	}
	
	/**
	 * calculate the distance between a scene and a POI
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public double getDistance(double latitude, double longitude) {
		//equatorial radius of Earth. No precision necessary, we'll use the distance to compare distances
		double r=6371000.0 ;
		double latitudeScene = this.location.getLatitude();
		double longitudeScene = this.location.getLongitude();
		double theta = longitude - longitudeScene;
		double dist = Math.sin(Math.toRadians(latitudeScene)) * Math.sin(Math.toRadians(latitude)) + Math.cos(Math.toRadians(latitudeScene)) * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(theta));
        dist = Math.abs(Math.round(Math.toDegrees(Math.acos(dist)) * 2*Math.PI * r /360 ));
		return dist;
	}
	


	public int compareTo(Scene another) {
		
			return this.getName().compareTo(another.getName());
		
		
	}

	
	
}
