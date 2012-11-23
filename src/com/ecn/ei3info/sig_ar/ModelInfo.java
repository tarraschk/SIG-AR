package com.ecn.ei3info.sig_ar;
/**
 * Class ModelInfo for receive information from database PGSQL. ONly use for format data to UI
 */
public class ModelInfo {

	protected int id;
	protected String Name;
	protected String Description;
	protected String Category;
	protected double Latitude;
	protected double Longitude;
	protected double Altitude;
	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param category
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 */
	public ModelInfo(int id, String name, String description, String category,
			double latitude, double longitude, double altitude) {
		super();
		this.id = id;
		Name = name;
		Description = description;
		Category = category;
		Latitude = latitude;
		Longitude = longitude;
		Altitude = altitude;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return Category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		Category = category;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return Latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return Longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	/**
	 * @return the altitude
	 */
	public double getAltitude() {
		return Altitude;
	}
	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(double altitude) {
		Altitude = altitude;
	}
	
	
}
