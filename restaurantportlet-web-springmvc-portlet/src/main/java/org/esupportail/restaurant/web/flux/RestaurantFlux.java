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
import java.sql.SQLException;

import net.sf.ehcache.Element;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.json.RestaurantFeedRoot;

public class RestaurantFlux implements Serializable {
	
	private String jsonStringified;
	private RestaurantFeedRoot flux;
	private ObjectMapper mapper;
	private URL path;
	
	public RestaurantFlux() {
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
		} catch (SQLException e) {
			System.out.println("[INFO] No URLFLUX available, the portlet needs to be configured by an admin");
		}
		
	}
	private RestaurantFeedRoot mapJson() {
		RestaurantFeedRoot br = null;
		try {
			br = mapper.readValue(this.jsonStringified, RestaurantFeedRoot.class);
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
	
	public void cacheJsonString() {
		RestaurantCache rc = RestaurantCache.getInstance();
		rc.setCacheElement(new Element(CacheModelConst.RESTAURANT_FLUX, this.jsonStringified));
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
		
		if(this.path == null) 
			return false;
		
		RestaurantFlux newRf = new RestaurantFlux();
		newRf.setPath(this.path);
	
		if(this.equals(newRf)) 
			return false;
		
		this.setPath(this.path);
		return true;
	}
	
	public boolean equals(Object o) {
		boolean retour = false;
		if(o instanceof RestaurantFlux) {
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
