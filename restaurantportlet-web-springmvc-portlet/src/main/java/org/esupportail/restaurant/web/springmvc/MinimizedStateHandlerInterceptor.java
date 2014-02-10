package org.esupportail.restaurant.web.springmvc;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.dao.IInitializationService;
import org.esupportail.restaurant.web.dao.SessionSetupInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;

public class MinimizedStateHandlerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Authenticator authenticator;

	private IInitializationService initializationService;

    // Is executed before every render
    // Set admin rights and check if the portlet is minimized
    @Override
    public boolean preHandleRender(RenderRequest request, RenderResponse response, Object handler) throws Exception {

        PortletSession session = request.getPortletSession(true);
        if (session.getAttribute("isAdmin") == null) {
            initializationService.initialize(request);
        }
        
        if (WindowState.MINIMIZED.equals(request.getWindowState())) {
            return false;
        }

        return true;
    }

    @Required
    @Resource(name = "sessionSetup")
    public void setInitializationServices(final IInitializationService service) {
        this.initializationService = service;
    }
    
}