package org.esupportail.restaurant.web.springmvc;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("ABOUT")
public class AboutController extends AbstractExceptionController {

	@RequestMapping
	public ModelAndView renderAboutView(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelMap model = new ModelMap();
		return new ModelAndView("about", model);
	}
}
