package org.esupportail.restaurant.web.flux;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.domain.beans.User;
import org.esupportail.restaurant.web.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

public class RestaurantData {

	@Autowired
	private RestaurantFeed feed;
	@Autowired 
	RestaurantCache cache;
	@Autowired
	private DatabaseConnector dc;
	
	public RestaurantData() {
		
	}
	
	public List<Restaurant> getFavoriteList(User user) {
		List<Restaurant> favorites = new ArrayList<Restaurant>();
				
		try {
    		ResultSet favList = dc.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='" + user.getLogin() +"';");
    		
    		if(favList.next()) {
	    		for(Restaurant r : feed.getFlux().getRestaurants()) {
	    			
	    			do {
	    				if(r.getId() == favList.getInt("RESTAURANTID"))
	    					favorites.add(r);
	    			} while(favList.next());
	    			
	    			favList.first();
	    		}
    		}
    		
    	} catch(SQLException e) {
    		// Nothing to do here.
    	} catch(NullPointerException e2) {
    		// nop
    	}
		
		return favorites;
	}

	public List<Restaurant> getRestaurantList(String zone) {
		List<Restaurant> dininghallList = new ArrayList<Restaurant>();   	
		try {   		
			for(Restaurant restaurant : feed.getFlux().getRestaurants()) {
				if(restaurant.getArea().equalsIgnoreCase(zone))
					dininghallList.add(restaurant);
			}

		} catch(Exception e) { /**/	}
		return dininghallList;		
	}
	
	public Set<String> getNutritionItems(User user) {
		Set<String> nutritionPrefs = new HashSet<String>();
		
		try {
			
			ResultSet prefUser = dc.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"+ user.getLogin() +"';");
	    		
			while(prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}
			
		} catch(SQLException e) { /**/ }
		
		return nutritionPrefs;
	}
		
}
