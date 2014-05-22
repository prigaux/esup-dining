package org.esupportail.dining.web.feed;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.esupportail.dining.web.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

public class RestaurantCache {

	public static final String RESTAURANT_CACHE_NAME = "restaurant_cache";

	@Autowired
	private CacheManager cacheManager;
	private Cache restaurantCache;

	@Autowired
	private RestaurantFeed flux;

	public RestaurantCache(RestaurantFeed feed) {
		this.cacheManager = new CacheManager();
		this.flux = feed;
		this.cacheManager.addCache(RESTAURANT_CACHE_NAME);
		this.restaurantCache = this.cacheManager
				.getCache(RESTAURANT_CACHE_NAME);
	}

	public RestaurantFeed getFeed() {
		return this.flux;
	}

	public void cacheRestaurant() {

		List<Restaurant> restaurantList = flux.getFlux().getRestaurants();

		for (Restaurant restaurant : restaurantList) {

			int restaurantKey = restaurant.getId();

			restaurantCache.put(new Element(restaurantKey, restaurant));

		}
	}

	public Restaurant getCachedRestaurant(int key) {

		Restaurant restaurant;

		Element el = restaurantCache.get(key);
		if (el != null) {
			restaurant = (Restaurant) el.getObjectValue();
		} else {

			restaurant = flux.getRestaurantById(key);

			if (restaurant != null) {
				restaurantCache.put(new Element(key, restaurant));
			}

		}
		return restaurant;
	}

	public void cacheReset(String cacheName) {
		cacheManager.getCache(cacheName).removeAll();
	}

}