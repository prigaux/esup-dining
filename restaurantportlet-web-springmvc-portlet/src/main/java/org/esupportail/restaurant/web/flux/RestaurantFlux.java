package org.esupportail.restaurant.web.flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.restaurant.web.model.bindings.BindingsRestaurant;

public class RestaurantFlux implements Serializable {
	
	private String jsonStringified;
	private BindingsRestaurant flux;
	private ObjectMapper mapper;
	private URL path;
	
	public RestaurantFlux() {
		this.jsonStringified = new String();
		this.mapper = new ObjectMapper();
	}
	private BindingsRestaurant mapJson() {
		BindingsRestaurant br = null;
		try {
			br = mapper.readValue(this.jsonStringified, BindingsRestaurant.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return br;
	}

	public URL getPath() {
		return this.path;
	}
	
	public void setPath(URL path) {
		this.path = path;
		
		// If the user sets a new path, we have to update json string and re-map the all json file.
		this.updateJson();
	}
	
	public String getJsonStringified() {
		return this.jsonStringified;
	}
	
	public void updateJson() {
		URLConnection yc = null;
		try {
			yc = this.path.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// get a stringified version of the flux to help equality comparison
		this.jsonStringified = this.getJsonContent(br);		
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Init flux by mapping JSON to POJO
		this.flux = this.mapJson();
	}
	
	
	private String getJsonContent(BufferedReader in) {
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
		System.out.println(jsonText);
		return jsonText;
	}
	
	
	public BindingsRestaurant getFlux() {
		return this.flux;
	}
	public void setFlux(BindingsRestaurant flux) {
		this.flux = flux;
	}
	
	public boolean equals(Object o) {
		boolean retour = false;
		if(o instanceof RestaurantFlux) {
			System.out.println("instance");
			RestaurantFlux rf = (RestaurantFlux) o;
			if(rf.getJsonStringified().equals(this.jsonStringified))
				retour = true;
		}
		return retour;
	}
	
	public String toString() {
		return this.jsonStringified;
	}
	
}
