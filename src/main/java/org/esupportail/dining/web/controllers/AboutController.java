package org.esupportail.dining.web.controllers;

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
    public final ModelAndView renderAboutView(final RenderRequest request,
			final RenderResponse response) throws Exception {
		ModelMap model = new ModelMap();
		return new ModelAndView("about", model);
	}
}
