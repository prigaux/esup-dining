package org.esupportail.restaurant.web.dao;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class SessionSetupInitializationService implements IInitializationService {
	
    private String userAdminRole;
    
    public SessionSetupInitializationService(String userAdminRole) {
        this.userAdminRole = userAdminRole;
    }
    
	@Override
	public void initialize(PortletRequest request) {
		PortletSession session = request.getPortletSession(true);
		session.setAttribute("isAdmin", request.isUserInRole(this.userAdminRole), PortletSession.APPLICATION_SCOPE);
	}

}
