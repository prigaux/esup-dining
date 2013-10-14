package org.esupportail.restaurant.web.springmvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.exceptions.EsupException;
import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.flux.Restaurant;
import org.esupportail.restaurant.web.flux.RestaurantFlux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController extends AbastractExceptionController {
	
	@Autowired
	private Authenticator authenticator;
	@Autowired
	private static RestaurantFlux flux = new RestaurantFlux("https://dl.dropboxusercontent.com/u/14925931/flux.json");
	
    @RequestMapping("/")
    public ModelAndView renderView(HttpServletRequest request, HttpServletResponse response) throws Exception {    			    	
    		
    	// Ici est la zone qui est définis par défaut par l'administrateur
    	String areaToDisplay = "LA ROCHELLE";
    	
    	// On check si l'utilisateur à définit une autre zone que l'admin
    
        Cookie[] cookieList = request.getCookies();
        
      	String[] favoriteIDs= new String[0];
    	List<Restaurant> favoriteRestaurant = new ArrayList<Restaurant>();
    	        
    	for(int i=0; i<cookieList.length; i++) {
    		Cookie c = cookieList[i];
    		if(c.getName().equalsIgnoreCase("userArea"))
    			areaToDisplay = c.getValue();
    		if(c.getName().equalsIgnoreCase("fav"))
    			favoriteIDs = c.getValue().split("-");
    	}
    	
    	
    	for(int i=0; i<favoriteIDs.length; i++) {
    		try {
        		int id = Integer.parseInt(favoriteIDs[i], 10);
        		favoriteRestaurant.add(flux.getRestaurantById(id));
        	} catch (NumberFormatException e) {
        		// Happens if you delete the last restaurant
        	}
    	}

    	ModelMap model = new ModelMap();   	
    	
    	
    	if(favoriteRestaurant.size() > 0)
    		model.put("favList", favoriteRestaurant);        	
    	model.put("area", areaToDisplay);
    	model.put("restaurantList", flux.getRestaurantList(areaToDisplay));
    	
    	return new ModelAndView("view", model);
	}
    
    @RequestMapping("/about")
    public ModelAndView renderAbout() throws Exception {
        return new ModelAndView("about", null);
    }
    
    @RequestMapping("/exception")
    public ModelAndView renderException() throws Exception {
       throw new EsupException("exception.genere") {};
    }

    @RequestMapping("/restaurant")
    public ModelAndView renderRestaurant(@RequestParam(value = "id") int id) throws Exception {
    	ModelMap model = new ModelMap();
    	model.put("restaurant", flux.getRestaurantById(id));
    	model.put("meals", flux.getMealByRestaurant(id));
    	return new ModelAndView("restaurant", model);
    }

    @RequestMapping(value="/restaurant", method=RequestMethod.GET, params={"id", "addToFavorite"})
    public ModelAndView addToFavorite(HttpServletRequest request, 
    								  HttpServletResponse response, 
    								  @RequestParam(value = "id") int id) throws Exception {
    	
    	Cookie[] cookies = request.getCookies();
    	boolean cookieExist = false;
    	for(int i=0; i<cookies.length; i++) {
    		Cookie c = cookies[i];
    		if(c.getName().equalsIgnoreCase("fav")) {
    			cookieExist = true;
    			if(! c.getValue().contains(id+"-")) {
    				c.setValue(c.getValue() + id + "-"); 
    				response.addCookie(c);
    			}
    		}
    	}
    	if(!cookieExist) {
    		Cookie favCookie = new Cookie("fav", id+"-");
    		response.addCookie(favCookie);
    	}
    	
    	return this.renderRestaurant(id);
    }

    
}
