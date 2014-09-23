/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.dining.web.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.ui.Model;



@Controller
public class WebWidgetController extends AbstractExceptionController {

	@Autowired ViewResolver viewResolver;

	@RequestMapping("/WebWidget")
	public String renderWebWidget() throws Exception {
	    return "WebWidget";
	}

	@RequestMapping("/WebWidget.js")
	public ResponseEntity<String> renderWebWidgetJs(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {   
		String html = renderView(request, "WebWidget", model);

		String js = "document.write(" + new ObjectMapper().writeValueAsString(html) + ");";
		response.setContentType("application/x-javascript");
		return new ResponseEntity<String>(js, HttpStatus.OK);
	}

	private String renderView(HttpServletRequest request, String viewName, Model model) throws Exception {
		View resolvedView = viewResolver.resolveViewName(viewName, Locale.US);
		MockHttpServletResponse mockResp = new MockHttpServletResponse();
		resolvedView.render(model.asMap(), request, mockResp);		
	    return mockResp.getContentAsString();
	}

	
	
}
