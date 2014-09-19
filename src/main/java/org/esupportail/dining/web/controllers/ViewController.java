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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.esupportail.dining.domain.beans.User;
import org.esupportail.dining.domainservices.services.auth.Authenticator;
import org.esupportail.dining.web.dao.DatabaseConnector;
import org.esupportail.dining.web.feed.DiningCache;
import org.esupportail.dining.web.feed.DiningFeed;
import org.esupportail.dining.web.models.Manus;
import org.esupportail.dining.web.models.Restaurant;
import org.esupportail.dining.web.models.RestaurantFeedRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController extends AbstractExceptionController {

	@Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private DiningFeed feed;

	private RestaurantFeedRoot dinings;

	@Autowired
	private DiningCache cache;

	@RequestMapping("/restaurants")
	public ModelAndView renderMain() throws Exception {
		ModelMap model = new ModelMap();
		User user = this.authenticator.getUser();
		
		
		/* Get Area To Display */
		String[] areaToDisplay = null;
		try {
			// Check if user has a special options that overrides default config
			ResultSet results = this.dc.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='" + StringEscapeUtils.escapeSql(user.getLogin()) + "';");
			results.next();
			areaToDisplay = results.getString("AREANAME").split(",");
		} catch (Exception e) {
			
			// If not we just fetch data from the default config
			ResultSet results = this.dc.executeQuery("SELECT AREANAME FROM PATHFLUX WHERE IS_DEFAULT=TRUE;");
			results.next();
			areaToDisplay = results.getString("AREANAME").split(",");
		}
		model.addAttribute("area", areaToDisplay);
		
		// Get favorite for the current user
		try {
			ResultSet favList = this.dc
					.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(user.getLogin()) + "';");
			List<Restaurant> favorites = new ArrayList<Restaurant>();

			if (favList.next()) {
				for (Restaurant r : this.feed.getFeed().getRestaurants()) {

					do {
						if (r.getId() == favList.getInt("RESTAURANTID")) {
							favorites.add(r);
						}
					} while (favList.next());

					favList.first();
				}

				model.put("favorites", favorites);
			}

		} catch (Exception e) {
			// Nothing to do here.
			// If SQL fetch throw an exception nevermind, it means the user does not have any fav atm.
		}
		
		// Fetch data from the feed based on the fav and area settings
		try {

			this.dinings = this.feed.getFeed();

			Map<String, List<Restaurant>> areasToDisplayList = new HashMap<String, List<Restaurant>>();

			for (int i = 0; i < areaToDisplay.length; i++) {
				areasToDisplayList.put(areaToDisplay[i],
						new ArrayList<Restaurant>());
			}

			for (Restaurant restaurant : this.dinings.getRestaurants()) {
				if (areasToDisplayList.containsKey(restaurant.getArea())) {
				    // update "isClosed" with current date
					restaurant.setAdditionalProperties("isClosed",
							this.feed.isClosed(restaurant));
					areasToDisplayList.get(restaurant.getArea())
					.add(restaurant);
				}
			}
			model.put("restaurantLists", areasToDisplayList);

		} catch (NullPointerException e) { /* */
		} catch (Exception e) {
			return new ModelAndView("error",
					new ModelMap("err", e.getMessage()));
		}
		
		return new ModelAndView("view", model);		
	}

	@RequestMapping("/restaurant")
	public ModelAndView renderRestaurantView(
			@RequestParam(value = "id", required = true) int id)
					throws Exception {
		ModelMap model = new ModelMap();

		User user = this.authenticator.getUser();

		Restaurant restaurant = this.cache.getCachedDining(id);
		model.put("restaurant", restaurant);

		try {

			ResultSet results = this.dc
					.executeQuery("SELECT * FROM FAVORITERESTAURANT WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(user.getLogin())
							+ "' AND RESTAURANTID='"
							+ id
							+ "';");
			if (results.next()) {
				model.put("isFavorite", true);
			}

			if (this.feed.isClosed(restaurant)) {
				model.put("restaurantClosed", true);
			}

		} catch (NullPointerException e) { /*
		 * Useful is the user isn't logged
		 * in
		 */
		}

		return new ModelAndView("restaurant", model);
	}

	@RequestMapping("/meals")
	public ModelAndView renderMealsView(
			@RequestParam(value = "id", required = true) int id)
					throws Exception {

		ModelMap model = new ModelMap();
		User user = this.authenticator.getUser();

		try {

			ResultSet prefUser = this.dc
					.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(user.getLogin()) + "';");

			Set<String> nutritionPrefs = new HashSet<String>();

			while (prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}

			model.put("nutritionPrefs", nutritionPrefs);

		} catch (SQLException e) { /**/
		} catch (NullPointerException e) { /*
		 * Useful is the user isn't logged
		 * in
		 */
		}

		try {
			Restaurant restaurant = this.cache.getCachedDining(id);
			model.put("restaurant", restaurant);
			List<Manus> menuList = new ArrayList<Manus>();
			Date dateNow = new Date();
			for (Manus m : restaurant.getMenus()) {
				Date dateMenu = new SimpleDateFormat("yyyy-MM-dd").parse(m
						.getDate());
				// We only send upcomings menu to the view
				if (dateMenu.compareTo(dateNow) >= 0) {
					menuList.add(m);
				}
			}
			model.put("menus", menuList);

			if (this.feed.isClosed(restaurant)) {
				model.put("restaurantClosed", true);
			}

		} catch (Exception e) {
			return new ModelAndView("error",
					new ModelMap("err", e.getMessage()));
		}

		try {
			ResultSet results = this.dc
					.executeQuery("SELECT * FROM FAVORITERESTAURANT WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(user.getLogin())
							+ "' AND RESTAURANTID='"
							+ id
							+ "';");
			if (results.next()) {
				model.put("isFavorite", true);
			}
		} catch (NullPointerException e) { /*
		 * Useful is the user isn't logged
		 * in
		 */
		}

		return new ModelAndView("meals", model);
	}

	@RequestMapping("/favorite/add")
	public String setFavorite(@RequestParam(value = "id", required = true) String id)
					throws Exception {

		User user = this.authenticator.getUser();

		try {
			this.dc.executeUpdate("INSERT INTO FAVORITERESTAURANT VALUES ('"
					+ StringEscapeUtils.escapeSql(user.getLogin()) + "', '" + StringEscapeUtils.escapeSql(id) + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) { /*
		 * Useful is the user isn't logged
		 * in
		 */
		}

		return "redirect:/restaurant?id=" + id;
	}

	@RequestMapping("/dish")
	public ModelAndView renderDish(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "ingredients", required = false) String ingredients,
			@RequestParam(value = "nutritionitems", required = false) String nutritionitems,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "id", required = true) int id)
					throws Exception {

		User user = this.authenticator.getUser();
		ModelMap model = new ModelMap();

		model.put("restaurantId", id);
		model.put("name", name);
		model.put("ingredients", ingredients);
		model.put("code", code.substring(1, code.length() - 1).split(","));

		/* Awful code starts now */
		// Need to find an other solution... a cleaner one.

		String str = nutritionitems.substring(1, nutritionitems.length() - 1);
		List<Map<String, String>> listNutritionItems = new ArrayList<Map<String, String>>();
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(str);
		while (m.find()) {
			Map<String, String> entry = new HashMap<String, String>();
			for (String s2 : m.group(1).split(",")) {
				String[] keyValue = s2.split("=");
				entry.put(keyValue[0], keyValue[1]);
			}
			listNutritionItems.add(entry);
		}

		/* Awful code ends now */

		model.put("nutritionitems", listNutritionItems);

		try {

			ResultSet prefUser = this.dc
					.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"
							+ StringEscapeUtils.escapeSql(user.getLogin()) + "';");
			Set<String> nutritionPrefs = new HashSet<String>();

			while (prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}

			model.put("nutritionPrefs", nutritionPrefs);

		} catch (SQLException e) { /**/
		} catch (NullPointerException e) { /*
		 * Useful is the user isn't logged
		 * in
		 */
		}

		return new ModelAndView("dish", model);
	}
}
