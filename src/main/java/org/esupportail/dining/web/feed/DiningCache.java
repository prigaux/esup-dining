/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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