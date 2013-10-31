package org.esupportail.restaurant.web.springmvc;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.restaurant.domain.beans.User;
import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.flux.RestaurantFlux;
import org.esupportail.restaurant.web.json.Dish;
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
    	
    	User user = authenticator.getUser();
    	String areaToDisplay = new String();
    	
    	try {
    		ResultSet results = dc.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='"+user.getLogin()+"';");
    		results.next();
    		areaToDisplay = results.getString("AREANAME");
    	} catch (SQLException e) {
    		// we are here if the user doesn't set a specific area for himself
    		ResultSet results = dc.executeQuery("SELECT AREANAME FROM PATHFLUX");
        	results.next();
        	try {
        		areaToDisplay = results.getString("AREANAME");
        	} catch (SQLException e2) {
        		// If there is no default area, then the admin must configure the portlet before.
        		model.put("nothingToDisplay", "This portlet needs to be configured by an authorized user");
        	}
    	}

    	model.put("area", areaToDisplay);
    	
    	try {
    		restaurants = flux.getFlux();
    		
        	List<Restaurant> dininghallList = new ArrayList<Restaurant>();   	
        	
    		for(Restaurant restaurant : restaurants.getRestaurants()) {
    			if(restaurant.getArea().equalsIgnoreCase(areaToDisplay))
    				dininghallList.add(restaurant);
    		}
 			model.put("dininghalls", dininghallList);
   
    	} catch(Exception e) {
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

    				ResultSet results = dc.executeQuery("SELECT * FROM FAVORITERESTAURANT WHERE USERNAME='" + user.getLogin() +"' AND RESTAURANTID='" + id + "';");
     				
    				if(results.next()) {
    					 model.put("isFavorite", true);
    				}
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
    	
    	User user = authenticator.getUser();
    	
    	try {
    		dc.executeUpdate("INSERT INTO FAVORITERESTAURANT VALUES ('" + user.getLogin() + "', '" + id + "');");
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	response.setRenderParameter("id", id);
    	response.setRenderParameter("action", "viewRestaurant");    	
    }    
   
    @RequestMapping(value = {"VIEW"}, params = {"action=viewDish"})
    public ModelAndView renderDish(RenderRequest request, RenderResponse response, 
			   @RequestParam(value = "name", required = true) String name,
			   @RequestParam(value = "ingredients", required = false) String ingredients,
			   @RequestParam(value = "nutritionitems", required = false) String nutritionitems,
			   @RequestParam(value = "code", required = false) String code,
			   @RequestParam(value = "id", required=true) int id) throws Exception {
    	
    	
    	ModelMap model = new ModelMap();
    	
    	model.put("restaurantId", id);
    	model.put("name", name);
    	model.put("ingredients", ingredients);
    	model.put("code", code.substring(1, code.length()-1).split(","));
    	
    	/* Awful code starts now */
    	// Need to find an other solution... a cleaner one.
    	
    	String str = nutritionitems.substring(1, nutritionitems.length()-1);		
		List<Map<String, String>> listNutritionItems = new ArrayList<Map<String, String>>();
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(str);
		while(m.find()) {
			Map<String, String> entry = new HashMap<String, String>();
			for(String s2 : m.group(1).split(",")) {
				String[] keyValue = s2.split("=");
				entry.put(keyValue[0], keyValue[1]);
			}
			listNutritionItems.add(entry);
		}
			
		/* Awful code ends now */
    	
    	model.put("nutritionitems", listNutritionItems);
    	
    	return new ModelAndView("dish", model);
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
    		
    		String userArea = new String();
    		try {
    			ResultSet results = dc.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='"+user.getLogin()+"';");
    			results.next();
    			userArea = results.getString("AREANAME");
    		} catch (SQLException e) {
    			// here we are if the user doesn't already have a specific area setting.
    			ResultSet results = dc.executeQuery("SELECT AREANAME FROM PATHFLUX");
    			results.next();
    			try {
    				userArea = results.getString("AREANAME");
    			} catch (SQLException e2) {
    				// No default area for all user, admin must configure the portlet. 
    				// No need to throw an exception
    			}
    			
    		}
    		model.put("defaultArea", userArea);
    		
        	Set<String> favResults = new HashSet<String>();
        	try {
        		ResultSet results = dc.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='"+ user.getLogin() +"'");
        		while(results.next()) {
        			favResults.add(results.getString("restaurantId"));
        		}
        	} catch(Exception e) {
        		// No data available, doesn't matter 
        	}
        	model.put("favList", favResults);
        	
        	List<Restaurant> listRestaurant = flux.getFlux().getRestaurants();
        	List<Restaurant> listFavRestaurant = new ArrayList<Restaurant>();
        
        	for(Restaurant r : listRestaurant) {
        		for(String favId : favResults) {
        			if(r.getId() == Integer.parseInt(favId, 10)) listFavRestaurant.add(r);
        		}
        	}

        	model.put("listFavRestaurant", listFavRestaurant);
       	
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

    	User user = authenticator.getUser();
    	
    	try {
    		ResultSet results = dc.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='" + user.getLogin() + "';");
    		results.next();
    		results.updateString("AREANAME", area);
    		results.updateRow();
    	} catch (SQLException e) {
    		dc.executeUpdate("INSERT INTO USERAREA (USERNAME, AREANAME) VALUES ('"+user.getLogin()+"', '"+area+"');");
    	}
    	
    	response.setRenderParameter("zoneSubmit", "true");
    }
    
    @RequestMapping(value = {"EDIT", "VIEW"}, params = {"action=removeFavorite"})
    public void removeFavorite(ActionRequest request, ActionResponse response, @RequestParam(value = "restaurant-id", required = true) String id) throws Exception {
    	dc.executeUpdate("DELETE FROM FAVORITERESTAURANT "
    				   + "WHERE RESTAURANTID=" + id 
    				   +" AND USERNAME='"+ authenticator.getUser().getLogin() +"'");
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
        	
    		ResultSet results = dc.executeQuery("SELECT URLFLUX, AREANAME FROM PATHFLUX");
    		results.next();
    		String area, urlflux;
    		try {
    			area = results.getString("AREANAME");
    		} catch (SQLException e) {
    			area = null;
    		}
    		try {
    			urlflux = results.getString("URLFLUX");
    		} catch (SQLException e) {
    			urlflux = null;
    		}

        	model.put("urlfluxdb", urlflux);
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
    	
    	String feedUpdate = request.getParameter("feedUpdate");
    	if(feedUpdate != null) {
    		if(Boolean.parseBoolean(feedUpdate)) {
    			model.put("updateFeed", "The feed has been correctly updated");
    		} else {
        		model.put("updateFeed", "The feed is already up to date");
        	}
    	} 
    	
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
    	/*
    	 * #TODO : 
    	 * Validate feed with the JSON Schema
    	 */
		response.setRenderParameter("action", "adminSettings");
    	try {
    		URL urlFlux = new URL(url);	
    		flux.setPath(urlFlux);
    		flux.cacheJsonString();
    		response.setRenderParameter("urlError", "false");
    		
    		// If URL is correct, then we can insert this into the database. 
    		
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
    
    @RequestMapping(value = {"EDIT"}, params = {"action=forceFeedUpdate"})
    public void feedUpdate(ActionRequest request, ActionResponse response) throws Exception {
    	
    	boolean needUpdate = flux.update();
    	/*
    	if(needUpdate) {
    		
    	}
    	
    	//String needUpdate = new Boolean(flux.update()).toString();
    	
    	//response.setRenderParameter("feedUpdate", needUpdate);
    	 * 
    	 */
    	response.setRenderParameter("action", "adminSettings");
    }
    
    @RequestMapping("ABOUT")
	public ModelAndView renderAboutView(RenderRequest request, RenderResponse response) throws Exception {
		ModelMap model = new ModelMap();
		return new ModelAndView("about", model);
	}
    
    @RequestMapping("HELP")
    public ModelAndView renderHelpView(RenderRequest request, RenderResponse response) throws Exception {
    	ModelMap model = new ModelMap();
		int[] code = {1,2,3,4,5,6,7,9,10,11,12,13,14,15};
		model.put("code", code);
    	return new ModelAndView("help", model);
    }
}