package org.esupportail.restaurant.web.quartz;

import java.net.MalformedURLException;
import java.net.URL;

import org.esupportail.restaurant.web.flux.*;

public class QuartzCacheUpdateJob {

	private RestaurantFlux restaurantFlux;
	private RestaurantCache rc;

	public void execute(){
		
		String jsonStringifiedCache = (String) rc.getCacheByKey(CacheModelConst.RESTAURANT_FLUX).getObjectValue();
		
		try {
			restaurantFlux.setPath(new URL("http://www.souquet.eu/test/v2/flux.json"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(restaurantFlux.getJsonStringified().equalsIgnoreCase(jsonStringifiedCache)) {
			System.out.println("Flux égale, on ne fait rien");
		} else {
			System.out.println("Flux différent, on update !");
		}
		
	}
 
	public void setRestaurantFlux(RestaurantFlux restaurantFlux) {
		this.restaurantFlux = new RestaurantFlux();
	}
	
}
