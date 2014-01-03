package org.esupportail.restaurant.web.dao;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class SessionSetupInitializationService implements IInitializationService {
	
	@Override
	public void initialize(PortletRequest request) {
		PortletSession session = request.getPortletSession(true);
		session.setAttribute("isAdmin", request.isUserInRole("restaurantPortletAdmin"), PortletSession.APPLICATION_SCOPE);
	}

}
