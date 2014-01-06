package org.esupportail.restaurant.web.springmvc;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
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

    @RequestMapping(value = {"EDIT", "VIEW"}, params = {"action=removeFavorite"})
    public final void removeFavorite(final ActionRequest request, final ActionResponse response, @RequestParam(value = "restaurant-id", required = true) final String id) throws Exception {
    	dc.executeUpdate("DELETE FROM FAVORITERESTAURANT "
    				   + "WHERE RESTAURANTID=" + id 
    				   + "AND USERNAME='"+ authenticator.getUser().getLogin() +"'");    	
    }

}
