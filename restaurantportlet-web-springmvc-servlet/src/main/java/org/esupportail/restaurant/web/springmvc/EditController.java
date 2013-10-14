package org.esupportail.restaurant.web.springmvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.restaurant.domain.beans.User;
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
@RequestMapping("/edit")
public class EditController {
	
	@Autowired
	private Authenticator authenticator;
	@Autowired
	private static RestaurantFlux flux = new RestaurantFlux("https://dl.dropboxusercontent.com/u/14925931/flux.json");
	
	
    @RequestMapping
    public ModelAndView renderEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ModelMap model = new ModelMap();
        
    	String[] favoriteIDs= new String[0];
    	List<Restaurant> favoriteRestaurant = new ArrayList<Restaurant>();
    	
    	Cookie[] cookieList = request.getCookies();
    	for(int i=0; i<cookieList.length; i++) {
    		Cookie c = cookieList[i];
    		if(c.getName().equalsIgnoreCase("userArea"))
    			model.put("userArea", c.getValue());
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
    	
    	if(favoriteRestaurant.size() > 0)
    		model.put("favList", favoriteRestaurant);
    	
    	User user = authenticator.getUser();
    	model.put("user", user);
    	model.put("areas", flux.getAreas());
    	return new ModelAndView("edit", model);
    }
    
    @RequestMapping(method=RequestMethod.POST , params="zone")
    public ModelAndView userPreferredZone(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "zone") String zone) throws Exception {
    	Cookie userAreaCookie = new Cookie("userArea", zone);
    	response.addCookie(userAreaCookie);
    	ModelMap model = new ModelMap();
    	model.put("zone", zone);
    	return new ModelAndView("edit-submit", model);
    }
	
    @RequestMapping(method=RequestMethod.POST, params={"id", "removeFromFavorite"})
    public ModelAndView removeFromFavorite(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") String id) throws Exception {
    	
    	Cookie[] cookies = request.getCookies();
    	Cookie favCookie = null;
    	for(int i=0; i<cookies.length; i++) {
    		Cookie c = cookies[i];
    		if(c.getName().equalsIgnoreCase("fav"))
    			favCookie = c;
    	}
    	// On remplace l'id par une chaine de caractère vide dans le cookie
    	favCookie.setValue(favCookie.getValue().replace(id+"-", ""));  
    	response.addCookie(favCookie);
    
    	return this.renderEdit(request, response);
    }
    
    @RequestMapping("/admin")
    public ModelAndView renderAdminEdit() throws Exception {
    	User user = authenticator.getUser();
    	ModelMap model = new ModelMap();
    	model.put("user", user);
    	return new ModelAndView("edit-admin", model);  
    }
    
    
    @RequestMapping(value="/admin", method=RequestMethod.POST, params="url-flux")
    public ModelAndView addFlux(@RequestParam(value="url-flux") String url) throws Exception {
    	// #TODO
    	return this.renderAdminEdit();
    }
    
}
