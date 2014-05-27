package org.esupportail.dining.web.feed;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.esupportail.dining.web.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

public class DiningCache {

	public static final String DINING_CACHE_NAME = "restaurant_cache";

	@Autowired
	private final CacheManager cacheManager;
	private final Cache diningCache;

	@Autowired
	private final DiningFeed feed;

	public DiningCache(DiningFeed feed) {
		this.cacheManager = new CacheManager();
		this.feed = feed;
		this.cacheManager.addCache(DINING_CACHE_NAME);
		this.diningCache = this.cacheManager
				.getCache(DINING_CACHE_NAME);
	}

	public DiningFeed getFeed() {
		return this.feed;
	}

	public void cacheDining() {

		List<Restaurant> restaurantList = this.feed.getFeed().getRestaurants();

		for (Restaurant restaurant : restaurantList) {

			int restaurantKey = restaurant.getId();

			this.diningCache.put(new Element(restaurantKey, restaurant));

		}
	}

	public Restaurant getCachedDining(int key) {

		Restaurant restaurant;

		Element el = this.diningCache.get(key);
		if (el != null) {
			restaurant = (Restaurant) el.getObjectValue();
		} else {

			restaurant = this.feed.cache.getDiningById(this.feed, key);

			if (restaurant != null) {
				this.diningCache.put(new Element(key, restaurant));
			}

		}
		return restaurant;
	}

	public void cacheReset(String cacheName) {
		this.cacheManager.getCache(cacheName).removeAll();
	}

	public Restaurant getDiningById(DiningFeed diningFeed, int id) {
		for (Restaurant r : diningFeed.getFeed().getRestaurants()) {
			if (r.getId() == id) {
				return r;
			}
		}
		return null;
	}

}