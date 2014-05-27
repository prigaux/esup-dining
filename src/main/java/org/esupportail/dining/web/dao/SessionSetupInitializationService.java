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
package org.esupportail.dining.web.dao;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

public class SessionSetupInitializationService implements
		IInitializationService {

	private String userAdminRole;
	private String dateLocalePattern;

	public SessionSetupInitializationService(String userAdminRole,
			String dateLocalePattern) {
		this.userAdminRole = userAdminRole;
		this.dateLocalePattern = dateLocalePattern;
	}

	public void initialize(PortletRequest request) {
		PortletSession session = request.getPortletSession(true);
		session.setAttribute("isAdmin",
				request.isUserInRole(this.userAdminRole),
				PortletSession.APPLICATION_SCOPE);
		session.setAttribute("dateLocalePattern", this.dateLocalePattern,
				PortletSession.APPLICATION_SCOPE);
	}

}