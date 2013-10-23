package org.esupportail.restaurant.web.quartz;

import org.esupportail.restaurant.web.flux.RestaurantCache;

public class QuartzCacheUpdateJob {

	private RestaurantCache rc;

	public void execute(){
		rc.update();
	}
 
	public void setRestaurantCache(RestaurantCache restaurantCache) {
		this.rc = RestaurantCache.getInstance();
	}
	
}
