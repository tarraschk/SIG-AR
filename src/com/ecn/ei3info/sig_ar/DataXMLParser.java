package com.ecn.ei3info.sig_ar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.ecn.ei3info.sig_ar.Scene;
import com.hitlabnz.androidar.data.Coordinate;
import com.hitlabnz.androidar.data.ModelData;
import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.androidar.data.representation.model.AnimatedTransform;
import com.hitlabnz.androidar.data.representation.model.Transform;

import android.util.Xml;


public class DataXMLParser {

	private static final String ns = null;

	public List<Scene> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readScenes(parser);
		} finally {
			in.close();
		}
	}
	private List<Scene> readScenes(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Scene> scenes = new ArrayList<Scene>();

		//changer type de return pour un datamanager...
		parser.require(XmlPullParser.START_TAG, ns, "scenes");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("scene")) {
				scenes.add(readScene(parser));
			} else {
				skip(parser);
			}
		}  
		return scenes;
	}



	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private Scene readScene(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "scene");
		
		//chaanger nom methode pour read scene
		Integer id = 0;
		String namescene = null;
		String category = null;
		String description = null;
		String creator = null;
		boolean activated = false;
		String modelVisible=null;

		// ajouter les autres caracteristiques


		//models
		List<ModelData> models=null;

		String number = null;
		//model
		String name_model = null;
		String id_model = null;
		String  type= null;
		SceneLocation  location= null;
		String  transformation= null;
		String  animation= null;
		// modifier les types
		/* String  longitude= null;
	    String  latitude= null;
	    String  altitude= null;
	    String  type= null;
	    String  type= null;*/


		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
//
	//		Log.w("myApp", name);
//
			if (name.equals("id")) {
				id = Integer.parseInt(readId(parser));
			} else if (name.equals("name")) {
				namescene = readNameScene(parser);
			} else if (name.equals("category")) {
				category = readCategory(parser);
			} else if (name.equals("description")) {
				description = readDescription(parser);
			} else if (name.equals("creator")) {
				creator = readCreator(parser);
			} else if (name.equals("location")) {
				location = readLocation(parser);
			} else if (name.equals("activated")) {
				activated = readBoolean(parser);
			} else if (name.equals("model_visible")) {
				modelVisible = readModelVisible(parser);
			} else if (name.equals("models")) {
				models = readModels(parser);	            
			} else {
				skip(parser);
			}
		}
		Scene test= new Scene(id,namescene,description,category,creator,activated,modelVisible,location,models,new Transform());//completer le constructuer en fonctiondes attributs
		test.logScene();
		return test;
	}

	// ID STRING TYPE??????

	// Processes id tags in the feed.
	private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "id");
		String id = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "id");
		return id;
	}

	// Processes name tags in the feed.
	private String readNameScene(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "name");
		String namescene = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "name");
		return namescene;
	}
	// Processes category tags in the feed.
	private String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "category");
		String category = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "category");
		return category;
	}
	// Processes description tags in the feed.
	private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "description");
		return description;
	}
	// Processes category tags in the feed.
	private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "creator");
		String creator = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "creator");
		return creator;
	}


	// Processes location tags in the feed.
	private SceneLocation readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "location");
		SceneLocation location=new SceneLocation();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("longitude")) {
				location.setLongitude(readDouble(parser));
			} else if (name.equals("latitude")){
				location.setLatitude(readDouble(parser));
			} else if (name.equals("altitude")){
				location.setAltitude(readDouble(parser));
			} else{
				skip(parser);
			}
		}  
		return location;
	}
	
	private String readModelVisible(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "model_visible");
		String modelVisible = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "model_visible");
		return modelVisible;
	}
	
	
	// Processes
	private List<ModelData> readModels(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "models");
		List<ModelData> models = new ArrayList<ModelData>();


		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			ModelData model=null;
			String name = parser.getName();
			if (name.equals("model")) {
				model = readModel(parser);
				models.add(model);
			} else {
				skip(parser);
			}
		}
		return models;
	}

	private ModelData readModel(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "model");
		ModelData model=new ModelData();




		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name1 = parser.getName();
			if (name1.equals("name_model")) {
				model.name = readText(parser);
			} else if (name1.equals("id_model")) {
				model.id = readText(parser);
			//} else if (name1.equals("type")) {
				//model.setModelType(Integer.parseInt(readText(parser)));
			} else if (name1.equals("transformation")) {
				model.addTransform(readTransformation(parser));
			} else if (name1.equals("animation")) {
				model.addAnimation(readAnimation(parser));
			} else {
				skip(parser);
			}
		}
		Transform modelTransform = new Transform();
		modelTransform.setTranslation(new Coordinate(0,-2,0));
		model.addTransform(modelTransform);


		return model;
	}

	private Transform readTransformation(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "transformation");
		Transform transform=new Transform();
		
		//A COMPLETER
		
		return transform;
	}	
	
	private AnimatedTransform readAnimation(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "transformation");
		AnimatedTransform animation=new AnimatedTransform();
		
		//A COMPLETER
		
		return animation;
	}	
	
	
	// For the tags,  extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}
	// For the numeric tags,  extracts their double values.
	private double readDouble(XmlPullParser parser) throws IOException, XmlPullParserException {
		double result = 0;
		if (parser.next() == XmlPullParser.TEXT) {
			result = Double.parseDouble(parser.getText().toString());
			parser.nextTag();
		}
		return result;
	}
	// For the boolean tags,  extracts their boolean values.
	private boolean readBoolean(XmlPullParser parser) throws IOException, XmlPullParserException {
		boolean result = true;
		if (parser.next() == XmlPullParser.TEXT) {
			result = Boolean.parseBoolean(parser.getText());
			parser.nextTag();
		}
		return result;
	}
	
	/**
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
