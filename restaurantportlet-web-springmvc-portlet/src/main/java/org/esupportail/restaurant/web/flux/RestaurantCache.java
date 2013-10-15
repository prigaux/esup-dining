package org.esupportail.restaurant.web.flux;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

// # WARNING !!!!!!!!!!!!!!!!!!!!!!!!!!!
// Not finished. 
// updateCache() not working
// #todo : refactor code, rename variable, make update work

public class RestaurantCache {
	
	private static RestaurantCache instance;
	private URL url;
	private Cache fluxCache;
	private CacheManager singletonManager;
	
	private RestaurantCache() {		
		singletonManager = CacheManager.create();
		singletonManager.addCache("restaurantFlux");
		fluxCache = singletonManager.getCache("restaurantFlux");
	}
	
	public static RestaurantCache getInstance() {
		if(instance == null)
			instance = new RestaurantCache();
		return instance;
	}
	
	public void setUrl(URL url) {
		this.url = url;
	}
	
	public URL getURL() {
		return this.url;
	}
	
	public Cache getCache() {
		return this.fluxCache;
	}
	
	public void updateCache() throws Exception {
		
		Element el = fluxCache.get("restaurantFlux");
		RestaurantFlux fluxFromCache = (RestaurantFlux) el.getValue();
		RestaurantFlux fluxFromUrl = new RestaurantFlux(new URL("http://www.souquet.eu/test/flux2.json"));
		
		if(!fluxFromCache.equals(fluxFromUrl)) {
			Element newEl = new Element("restaurantFlux", fluxFromUrl.getFlux());
			this.fluxCache.put(newEl);
		}
		
	}
}