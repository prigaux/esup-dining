/**
 * ESUP-Portail Blank Application - Copyright (c) 2010 ESUP-Portail consortium.
 */
package org.esupportail.restaurant.services.auth;

import org.esupportail.restaurant.domain.beans.User;

/**
 * @author Yves Deschamps (Universite de Lille 1) - 2010
 * 
 */
public interface Authenticator {

	/**
	 * @return the authenticated user.
	 * @throws Exception 
	 */
	User getUser() throws Exception;

}