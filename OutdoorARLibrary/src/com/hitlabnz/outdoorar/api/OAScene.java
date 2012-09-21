/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import java.util.ArrayList;

import com.hitlabnz.androidar.data.Coordinate;
import com.hitlabnz.androidar.data.ModelData;
import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.androidar.data.representation.model.Transform;

/**
 * A class representing a scene, or a point of interest.
 * @author  Leigh Beattie
 */

public class OAScene extends SceneData {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor with model id.
	 * @param latitude	The latitude of the scene
	 * @param longitude	The longitude of the scene
	 * @param name		A descriptive name for the scene. i.e. Park or Dairy
	 * @param idModel	The id of the model to use for this scene in the AR view. 
	 * Models should be in a folder named by its id, and located in the 3DModels folder under the working path (see OADataManager). 
	 * The folder containing the model should contain a 3D model file with supported file name (model.obj).
	 */
	public OAScene(double latitude, double longitude, String name, String modelID){
		super();
		
		SceneLocation location = new SceneLocation();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		
		this.location = location;
		this.name = name;
		
		//Build a model and add it to the scene. 
		ModelData model = new ModelData();
		
		model.name = name;
		model.id = modelID;

		Transform modelTransform = new Transform();
		modelTransform.setTranslation(new Coordinate(0,-2,0));
		model.addTransform(modelTransform);
		
		this.models.add(model);
	}
	
	/**
	 * Constructor with custom model data.
	 * @param latitude	The latitude of the scene
	 * @param longitude	The longitude of the scene
	 * @param name		A descriptive name for the scene. i.e. Park or Dairy
	 * @param model		An instance of ModelData
	 */
	public OAScene(double latitude, double longitude, String name, ModelData model){
		super();
		
		SceneLocation location = new SceneLocation();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		
		this.location = location;
		this.name = name;
		
		this.models.add(model);
	}
	
	/**
	 * Constructor with multiple models.
	 * Be sure to setup the transforms on all the models so they don't intersect with each other.
	 * Add all your models to an array list and then pass it to the scene through the models argument.
	 * @param latitude	The latitude of the scene
	 * @param longitude	The longitude of the scene
	 * @param name		A descriptive name for the scene. i.e. Park or Dairy
	 * @param models	The list of models you want in the scene.
	 */
	public OAScene(double latitude, double longitude, String name, ArrayList<ModelData> models){
		super();
		SceneLocation location = new SceneLocation();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		
		this.location = location;
		this.name = name;
		
		for(ModelData model : models){
			this.models.add(model);
		}
	}
	
	/**
	 * Minimal constructor 
	 * @param latitude	The latitude of the scene
	 * @param longitude	The longitude of the scene
	 * @param name		A descriptive name for the scene. i.e. Park or Dairy
	 */
	public OAScene(double latitude, double longitude, String name){
		super();
		SceneLocation location = new SceneLocation();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		
		this.location = location;
		this.name = name;
	}
	
	/**
	 * Copy constructor to support lower level library compatibility.
	 * @param scene
	 */
	public OAScene(SceneData scene) {
		super(scene);
	}
	
	/**
	 * Constructor for undefined scene.
	 */
	public OAScene() {
		super();
	}
		
	/**
	 * Gets the latitude of the scene
	 * @return	latitude
	 */
	public double getLatitude(){
		return location.getLatitude();
	}
	
	/**
	 * Gets the longitude of the scene
	 * @return	longitude
	 */
	public double getLongitude(){
		return location.getLongitude();
	}
	
	/**
	 * Gets the name of the scene
	 * @return		the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the scene
	 * @param name	name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the description of the scene
	 * @return	description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description of the scene
	 * @param description	description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the category of the scene
	 * @return	category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Sets the category of the scene
	 * @param category	category
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Sets an extra attribute of the scene
	 * @param key	A key (or name) of the attribute to set
	 * @param value	The value to set
	 */
	public void setExtraAttrib(String key, String value) {
		extraAttribs.put(key,  value);
	}
	
	/**
	 * Gets the value of an extra attribute with the key
	 * @param key	The key (or name) of the attribute to get
	 * @return		The value of the attribute with the given key
	 */
	public String getExtraAttrib(String key) {
		if(extraAttribs.containsKey(key))
			return extraAttribs.get(key);
		return null;
	}
	
	/**
	 * Get the id of the scene
	 * @return	id of the scene. ID_UNKNOWN if the scene has no id assigned.
	 */
	public int getId() {
		return this.id;
	}
	
}
