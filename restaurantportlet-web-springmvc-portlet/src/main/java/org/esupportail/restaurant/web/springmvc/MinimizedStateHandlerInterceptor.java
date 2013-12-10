package org.esupportail.restaurant.web.springmvc;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.esupportail.restaurant.services.auth.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;

public class MinimizedStateHandlerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Authenticator authenticator;
	
    @Override
    public boolean preHandleRender(RenderRequest request, RenderResponse response, Object handler) throws Exception {
        if (WindowState.MINIMIZED.equals(request.getWindowState())) {
            return false;
        }
        
        return true;
    }
}