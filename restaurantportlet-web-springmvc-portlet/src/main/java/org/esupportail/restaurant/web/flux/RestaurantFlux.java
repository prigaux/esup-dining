package org.esupportail.restaurant.web.flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//#TODO CLEAN THAT CODE ! 
//#TODO CLEAN THAT CODE ! 
//#TODO CLEAN THAT CODE ! 
//#TODO CLEAN THAT CODE ! 
//#TODO CLEAN THAT CODE ! 
//#TODO CLEAN THAT CODE ! 

// #TODO : JAXB ?! Regarder la génération de classe automatique dans la CampusLifePortlet et les possibilités

public class RestaurantFlux implements Serializable {

	private JSONObject flux;
	private JSONArray restaurantList;
	private JSONArray mealList;
	
	public RestaurantFlux(URL urlFlux) {
		this.flux = this.getAndCreateJson(urlFlux);		
		try {
			this.restaurantList = flux.getJSONArray("dining-halls");
			this.mealList = flux.getJSONArray("meals");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getFlux() {
		return this.flux;
	}
	
	public JSONObject getAndCreateJson(URL path) {
		

		URL fluxURL=path;
		URLConnection yc=null;
		try {
			yc = fluxURL.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		BufferedReader in=null;
		try {
			in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
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
			JSONObject restaurant = restaurantList.getJSONObject(i);
			if(area.equals(restaurant.get("area")))
				list.add(new Restaurant(restaurant));
		}
		return list;
	}
	
	public Restaurant getRestaurantById(int id) throws JSONException {
		for(int i=0; i<restaurantList.length(); i++) {
			JSONObject restaurant = restaurantList.getJSONObject(i);
			if(restaurant.getInt("id") == id)
				return new Restaurant(restaurant);
		}
		return null;
	}
	
	public List<Meal> getMealByRestaurant(int id) throws JSONException {
		
		List<Meal> meals = new ArrayList<Meal>();
		for(int i=0; i<mealList.length(); i++) {
			JSONObject meal = mealList.getJSONObject(i);
			if(meal.getInt("dining-hall") == id)
				meals.add(new Meal(meal));
		}
		return meals;
	}
	
	public boolean equals(Object o) {
		boolean retour = false;
		if(o instanceof RestaurantFlux) {
			System.out.println("instance");
			RestaurantFlux rf = (RestaurantFlux) o;
			if(rf.getFlux().toString().equals(this.flux.toString()))
				retour = true;
		}
		return retour;
	}
}
