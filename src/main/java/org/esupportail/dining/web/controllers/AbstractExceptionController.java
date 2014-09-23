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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.esupportail.smsu.services.UrlGenerator;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractExceptionController {

	private final Logger logger = new LoggerImpl(this.getClass());

	@Autowired UrlGenerator urlGenerator;
	
        @ModelAttribute("baseURL")
	public String getBaseURL(HttpServletRequest request) {
	    return urlGenerator.baseURL(request);
        }	

	@ExceptionHandler(Exception.class)
	public final ModelAndView handleException(final Exception ex) {

		logger.error("Exception catching in spring mvc controller ... ", ex);

		ModelMap model = new ModelMap();

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		ex.printStackTrace(print);
		String exceptionStackTrace = new String(output.toByteArray());
		model.put("exceptionStackTrace", exceptionStackTrace);

		model.put("exceptionMessage", ex.getMessage());

		return new ModelAndView("exception", model);
	}

}
