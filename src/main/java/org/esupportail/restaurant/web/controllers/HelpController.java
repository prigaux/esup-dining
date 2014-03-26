package org.esupportail.restaurant.web.controllers;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("HELP")
public class HelpController extends AbstractExceptionController {

	@RequestMapping
    public ModelAndView renderHelpView(RenderRequest request, RenderResponse response) throws Exception {
    	/* Send nutrition code to the view in order to display them to the user with definitions */

    	ModelMap model = new ModelMap();
		int[] code     = {1,2,3,4,5,6,7,9,10,11,12,13,14,15};
		model.put("code", code);
    	return new ModelAndView("help", model);
    }

}
