package org.esupportail.restaurant.web.flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.model.Restaurant;
import org.esupportail.restaurant.web.model.RestaurantFeedRoot;
import org.springframework.beans.factory.annotation.Autowired;

public class RestaurantFeed implements Serializable {
	
	private String jsonStringified;
	private RestaurantFeedRoot flux;
	private ObjectMapper mapper;
	private URL path;
	
	@Autowired 
	RestaurantCache cache;
	
	public RestaurantFeed() {
		this.jsonStringified = new String();
		this.mapper = new ObjectMapper();
		
		DatabaseConnector dc = DatabaseConnector.getInstance();
		
		try {
			ResultSet results = dc.executeQuery("SELECT URLFLUX FROM PATHFLUX");
			results.next(); // Move the cursor to the first line.
			// results.getUrl... throw an exception : This function is not supported
			// When we'll update to postgre or something, try this out : 
			// URL urlFlux = results.getURL("URLFLUX");
			// Should be better.
			URL urlFlux=null;
			try {
				urlFlux = new URL(results.getString("URLFLUX"));
			} catch (MalformedURLException e) {
				// Nothing to do because normally, we check the URL validity
				// before the inserting this row.
			}
			this.setPath(urlFlux);
		} catch (Exception e) {
			System.out.println("[WARN] Couldn't fully instantiate RestaurantFlux, you'll need to configure it manually from admin settings.");
		}
	}
	private RestaurantFeedRoot mapJson() throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(this.jsonStringified, RestaurantFeedRoot.class);
	}

	public URL getPath() {
		return this.path;
	}
	
	public void setPath(URL path) throws JsonParseException, JsonMappingException, IOException {
		this.path = path;
		// If the user sets a new path, we have to update json string and re-map the all json file.
		this.updateJson();
	}
	
	public String getJsonStringified() {
		return this.jsonStringified;
	}
	
	public void updateJson() throws JsonParseException, JsonMappingException, IOException {
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
		return jsonText;
	}
	
	public RestaurantFeedRoot getFlux() {
		return this.flux;
	}
	public void setFlux(RestaurantFeedRoot flux) {
		this.flux = flux;
	}
	
	public boolean update() {
		
		if(this.path == null) return false;
		
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

		String tempJson = this.getJsonContent(br);
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(!this.jsonStringified.equals(tempJson)) {
			this.jsonStringified = tempJson;
			try {
				this.flux = this.mapJson();
				cache.cacheReset(RestaurantCache.RESTAURANT_CACHE_NAME);
				cache.cacheRestaurant();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		} else {
			return false;
		}
	}
	
	public Restaurant getRestaurantById(int id) {
		for(Restaurant r : flux.getRestaurants()) {
			if(r.getId() == id)
				return r;
		}
		return null;
	}
	
	public boolean equals(Object o) {
		boolean retour = false;
		if(o instanceof RestaurantFeed) {
			RestaurantFeed rf = (RestaurantFeed) o;
			if(rf.getJsonStringified().equals(this.jsonStringified))
				retour = true;
		}
		return retour;
	}
	
	public String toString() {
		return this.jsonStringified;
	}	
	
	/* automated task */
	
	public void run() {
		System.out.println("Update : " + this.update());
	}
}
