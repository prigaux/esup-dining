package org.esupportail.dining.web.controllers;


import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.esupportail.dining.domainservices.services.auth.Authenticator;
import org.esupportail.dining.web.dao.DatabaseConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

	@Autowired
	private Authenticator authenticator;	
	@Autowired
	private DatabaseConnector dc;

    /* this method is used in user edit param and in the restaurant and meal view */
    @RequestMapping(value = {"EDIT", "VIEW"}, params = {"action=removeFavorite"})
    public final void removeFavorite(final ActionRequest request, final ActionResponse response, @RequestParam(value = "restaurant-id", required = true) final String id) throws Exception {
        try {
            dc.executeUpdate("DELETE FROM FAVORITERESTAURANT "
        				   + "WHERE RESTAURANTID=" + id 
        				   + "AND USERNAME='"+ authenticator.getUser().getLogin() +"'");    	
        } catch (NullPointerException e) { /* Useful is the user isn't logged in */ }
    }

}
