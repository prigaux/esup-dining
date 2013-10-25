package org.esupportail.restaurant.web.springmvc;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esupportail.restaurant.domain.beans.User;
import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.flux.RestaurantFlux;
import org.esupportail.restaurant.web.json.Restaurant;
import org.esupportail.restaurant.web.json.RestaurantFeedRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
//import org.esupportail.restaurant.web.dao.POJOGenerator;

@Controller
public class WebController extends AbstractExceptionController {

	
    @Autowired
	private Authenticator authenticator;
	//private RestaurantCache restaurantCache = RestaurantCache.getInstance();   
	
	@Autowired
	private RestaurantFlux flux;
	
	private RestaurantFeedRoot restaurants;
    
    @RequestMapping("VIEW")
    public ModelAndView renderMainView(RenderRequest request, RenderResponse response) throws Exception {	
    	
    	ModelMap model = new ModelMap();
    	
    	PortletSession sess = request.getPortletSession();
    	sess.setMaxInactiveInterval(-1);
    	
    	PortletPreferences prefs = request.getPreferences();
    	
    	String areaToDisplay = (String) sess.getAttribute("userArea");
    	if(areaToDisplay == null || areaToDisplay.length() == 0)
    		areaToDisplay = prefs.getValue("defaultArea", "");
    	
    	model.put("area", areaToDisplay);
    	
    	String[] favList = (String[]) sess.getAttribute("favorite");
		if(favList!=null && favList.length > 0) 
			model.put("favList", favList);
		
    	try {
    		
    		restaurants = flux.getFlux();
    		
        
        	List<Restaurant> dininghallList = new ArrayList<Restaurant>();   	
        	
    		for(Restaurant restaurant : restaurants.getRestaurants()) {
    			if(restaurant.getArea().equalsIgnoreCase(areaToDisplay))
    				dininghallList.add(restaurant);
    		}
    		model.put("dininghalls", dininghallList);

    	} catch(NullPointerException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}
    	return new ModelAndView("view", model);
    }
    
    
    
    @RequestMapping(value = {"VIEW"}, params = {"action=viewRestaurant"})
    public ModelAndView renderRestaurantView(RenderRequest request, RenderRequest reponse, @RequestParam(value = "id", required = true) int id) throws Exception {
    	
    	ModelMap model = new ModelMap();
    	    	    	
    	try {
    		restaurants = flux.getFlux();
    		for(Restaurant r : restaurants.getRestaurants()) {
    			if(r.getId() == id) {
    				model.put("restaurant", r);
    				
    				PortletSession sess = request.getPortletSession();
    		    	
    				String[] favList = (String[]) sess.getAttribute("favorite");
    				if(favList!=null && favList.length > 0) {
    					for(int i=0; i<favList.length; i++) {
    						// Check if the current Restaurant is in the favorite list or not
    						// (to display add or remove link)
    						if(Integer.parseInt(favList[i], 10) == id)
    							model.put("isFavorite", true);
    					}
    				}
    			}
    		}
    		
    	} catch(NullPointerException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}

    	return new ModelAndView("restaurant", model);
    }
    
    @RequestMapping(value = {"VIEW"}, params = {"action=setFavorite"})
    public void setFavorite(ActionRequest request, ActionResponse response, @RequestParam(value = "id", required = true) String id) throws Exception {

    	PortletSession sess = request.getPortletSession();
    	String[] favoriteList = (String[]) sess.getAttribute("favorite");
    	
    	if(favoriteList != null && favoriteList.length > 0) {
    		String[] newFavoriteList = new String[favoriteList.length + 1];
    		
    		// Check if the array already contains the ID we try to add
    		boolean alreadyFavorite = false;
    		for(int i=0; i<favoriteList.length; i++) { 
    			if(favoriteList[i].equalsIgnoreCase(id))
    				alreadyFavorite = true;
    		}
    		
    		// if it is not in, then we can update the array and set it to the portlet preferences
    		if(!alreadyFavorite) {
        		newFavoriteList[0] = id;
        		for(int i=1; i<=favoriteList.length; i++) {
        			newFavoriteList[i] = favoriteList[i-1];
        		}    	
        		sess.setAttribute("favorite", newFavoriteList);	
    		}
    		
    	} else {
    		sess.setAttribute("favorite", new String[]{id});
    	}
    	
    }
    
    
    @RequestMapping("EDIT")
    public ModelAndView renderEditView(RenderRequest request, RenderResponse response) throws Exception {
        
    	ModelMap model = new ModelMap();
    	
    	User user = authenticator.getUser();
    	model.put("user", user);
    	
    	try {
        	//RestaurantFlux flux = restaurantCache.getCachedElement();
        	
        	//model.put("areas", flux.getAreas());
        	
        	PortletPreferences prefs = request.getPreferences();
        	PortletSession sess = request.getPortletSession();
        	String userArea = (String) sess.getAttribute("userArea");
        	
        	if(userArea != null && userArea.length() != 0) {
        		model.put("defaultArea", userArea);
        	} else {
        		model.put("defaultArea", prefs.getValue("userArea", null));
        	}
        	
        	String[] favList = (String[]) sess.getAttribute("favorite");
        	if(favList != null && favList.length > 0)
        		model.put("favList", favList);	
        	
    	} catch(NullPointerException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}
    	
    	return new ModelAndView("edit", model);
    }    
    
