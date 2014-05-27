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
package org.esupportail.dining.domainservices.domain;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.dining.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Yves Deschamps (Universite de Lille 1) - 2010
 * 
 */
public class DomainServiceImpl implements DomainService, InitializingBean {

	/**
	 * For Serialize.
	 */
	private static final long serialVersionUID = 5562208937407153456L;

	/**
	 * For Logging.
	 */
	@SuppressWarnings("unused")
	private final Logger logger = new LoggerImpl(this.getClass());

	/**
	 * En l'absence de Dao et de Ldap, on constitue ici une liste... limitee de
	 * fait a l'utilisateur courant.
	 */
	private List<User> users;

	/**
	 * Constructor.
	 */
	public DomainServiceImpl() {
		super();
		users = new ArrayList<User>();
	}

	public void afterPropertiesSet() throws Exception {
		// nothing to do yet.
	}

	public User getUser(String uid) {
		User user = null;
		for (User userInList : users) {
			if (userInList.getLogin().equals(uid)) {
				user = userInList;
				break;
			}
		}
		if (user == null) {
			user = new User();
			user.setLogin(uid);
			// On cree l'utilisateur, son nom complet prend la valeur de
			// l'Uid.
			user.setDisplayName(uid);
			user.setLanguage("fr");
			users.add(user);
		}
		return user;
	}

}
