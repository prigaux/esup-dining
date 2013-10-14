package org.esupportail.restaurant.web.flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantFlux {

	private JSONObject flux;
	private JSONArray restaurantList;
	
	public RestaurantFlux(String urlFlux) {
		this.flux = this.getAndCreateJson(urlFlux);		
		try {
			this.restaurantList = (JSONArray)flux.get("dining-halls");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getFlux() {
		return this.flux;
	}
	
	public JSONObject getAndCreateJson(String path) {
		

		URL fluxURL=null;
		try {
			fluxURL = new URL(path);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		URLConnection yc=null;
		try {
			yc = fluxURL.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		BufferedReader in=null;
		try {
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String jsonText = new String();
		while(true) {
			String str = null;
			try {
				str = in.readLine();
				if(str==null) break;
				jsonText += str;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JSONObject jsonObj=null;
		try {
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jsonObj = new JSONObject(jsonText);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return jsonObj;
	}
	
	public List<String> getAreas() {
		List<String> areas = new ArrayList<String>();
		try {
			JSONArray tab = (JSONArray)flux.get("areas");
			for(int i=0; i<tab.length(); i++) {
				JSONObject obj = (JSONObject)tab.get(i);
				areas.add((String)obj.get("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return areas;
	}
	
	public List<Restaurant> getRestaurantList(String area) throws JSONException {
		List<Restaurant> list = new ArrayList<Restaurant>();
		for(int i=0; i<restaurantList.length(); i++) {
			JSONObject restaurant = (JSONObject)restaurantList.get(i);
			if(area.equals(restaurant.get("area")))
				list.add(new Restaurant(restaurant));
		}
		return list;
	}
	
	public Restaurant getRestaurantById(int id) throws JSONException {
		for(int i=0; i<restaurantList.length(); i++) {
			JSONObject restaurant = (JSONObject)restaurantList.get(i);
			if(restaurant.getInt("id") == id)
				return new Restaurant(restaurant);
		}
		return null;
	}
		
}
