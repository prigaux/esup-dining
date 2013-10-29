package org.esupportail.restaurant.web.springmvc;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.flux.RestaurantFlux;
import org.esupportail.restaurant.web.json.Manus;
import org.esupportail.restaurant.web.json.Restaurant;
import org.esupportail.restaurant.web.json.RestaurantFeedRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
public class WebController extends AbstractExceptionController {

	
    @Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private RestaurantFlux flux;
	private RestaurantFeedRoot restaurants;
    
    @RequestMapping("VIEW")
    public ModelAndView renderMainView(RenderRequest request, RenderResponse response) throws Exception {	
    	
    	ModelMap model = new ModelMap();
    	
    	/*
    	String areaToDisplay = (String) sess.getAttribute("userArea");
    	if(areaToDisplay == null || areaToDisplay.length() == 0)
    		areaToDisplay = prefs.getValue("defaultArea", "");
    	
    	model.put("area", areaToDisplay);
    	
    	String[] favList = (String[]) sess.getAttribute("favorite");
		if(favList!=null && favList.length > 0) 
			model.put("favList", favList);
		*/ 
    	try {
    		
    		ResultSet results = dc.executeQuery("SELECT AREANAME FROM PATHFLUX");
        	results.next();
        	String areaToDisplay = results.getString("AREANAME");
        	
    		restaurants = flux.getFlux();
    		
        	List<Restaurant> dininghallList = new ArrayList<Restaurant>();   	
        	
    		for(Restaurant restaurant : restaurants.getRestaurants()) {
    			if(restaurant.getArea().equalsIgnoreCase(areaToDisplay))
    				dininghallList.add(restaurant);
    		}
 			model.put("dininghalls", dininghallList);
   
    	} catch(SQLException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}
    	return new ModelAndView("view", model);
    }
    
    
    
    @RequestMapping(value = {"VIEW"}, params = {"action=viewRestaurant"})
    public ModelAndView renderRestaurantView(RenderRequest request, RenderResponse response, @RequestParam(value = "id", required = true) int id) throws Exception {
    	
    	ModelMap model = new ModelMap();
    	
    	User user = authenticator.getUser();
    	
    	try {
    		restaurants = flux.getFlux();
    		for(Restaurant r : restaurants.getRestaurants()) {
    			if(r.getId() == id) {
    				model.put("restaurant", r);
    				
    				ResultSet results = dc.executeQuery("SELECT USER FROM FAVORITERESTAURANT WHERE USER='" + user.getLogin() +"' AND RESTAURANTID =" + id);
    				
    				if(results.next())
    					 model.put("isFavorite", true);
    			}
    		}
    	
    	} catch(NullPointerException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}

    	return new ModelAndView("restaurant", model);
    }
    
    @RequestMapping(value = {"VIEW"}, params = {"action=viewMeals"})
    public ModelAndView renderMealsView(RenderRequest request, RenderResponse response, @RequestParam(value = "id", required=true) int id) throws Exception {
    	ModelMap model = new ModelMap();
    	
    	try {
    		restaurants = flux.getFlux();
    		for(Restaurant r : restaurants.getRestaurants()) {
    			if(r.getId() == id) {
    				model.put("restaurant", r);
    				
    				List<Manus> menuList = new ArrayList<Manus>();
    				Date dateNow = new Date();
    				for(Manus m : r.getMenus())  {
    					Date dateMenu = new SimpleDateFormat("yyyy-MM-dd").parse(m.getDate());
    					// We only send upcomings menu to the view
    					if(dateMenu.compareTo(dateNow) >= 0) {
    						menuList.add(m);
    					}
    				}
    				
    				model.put("menus", menuList);
    				
    			}
    		}
    	
    	} catch(Exception e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}
    	
    	return new ModelAndView("meals", model);
    }
    
    @RequestMapping(value = {"VIEW"}, params = {"action=setFavorite"})
    public void setFavorite(ActionRequest request, ActionResponse response, @RequestParam(value = "id", required = true) String id) throws Exception {
    	dc.executeUpdate("INSERT INTO FAVORITERESTAURANT (USER, RESTAURANTID) "
    				   + "VALUES('" + authenticator.getUser().getLogin() +"', '"+ id +"')");
    	response.setRenderParameter("id", id);
    	response.setRenderParameter("action", "viewRestaurant");    	
    }    
    