    @RequestMapping(value = {"EDIT"}, params = {"action=setUserArea"})
    public void setUserArea(ActionRequest request, ActionResponse response, @RequestParam(value = "zone", required = true) String area) throws Exception {
    	PortletSession sess = request.getPortletSession();
    	sess.setAttribute("userArea", area);    	
    }
    
    @RequestMapping(value = {"EDIT", "VIEW"}, params = {"action=removeFavorite"})
    public void removeFavorite(ActionRequest request, ActionResponse response, @RequestParam(value = "restaurant-id", required = true) String id) throws Exception {
    	PortletSession sess = request.getPortletSession();
    	String[] favoriteList = (String[]) sess.getAttribute("favorite");
    	// Cast String[] to ArrayList<String>
    	List<String> newFavoriteList = new ArrayList<String>(Arrays.asList(favoriteList));
    	newFavoriteList.remove(id);
    	// Cast back the ArrayList<String> to String[] in order to store it
    	sess.setAttribute("favorite", (String[]) newFavoriteList.toArray(new String[newFavoriteList.size()]));
    }
    
    @RequestMapping(value = {"EDIT"}, params = {"action=adminSettings"})
    public ModelAndView renderEditAdminView(RenderRequest request, RenderResponse response) throws Exception {
    	
    	
    	ModelMap model = new ModelMap();
    	
    	User user = authenticator.getUser();
    	model.put("user", user);
    	
    	
    	try {
        	
    		Set<String> areaList = new HashSet<String>();
    		for(Restaurant r : flux.getFlux().getRestaurants()) {
    			areaList.add(r.getArea());
    		}
    		model.put("areas", areaList);
        	
        	PortletPreferences prefs = request.getPreferences();
        	String area = prefs.getValue("defaultArea", null);
        	model.put("defaultArea", area);
        	
    	} catch(NullPointerException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}
    	
    	String hasError = request.getParameter("urlError");
    	if(hasError != null)
    		model.put("urlError", "Incorrect URL");
    	
 //   	if(restaurantCache.getUrl()!=null)
 //   		model.put("urlFluxCache", restaurantCache.getUrl());
    	
    	return new ModelAndView("edit-admin", model);
    }
    
    @RequestMapping(value = {"EDIT"}, params = {"action=setDefaultArea"})
    public void setDefaultArea(ActionRequest request, ActionResponse response, @RequestParam(value = "zone", required = true) String area) throws Exception {

		response.setRenderParameter("action", "adminSettings");
		
    	PortletPreferences prefs = request.getPreferences();
    	prefs.setValue("defaultArea", area);
    	prefs.store();
    }
    
    @RequestMapping(value = {"EDIT"}, params = {"action=urlFlux"})
    public void setURLFlux(ActionRequest request, ActionResponse response, @RequestParam(value = "url", required = true) String url) throws Exception { 
  
		response.setRenderParameter("action", "adminSettings");
    	try {
    		URL urlFlux = new URL(url);	
    		flux.setPath(urlFlux);
    	} catch(MalformedURLException e) {
    		response.setRenderParameter("urlError", "true");
    	}
    }
    
    @RequestMapping("ABOUT")
	public ModelAndView renderAboutView(RenderRequest request, RenderResponse response) throws Exception {
		ModelMap model = new ModelMap();
		return new ModelAndView("about", model);
	}
    
    @RequestMapping("HELP")
    public ModelAndView renderHelpView(RenderRequest request, RenderResponse response) throws Exception {
    	ModelMap model = new ModelMap();
    	return new ModelAndView("help", model);
    }
}