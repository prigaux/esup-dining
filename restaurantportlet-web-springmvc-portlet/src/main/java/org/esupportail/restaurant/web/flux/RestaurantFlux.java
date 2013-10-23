package org.esupportail.restaurant.web.flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import net.sf.ehcache.Element;

import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.restaurant.web.model.bindings.Area;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.ehcache.annotations.Cacheable;



public class RestaurantFlux implements Serializable {

	private JSONObject flux;
	private JSONArray restaurantList;
	private JSONArray mealList;
	
	private ArrayList<Area> areaList;
	
	private ObjectMapper mapper;
	
	public RestaurantFlux(URL urlFlux) {
		this.flux = this.getAndCreateJson(urlFlux);		
		try {
			this.restaurantList = flux.getJSONArray("dining-halls");
			this.mealList = flux.getJSONArray("meals");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			this.areaList = this.getAreas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public RestaurantFlux(String url) {
		URL urlFlux=null;
		try {
			urlFlux = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
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
	
	
	public ArrayList<Area> getAreas() {
		ArrayList<Area> listeArea = new ArrayList<Area>();
		
		try {
			JSONArray areas = flux.getJSONArray("areas");
			System.out.println(areas);
			for(int i=0; i<areas.length(); i++) {
				JSONObject area = areas.getJSONObject(i);
				System.out.println(area);
				listeArea.add(mapper.readValue(area.toString(), Area.class));
			}
			
			RestaurantCache rc = RestaurantCache.getInstance();
			Element el = rc.getCacheByKey(CacheModelConst.AREAS);
			ArrayList<Area> la = (ArrayList<Area>) el.getObjectValue();
			System.out.println(la);	
		} catch(Exception e) {
			// lol
		}
		
		return listeArea;
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
