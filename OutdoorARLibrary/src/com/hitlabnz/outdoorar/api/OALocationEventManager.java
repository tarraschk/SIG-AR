package com.hitlabnz.outdoorar.api;

import java.util.ArrayList;

import android.location.Location;

import com.hitlabnz.androidar.tracking.TrackerEvent;
import com.hitlabnz.androidar.tracking.TrackerListener;

/**
 * Location manager monitors scenes/locations of interest, and triggers events when the user arrives at or leaves from the scene/location.
 * The event (OALocationEvent object) is delivered to the listener that implements OALocationEventListener interface.
 * The manager uses the defaultRange (initially 20 metres) if no range is provided with the location of interest. 
 * The manager filters out user location event with error greater than the userLocationErrorThreshold (initially 20 metres).
 * The manager uses overheadDisatnceForLeaving (initially 10 metres) to prevent generating continuous arrived/left events at the boundary of the range.
 * 
 * @author Gun Lee
 *
 */
public class OALocationEventManager {
	
	private ArrayList<LocationOfInterest> lois = new ArrayList<LocationOfInterest>();; // locations to monitor
	
	private ArrayList<OALocationEventListener> listeners = new ArrayList<OALocationEventListener>(); // listeners
	
	private float defaultRange = 20; // default range value for those location of interest without specific range
	private float userLocationErrorThreshold = 30; // threshold to make sure the user location information to use is within error threshold
	private float overheadDistanceForLeaving = 10; // overhead distance for leaving to prevent continuous arrived/left events at the boundary of range
	
	private boolean enabled = true;
	
	
	
	/**
	 * Constructor for a location manager object with a given listener
	 * @param sensorManager	The sensor manager that provides user location
	 * @param listener	The listener to be informed with the events
	 */
	public OALocationEventManager(OASensorManager sensorManager, OALocationEventListener listener) {
		this(sensorManager);
		
		listeners.add(listener);
	}
	
	
	/**
	 * Constructor for a location manager object
	 * @param sensorManager		The sensor manager that provides user location
	 */
	public OALocationEventManager(OASensorManager sensorManager) {
		
		sensorManager.addSensorListener(new TrackerListener() {

			@Override
			public void updatePose(TrackerEvent trackerevent) {
				if(trackerevent.getEventType() == TrackerEvent.EVENT_SENSOR_LOCATION) {
					update(trackerevent.getPosition());
				}
			}
			
		});
	}
	
	/**
	 * Add a listener
	 * @param listener
	 */
	public void addListener(OALocationEventListener listener) {
		if(listener != null && !listeners.contains(listener))
				listeners.add(listener);
	}
	
