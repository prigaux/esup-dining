package org.esupportail.restaurant.web.flux;

import org.json.JSONException;
import org.json.JSONObject;

public class Restaurant {

	private int id;
	
	private String area,
				   title,
				   opening,
				   closing,
				   shortDesc,
				   infos,
				   contact;
	
	private double lat,
				   lon;
	
	public Restaurant(JSONObject restaurant) {
		
		try {
			// These are not optionnal
			this.id = restaurant.getInt("id");
			this.area = restaurant.getString("area");
			this.title = restaurant.getString("title");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Optionnal entries
		
		try {
			this.opening = restaurant.getString("opening");
		} catch (JSONException e) {
			this.opening = "";
		}

		try {
			this.closing = restaurant.getString("closing");
		} catch (JSONException e) {
			this.closing = "";
		}

		try {
			this.shortDesc = restaurant.getString("shortDesc");
		} catch (JSONException e) {
			this.shortDesc = "";
		}

		try {
			this.lat = restaurant.getDouble("lat");
		} catch (JSONException e) {
			this.lat = 0;
		}

		try {
			this.lon = restaurant.getDouble("lon");
		} catch (JSONException e) {
			this.lon = 0;
		}

		try {
			this.infos = restaurant.getString("infos");
		} catch (JSONException e) {
			this.infos = "";
		}
		
		try {
			this.contact = restaurant.getString("contact");
		} catch (JSONException e) {
			this.contact = "";
		}
		
	}

	public int getId() {
		return id;
	}

	public String getArea() {
		return area;
	}

	public String getTitle() {
		return title;
	}

	public String getOpening() {
		return opening;
	}

	public String getClosing() {
		return closing;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getInfos() {
		return infos;
	}

	public String getContact() {
		return contact;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}	
}
