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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.esupportail.dining.domain.beans.User;
import org.esupportail.dining.domainservices.services.auth.Authenticator;
import org.esupportail.dining.web.dao.DatabaseConnector;
import org.esupportail.dining.web.feed.DiningFeed;
import org.esupportail.dining.web.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/settings")
public class EditController extends AbstractExceptionController {

	@Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private DiningFeed feed;

	@RequestMapping
	    public ModelAndView renderEditView(HttpServletRequest request) throws Exception {
		ModelMap model = new ModelMap();

		User user = this.authenticator.getUser();
		model.put("user", user);

		int[] allergenCodes = { 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14 };
		model.put("allergenCodes", allergenCodes);

		int[] preferenceCodes = { 12, 15 };
		model.put("preferenceCodes", preferenceCodes);

		try {

			ResultSet prefUser = this.dc
					.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(user.getLogin()) + "';");

			Set<String> nutritionPrefs = new HashSet<String>();

			while (prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}

			model.put("nutritionPrefs", nutritionPrefs);

			prefUser.close();
		} catch (SQLException e) { /**/
		}

		try {
			Set<String> areaList = new HashSet<String>();
			for (Restaurant r : this.feed.getFeed().getRestaurants()) {
				areaList.add(r.getArea());
			}

			model.put("areaList", areaList);

			String userArea[] = null;
			try {
				ResultSet results = this.dc
						.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='"
								+ user.getLogin() + "';");
				results.next();
				userArea = results.getString("AREANAME").split(",");
				results.close();
			} catch (SQLException e) {
				// here we are if the user doesn't already have a specific area
				// setting.
				ResultSet results = this.dc
						.executeQuery("SELECT AREANAME FROM PATHFLUX");
				results.next();
				try {
					userArea = results.getString("AREANAME").split(",");
				} catch (SQLException e2) {
					// No default area for all user, admin must configure the app.
					// No need to throw an exception
				}
				results.close();

			}
			model.put("defaultArea", userArea);

			Set<String> favResults = new HashSet<String>();
			try {
				ResultSet results = this.dc
						.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='"
								+ user.getLogin() + "'");
				while (results.next()) {
					favResults.add(results.getString("restaurantId"));
				}
				results.close();
			} catch (Exception e) {
				// No data available, doesn't matter
			}
			model.put("favList", favResults);

			List<Restaurant> listRestaurant = this.feed.getFeed().getRestaurants();
			List<Restaurant> listFavRestaurant = new ArrayList<Restaurant>();

			for (Restaurant r : listRestaurant) {
				for (String favId : favResults) {
					if (r.getId() == Integer.parseInt(favId, 10)) {
						listFavRestaurant.add(r);
					}
				}
			}

			model.put("listFavRestaurant", listFavRestaurant);

		} catch (NullPointerException e) { /**/
		}

		/* param from setUserArea action */
		String zoneSubmit = request.getParameter("zoneSubmit");
		if (zoneSubmit != null) {
			model.put("zoneSubmit", zoneSubmit);
		}
		/* param from nutritionPreferences action */
		String nutritSubmit = request.getParameter("nutritSubmit");
		if (nutritSubmit != null) {
			model.put("nutritSubmit", nutritSubmit);
		}
		return new ModelAndView("edit", model);
	}

	@RequestMapping(params = { "action=nutritionPreferences" })
	public String setUserNutritionPreferences(HttpServletRequest request) throws Exception {

		String userLogin = this.authenticator.getUser().getLogin();

		Map parameters = request.getParameterMap();

		int[] code = { 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15 };

		for (int i = 0; i < code.length; i++) {

			if (parameters.get("code-" + code[i]) != null) {

				try {
					this.dc.executeUpdate("INSERT INTO nutritionPreferences (USERNAME, NUTRITIONCODE) VALUES ('"
							+ StringEscapeUtils.escapeSql(userLogin) + "', '" + code[i] + "');");
				} catch (SQLException e) { /**/
				}

			} else {
				/*
				 * uncheck boxes need to be deleted from the db if it goes into
				 * the catch it mean the row didn't exist and it doesn't need
				 * specific treatment
				 */
				try {
					this.dc.executeUpdate("DELETE FROM nutritionPreferences WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(userLogin)
							+ "' AND  NUTRITIONCODE='"
							+ code[i]
									+ "';");
				} catch (SQLException e) { /**/
				}

			}
		}
		return "redirect:/settings?nutritSubmit=true";
	}

	@RequestMapping(params = { "action=setUserArea" })
	public String setUserArea(
			@RequestParam(value = "chkArea[]", required = false) String[] listAreas)
					throws Exception {

		User user = this.authenticator.getUser();

		String areanames = "";

		if (listAreas != null) {

			for (int i = 0; i < listAreas.length; i++) {
				areanames += listAreas[i]
						+ (i < listAreas.length - 1 ? "," : "");
			}

			try {
				ResultSet results = this.dc
						.executeQuery("SELECT * FROM USERAREA WHERE USERNAME='"
								+ user.getLogin() + "';");
				results.next();
				results.updateString("AREANAME", areanames);
				results.updateRow();
				results.close();
			} catch (SQLException e) {
				this.dc.executeUpdate("INSERT INTO USERAREA (USERNAME, AREANAME) VALUES ('"
						+ StringEscapeUtils.escapeSql(user.getLogin()) + "', '" + StringEscapeUtils.escapeSql(areanames) + "');");
			}

			return "redirect:/settings?zoneSubmit=true";
		}
		return "redirect:/settings";
	}
}