	/**
	 * Remove a listener
	 * @param listener
	 */
	public void removeListener(OALocationEventListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Interface to listen to location events
	 */
	public interface OALocationEventListener {
		abstract void onLocationEvent(OALocationEvent event);
	}
	
	/**
	 * Class of event objects that is passed to location event listener
	 * @author Gun Lee
	 *
	 */
	public class OALocationEvent {
		public static final int EVENT_UNDEFINED = 0;
		public static final int EVENT_ARRIVED_AT = 100;
		public static final int EVENT_LEFT_FROM = 200;
		
		/**
		 * Type of event
		 */
		public int type = EVENT_UNDEFINED;
		
		/**
		 * Id of the location where the event happened.
		 */
		public int locationId = -1;
		
		/**
		 * scene associated to the location. null if not scene associated.
		 */
		public OAScene scene = null;
		
		
		OALocationEvent(int type, int locationId, OAScene scene) {
			this.type = type;
			this.locationId = locationId;
			this.scene = scene;
		}
	}
	
	/**
	 * Add a scene as a location of interest to monitor with the default range
	 * @param scene	The scene to monitor
	 * @return	id of the location added. this id and the scene object will be passed through the location event
	 */
	public int addInterestedLocation(OAScene scene) {
		return addInterestedLocation(scene, RANGE_VALUE_USE_DEFAULT);
	}
	
	/**
	 * Add a scene as a location of interest to monitor with a given range
	 * @param scene	The scene to monitor
	 * @param range	The range around the scene to monitor in meters
	 * @return	id of the location added. this id and the scene object will be passed through the location event
	 */
	public int addInterestedLocation(OAScene scene, float range) {
		LocationOfInterest loi = new LocationOfInterest(scene, range);
		lois.add(loi);
		return loi.id;
	}
	
	/**
	 * Add a location of interest to monitor with the default range
	 * @param latitude	The latitude of the location in degrees
	 * @param longitude	The longitude of the location in degrees
	 * @return	id of the location added. this id will be passed through the location event
	 */
	public int addInterestedLocation(double latitude, double longitude) {
		return addInterestedLocation(latitude, longitude, RANGE_VALUE_USE_DEFAULT);
	}
	
	/**
	 * Add a location of interest to monitor with the given range
	 * @param latitude	The latitude of the location in degrees
	 * @param longitude	The longitude of the location in degrees
	 * @param range	The range around the location to monitor in meters
	 * @return	id of the location added. this id will be passed through the location event
	 */
	public int addInterestedLocation(double latitude, double longitude, float range) {
		LocationOfInterest loi = new LocationOfInterest(latitude, longitude, range);
		lois.add(loi);
		return loi.id;
	}
	
	/**
	 * Remove the location with the given id
	 * @param id	id of the location to remove
	 */
	public void removeInterestedLocation(int id) {
		for(LocationOfInterest loi: lois) {
			if(loi.id == id) {
				lois.remove(loi);
				break;
			}
		}
	}
	
	/**
	 * Remove the scene from the list and stop monitoring it.
	 * @param scene	The scene to stop monitoring
	 */
	public void removeInterestedLocation(OAScene scene) {
		for(LocationOfInterest loi: lois) {
			if(loi.scene == scene) {
				lois.remove(loi);
				break;
			}
		}
	}
	
	/**
	 * Get the default range value to use for those scenes/location that did not provided range.
	 * @return	The range value in meters
	 */
	public float getDefaultRange() {
		return this.defaultRange;
	}
	
	/**
	 * Set the default range value to use for those scenes/location that did not provided range.
	 * @param range		The range value in meters
	 */
	public void setDefaultRange(float range) {
		this.defaultRange = range;
	}
	
	
	/**
	 * Get overhead distance for leaving.
	 * @return	overhead The distance in metres
	 */
	public float getOverheadDistanceForLeaving() {
		return this.overheadDistanceForLeaving;
	}
	
	/**
	 * Set overhead distance for leaving.
	 * Default value is 
	 * @param overhead	The distance in metres
	 */
	public void setOverheadDistanceForLeaving(float overhead) {
		this.overheadDistanceForLeaving = overhead;
	}
	
	/**
	 * Get threshold of error for a user location to use
	 * @return	The error threshold in metres
	 */
	public float getUserLocationErrorThreshold() {
		return this.userLocationErrorThreshold;
	}
	
	/**
	 * Set threshold of error for a user location to use
	 * @param threshold		The error threshold in metres
	 */
	public void setUserLocationErrorThreshold(float threshold) {
		userLocationErrorThreshold = threshold;
	}
	
	
	/**
	 * Enable/disable the manager to start/stop generating events.
	 * The manager is enabled by default.
	 * @param enable	true to enable, false to disable.
	 */
	public void enable(boolean enable) {
		this.enabled = enable;
	}
	
	
	
	/* private implementations */
	
	private final static float RANGE_VALUE_USE_DEFAULT = -1; // value for range
	
	private int LOIcounter = 0;
	private class LocationOfInterest {
		
		
		int id;
		Location location;
		OAScene scene;
		float range = RANGE_VALUE_USE_DEFAULT; 
		
		boolean userAtLocation = false;
		
		LocationOfInterest(double latitude, double longitude, float range) {
			this.id = LOIcounter++;
			this.location = new Location("OALocationEventManager");
			this.location.setLatitude(latitude);
			this.location.setLongitude(longitude);
			this.scene = null;
			this.range = range;
		}
		
		LocationOfInterest(OAScene scene, float range) {
			this.id = LOIcounter++;
			this.location = new Location("OALocationEventManager");
			this.location.setLatitude(scene.getLatitude());
			this.location.setLongitude(scene.getLongitude());
			this.scene = scene;
			this.range = range;
		}
	}
	
	
	private void update(Location userLocation) {
		if(!enabled)
			return;
		
		// go through the list of location of interest
		for(LocationOfInterest loi: lois) {
			// calculate range around loi
			float range = loi.range;
			if(range == RANGE_VALUE_USE_DEFAULT)
				range = this.defaultRange;
			
			// calculate distance between loi and user
			float distance = userLocation.distanceTo(loi.location);
			
			// if user was already at location before, check if user left
			if(loi.userAtLocation) {
				// if far enough even considering the leaving overhead, trigger left from event
				if(distance > range + overheadDistanceForLeaving && userLocation.getAccuracy() <= userLocationErrorThreshold) {
					loi.userAtLocation = false; // set user is not at location to prevent triggering again
					for(OALocationEventListener listener: listeners)
						listener.onLocationEvent( new OALocationEvent(OALocationEvent.EVENT_LEFT_FROM, loi.id, loi.scene) );
				} else if(range+overheadDistanceForLeaving <= userLocation.getAccuracy() && distance > userLocation.getAccuracy()) {
					loi.userAtLocation = false; // set user is not at location to prevent triggering again
					for(OALocationEventListener listener: listeners)
						listener.onLocationEvent( new OALocationEvent(OALocationEvent.EVENT_LEFT_FROM, loi.id, loi.scene) );
				}
			} else { // user was not at location before, check if user arrived
				// if close enough trigger arrived at event
				if(distance < range && userLocation.getAccuracy() <= userLocationErrorThreshold) {
					loi.userAtLocation = true; // set user is at location to prevent triggering again
					for(OALocationEventListener listener: listeners)
						listener.onLocationEvent( new OALocationEvent(OALocationEvent.EVENT_ARRIVED_AT, loi.id, loi.scene) );
				}
			} // end of if(loi.userAtLocation)
		} // end of for(LocationOfInterest loi: lois)
	}
	
	
}
