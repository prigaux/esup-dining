package org.esupportail.restaurant.web.springmvc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.esupportail.restaurant.domain.beans.User;
import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.flux.RestaurantFlux;
import org.esupportail.restaurant.web.json.Restaurant;
import org.esupportail.restaurant.web.json.RestaurantFeedRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("EDIT")
public class EditAdminController extends AbstractExceptionController {

	@Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private RestaurantFlux flux;
	private RestaurantFeedRoot restaurants;
	
    @RequestMapping(params = {"action=adminSettings"})
    public ModelAndView renderEditAdminView(RenderRequest request, RenderResponse response) throws Exception {
    	
    	ModelMap model = new ModelMap();
    	
    	User user = authenticator.getUser();
    	
    	if(user.isAdmin()) {
	    	
	    	model.put("user", user);
	    	
	    	try {
	        	
	    		Set<String> areaList = new HashSet<String>();
	    		for(Restaurant r : flux.getFlux().getRestaurants()) {
	    			areaList.add(r.getArea());
	    		}
	        	
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
	    		
	    		model.put("areas", areaList);
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
	    	
    	}
    	
    	return new ModelAndView("edit-admin", model);
    }

    @RequestMapping(params = {"action=urlFlux"})
    public void setURLFlux(ActionRequest request, ActionResponse response, @RequestParam(value = "url", required = true) String url) throws Exception { 
		
    	User user = authenticator.getUser();
    	
    	if(user.isAdmin()) {
        	
        	response.setRenderParameter("action", "adminSettings");
        	try {
        		URL urlFlux = new URL(url);	
        		flux.setPath(urlFlux);
        		response.setRenderParameter("urlError", "false");
        		
        		// If URL is correct, then we can insert this into the database. 
        		
    			ResultSet results = dc.executeQuery("SELECT URLFLUX FROM PATHFLUX");
    			results.next();
    			results.updateString("URLFLUX", url);
    			results.updateRow();   		
    			
        	} catch(MalformedURLException e) {
        		response.setRenderParameter("urlError", "true");
        	} catch(SQLException e) {
        		System.out.println("[INFO] URL isn't set, we insert a new ROW to the PATHFLUX table.");
        		dc.executeUpdate("INSERT INTO PATHFLUX (URLFLUX) VALUES ('"+ url +"')");
        	} catch(JsonParseException e) {
        		response.setRenderParameter("urlError", "true");
        	} catch(JsonMappingException e) {
        		response.setRenderParameter("urlError", "true");
        	} catch(IOException e) {
        		response.setRenderParameter("urlError", "true");
        	} catch(Exception e) {
        		response.setRenderParameter("urlError", "true");
        	}	
    	}
    }
    
    @RequestMapping(params = {"action=setDefaultArea"})
    public void setDefaultArea(ActionRequest request, ActionResponse response, @RequestParam(value = "zone", required = true) String area) throws Exception {
		User user = authenticator.getUser();
    	
    	response.setRenderParameter("action", "adminSettings");
		response.setRenderParameter("zoneSubmit", "true");
		
		if(user.isAdmin()) {
			ResultSet results = dc.executeQuery("SELECT AREANAME FROM PATHFLUX");
			results.next();
			results.updateString("AREANAME", area);
			results.updateRow();
		}
    }
    
    @RequestMapping(params = {"action=forceFeedUpdate"})
    public void feedUpdate(ActionRequest request, ActionResponse response) throws Exception {
    	boolean needUpdate = flux.update();
    	response.setRenderParameter("action", "adminSettings");
    }
	
}