    @RequestMapping("EDIT")
    public ModelAndView renderEditView(RenderRequest request, RenderResponse response) throws Exception {
        
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
        	PortletSession sess = request.getPortletSession();
        	String userArea = (String) sess.getAttribute("userArea");
        	
        	if(userArea != null && userArea.length() != 0) {
        		model.put("defaultArea", userArea);
        	} else {
        		model.put("defaultArea", prefs.getValue("userArea", null));
        	}
        	
        	Set<String> favResults = new HashSet<String>();
        	try {
        		ResultSet results = dc.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USER='"+ user.getLogin() +"'");
        		while(results.next()) {
        			favResults.add(results.getString("restaurantId"));
        		}
        		results.close();
        	} catch(Exception e) {
        		System.out.println("On passe dans le Catch, la colonne demandé n'existe pas ???");
        	}
        	model.put("favList", favResults);	
       	
    	} catch(NullPointerException e) {
    		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
    	}
    	
    	String zoneSubmit = request.getParameter("zoneSubmit");
    	if(zoneSubmit != null)
    		model.put("zoneSubmit", zoneSubmit);
    	
    	return new ModelAndView("edit", model);
    }    
    
    @RequestMapping(value = {"EDIT"}, params = {"action=setUserArea"})
    public void setUserArea(ActionRequest request, ActionResponse response, @RequestParam(value = "zone", required = true) String area) throws Exception {
    	PortletSession sess = request.getPortletSession();
    	sess.setAttribute("userArea", area);
    	response.setRenderParameter("zoneSubmit", "true");
    }
    
    @RequestMapping(value = {"EDIT", "VIEW"}, params = {"action=removeFavorite"})
    public void removeFavorite(ActionRequest request, ActionResponse response, @RequestParam(value = "restaurant-id", required = true) String id) throws Exception {
    	dc.executeUpdate("DELETE FROM FAVORITERESTAURANT "
    				   + "WHERE RESTAURANTID=" + id 
    				   +" AND USER='"+ authenticator.getUser().getLogin() +"'");
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
    		model.put("urlError", hasError);
    	
    	String zoneSubmit = request.getParameter("zoneSubmit");
    	if(zoneSubmit != null)
    		model.put("zoneSubmit", zoneSubmit);
    	
    	return new ModelAndView("edit-admin", model);
    }
    
    @RequestMapping(value = {"EDIT"}, params = {"action=setDefaultArea"})
    public void setDefaultArea(ActionRequest request, ActionResponse response, @RequestParam(value = "zone", required = true) String area) throws Exception {
		response.setRenderParameter("action", "adminSettings");
		response.setRenderParameter("zoneSubmit", "true");
		ResultSet results = dc.executeQuery("SELECT AREANAME FROM PATHFLUX");
		results.next();
		results.updateString("AREANAME", area);
		results.updateRow();
    }
    
    @RequestMapping(value = {"EDIT"}, params = {"action=urlFlux"})
    public void setURLFlux(ActionRequest request, ActionResponse response, @RequestParam(value = "url", required = true) String url) throws Exception { 
  
		response.setRenderParameter("action", "adminSettings");
    	try {
    		URL urlFlux = new URL(url);	
    		flux.setPath(urlFlux);
    		flux.cacheJsonString();
    		response.setRenderParameter("urlError", "false");
    		
    		try {
    			ResultSet results = dc.executeQuery("SELECT URLFLUX FROM PATHFLUX");
    			results.next();
    			results.updateString("URLFLUX", url);
    			results.updateRow();
    		} catch (SQLException e) {
    			System.out.println("[INFO] URL isn't set, we insert a new ROW to the PATHFLUX table.");
    			dc.executeUpdate("INSERT INTO PATHFLUX (URLFLUX) VALUES ('"+ url +"')");
    		}
    		
    		
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