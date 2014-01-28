package org.esupportail.restaurant.web.springmvc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import org.esupportail.restaurant.web.flux.RestaurantFeed;
import org.esupportail.restaurant.web.model.Restaurant;
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
	private RestaurantFeed flux;


    @RequestMapping(params = {"action=adminSettings"})
    public ModelAndView renderEditAdminView(RenderRequest request, RenderResponse response) throws Exception {

    	ModelMap model = new ModelMap();

    	User user = authenticator.getUser();


	    	model.put("user", user);

	    	try {

	    		Set<String> areaList = new HashSet<String>();
	    		for (Restaurant r : flux.getFlux().getRestaurants()) {
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

	    	} catch (NullPointerException e) { /**/ }

	    	String hasError = request.getParameter("urlError");
	    	if (hasError != null)
	    		model.put("urlError", hasError);
	    	
	    	String zoneSubmit = request.getParameter("zoneSubmit");
	    	if (zoneSubmit != null)
	    		model.put("zoneSubmit", zoneSubmit);
	    	
	    	if (request.getParameter("update") != null) {
		    	Boolean isUpdated = new Boolean(request.getParameter("update"));
		    	if (isUpdated.booleanValue()) {
		    		model.put("updateFeed", "The feed has been correctly updated");
	    		} else {
	        		model.put("updateFeed", "The feed is already up to date");
	        	}
	    	}
    	return new ModelAndView("edit-admin", model);
    }
    @RequestMapping(params = {"action=adminStats"})
    public ModelAndView renderStatsAdminView(RenderRequest request, RenderResponse response) throws Exception {
        ModelMap model = new ModelMap();
        
        ResultSet results = dc.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT");
        
        HashMap<Integer, Integer> nbRestaurant = new HashMap<Integer, Integer>();
        
        while(results.next()) {
            int currentValue = (nbRestaurant.get(results.getInt("RESTAURANTID")) == null ? 0 : nbRestaurant.get(results.getInt("RESTAURANTID")));
            if(currentValue == 0) {
                nbRestaurant.put(results.getInt("RESTAURANTID"), 1);
            } else {
                nbRestaurant.put(results.getInt("RESTAURANTID"), ++currentValue);
            }
        }
        
        HashMap<Integer, String> restaurantsName = new HashMap<Integer,String>();
        for(Restaurant r : flux.getFlux().getRestaurants()) {
            restaurantsName.put(r.getId(), r.getTitle());
        }
        
        ResultSet resultsNutrit = dc.executeQuery("SELECT NUTRITIONCODE, COUNT(*) FROM NUTRITIONPREFERENCES GROUP BY NUTRITIONCODE");
        Map<Integer, Integer> prefCodeList = new HashMap<Integer, Integer>();
        
        while(resultsNutrit.next()) {
            prefCodeList.put(resultsNutrit.getInt(1), resultsNutrit.getInt(2));
        }
        
        model.put("prefCodeList", prefCodeList);
        model.put("stats", nbRestaurant);
        model.put("restaurantsName", restaurantsName);
                     
        return new ModelAndView("stats", model);
    }  

    @RequestMapping(params = {"action=urlFlux"})
    public void setURLFlux(ActionRequest request, ActionResponse response, @RequestParam(value = "url", required = true) String url) throws Exception { 	
    	response.setRenderParameter("action", "adminSettings");
    	try {
    	    URL urlFlux = new URL(url);	
    		flux.setPath(urlFlux);
    		response.setRenderParameter("urlError", "false");
    		// If URL is correct, then we can insert this into the database. 
			ResultSet results = dc.executeQuery("SELECT URLFLUX FROM PATHFLUX");
			results.next();
			results.updateString("urlflux", url);
			results.updateRow();
    	} catch (MalformedURLException e) {
    		response.setRenderParameter("urlError", "true");
    	} catch (SQLException e) {
    		dc.executeUpdate("INSERT INTO PATHFLUX (URLFLUX) VALUES ('"+ url +"')");
    	} catch (JsonParseException e) {
    		response.setRenderParameter("urlError", "true");
    	} catch (JsonMappingException e) {
    		response.setRenderParameter("urlError", "true");
    	} catch (IOException e) {
    		response.setRenderParameter("urlError", "true");
    	} catch (Exception e) {
    		response.setRenderParameter("urlError", "true");
    	}
    }
    
    @RequestMapping(params = {"action=setDefaultArea"})
    public void setDefaultArea(ActionRequest request, ActionResponse response, /*@RequestParam(value="areas", required=true) String[] areas*/ @RequestParam(value="test", required=false) String test) throws Exception {
		User user = authenticator.getUser();

    	response.setRenderParameter("action", "adminSettings");
		response.setRenderParameter("zoneSubmit", "true");
		System.out.println(test);
//		System.out.println(areas);
    }

    @RequestMapping(params = {"action=forceFeedUpdate"})
    public void feedUpdate(ActionRequest request, ActionResponse response) throws Exception {

    	User user = authenticator.getUser();
    	Boolean isUpdated;

		isUpdated = flux.update();

    	response.setRenderParameter("action", "adminSettings");
    	response.setRenderParameter("update", isUpdated.toString());
    }

}
