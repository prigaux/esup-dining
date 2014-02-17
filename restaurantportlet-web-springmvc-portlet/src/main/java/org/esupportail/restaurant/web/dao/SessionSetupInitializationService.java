package org.esupportail.restaurant.web.dao;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class SessionSetupInitializationService implements IInitializationService {
	
    private String userAdminRole;
    private String dateLocalePattern;
    
    public SessionSetupInitializationService(String userAdminRole, String dateLocalePattern) {
        this.userAdminRole = userAdminRole;
        this.dateLocalePattern = dateLocalePattern;
    }

	@Override
	public void initialize(PortletRequest request) {
		PortletSession session = request.getPortletSession(true);
		session.setAttribute("isAdmin", request.isUserInRole(this.userAdminRole), PortletSession.APPLICATION_SCOPE);
		session.setAttribute("dateLocalePattern", this.dateLocalePattern, PortletSession.APPLICATION_SCOPE);
	}

}
