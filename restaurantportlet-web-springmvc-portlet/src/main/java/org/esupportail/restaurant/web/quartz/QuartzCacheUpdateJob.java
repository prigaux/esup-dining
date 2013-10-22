package org.esupportail.restaurant.web.quartz;

import org.esupportail.restaurant.web.flux.RestaurantCache;

public class QuartzCacheUpdateJob {

	private RestaurantCache rc;

	public void execute(){
		System.out.println("In quartz job, I call the business manager") ;
		rc.update();
	}
 
	public void setRestaurantCache(RestaurantCache restaurantCache) {
		this.rc = RestaurantCache.getInstance();
	}
	
}
