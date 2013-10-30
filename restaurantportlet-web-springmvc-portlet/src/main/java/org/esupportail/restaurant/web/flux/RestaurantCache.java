package org.esupportail.restaurant.web.flux;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.context.annotation.Scope;

public class RestaurantCache {
	
	/*
	 * #TODO
	 * Not implemented yet, need to work on this later
	 */
	
	private static RestaurantCache instance;
	private URL url;
	private Cache fluxCache;
	private CacheManager cacheManager;
	
	public RestaurantCache() {
		cacheManager = CacheManager.getInstance();
		cacheManager.addCache("restaurantFlux");
		fluxCache = cacheManager.getCache("restaurantFlux");
		// Spring create the object using the constructor
		// So when we use getInstance for the first this, the variable instance == null
		// so it creates a second object RestaurantCache and throw and expcetion because the cache already exist.
		instance = this;
	}
	
	public static RestaurantCache getInstance() {
		if(instance == null)
			instance = new RestaurantCache();
		return instance;
	}
	
	public void setCacheElement(Element el) {
		this.fluxCache.put(el);
	}
	
	public RestaurantFlux getCachedElement() {
		if( this.fluxCache.get("restaurantFlux").getObjectValue() != null)
			return (RestaurantFlux) this.fluxCache.get("restaurantFlux").getObjectValue();
		else
			return null;
	}
	
	public Element getCacheByKey(String key) {
		return fluxCache.get(key);
	}
}