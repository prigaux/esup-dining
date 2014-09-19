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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.esupportail.dining.domain.beans.User;
import org.esupportail.dining.domainservices.services.auth.Authenticator;
import org.esupportail.dining.web.dao.DatabaseConnector;
import org.esupportail.dining.web.feed.DiningFeed;
import org.esupportail.dining.web.feed.FeedInformation;
import org.esupportail.dining.web.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class EditAdminController extends AbstractExceptionController {

	@Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private DiningFeed feed;

	@RequestMapping
	public ModelAndView renderEditAdminView(HttpServletRequest request) throws Exception {

		ModelMap model = new ModelMap();

		User user = this.authenticator.getUser();
		model.put("user", user);

		// try {

		/* Get all area in the current feed */

		try {
			Set<String> areaList = new HashSet<String>();
			for (Restaurant r : this.feed.getFeed().getRestaurants()) {
				areaList.add(r.getArea());
			}
			model.put("areaList", areaList);
		} catch (Exception e) {
			// Here we go if the URL isn't set.
		}

		String[] areanames = null;
		List<FeedInformation> feedInfoList = new ArrayList<FeedInformation>();
		ResultSet results = null;
		try {
			results = this.dc.executeQuery("SELECT * FROM PATHFLUX");
			
			while(results.next()) {
				FeedInformation feedInfo = new FeedInformation(
					results.getInt("id"),
					results.getString("name"),
					results.getString("areaname"),
					results.getString("urlflux"),
					results.getBoolean("is_default"));
				feedInfoList.add(feedInfo);
			}
			
		} catch (SQLException e) {
			// URL isn't set yet...
		}

		try {
			for(FeedInformation fi : feedInfoList) {
				if(fi.isDefault()) {
					String areaname = fi.getAreaname();
					areanames = (areaname == null ? "" : areaname).split(",");
				}
			}
		} catch (Exception e) {
			// URL may be set be default area is not
		}
		model.put("feedList", feedInfoList);
		model.put("defaultArea", areanames);
		
		/* Action urlFeed set urlError if form URL was not well-formed */
		String hasError = request.getParameter("urlError");
		if (hasError != null) {
			model.put("urlError", hasError);
		}

		/* Action setDefaultArea set urlError if form URL was not well-formed */
		String areaSubmit = request.getParameter("areaSubmit");
		if (areaSubmit != null) {
			model.put("areaSubmit", areaSubmit);
		}

		/* From ForceFeedUpdate */
		if (request.getParameter("update") != null) {
			Boolean isUpdated = new Boolean(request.getParameter("update"));
			if (isUpdated.booleanValue()) {
				model.put("updateFeed", "The feed has been correctly updated");
			} else {
				model.put("updateFeed", "The feed is already up to date");
			}
		}
		return new ModelAndView("editadmin", model);
	}

	@RequestMapping("/stats")
	public ModelAndView renderStatsAdminView() throws Exception {

		ModelMap model = new ModelMap();

		/* Favorite Restaurants stats */

		ResultSet results = this.dc
				.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT");

		HashMap<Integer, Integer> nbRestaurant = new HashMap<Integer, Integer>();

		while (results.next()) {
			int currentValue = nbRestaurant
					.get(results.getInt("RESTAURANTID")) == null ? 0
							: nbRestaurant.get(results.getInt("RESTAURANTID"));
			if (currentValue == 0) {
				nbRestaurant.put(results.getInt("RESTAURANTID"), 1);
			} else {
				nbRestaurant
				.put(results.getInt("RESTAURANTID"), ++currentValue);
			}
		}

		HashMap<Integer, String> restaurantsName = new HashMap<Integer, String>();
		for (Restaurant r : this.feed.getFeed().getRestaurants()) {
			restaurantsName.put(r.getId(), r.getTitle());
		}

		/* Nutrition preferences stats */

		ResultSet resultsNutrit = this.dc
				.executeQuery("SELECT NUTRITIONCODE, COUNT(*) FROM NUTRITIONPREFERENCES GROUP BY NUTRITIONCODE");
		Map<Integer, Integer> prefCodeList = new HashMap<Integer, Integer>();

		while (resultsNutrit.next()) {
			prefCodeList.put(resultsNutrit.getInt(1), resultsNutrit.getInt(2));
		}

		model.put("prefCodeList", prefCodeList);
		model.put("stats", nbRestaurant);
		model.put("restaurantsName", restaurantsName);

		return new ModelAndView("stats", model);
	}

	@RequestMapping(params = { "action=urlFeed" })
	public String setURLFeed(@RequestParam(value = "feedId", required = true) int feedId)
					throws Exception {
		
		
		boolean urlError;
		try {
			this.dc.executeUpdate("UPDATE PATHFLUX SET is_default=false WHERE is_default=true");
			
			ResultSet result = this.dc.executeQuery("SELECT * FROM PATHFLUX WHERE ID=" + feedId);
			result.next();
			result.updateBoolean("is_default", true);
			result.updateRow();
			URL urlFeed = new URL(result.getString("URLFLUX"));
			urlError = false;
			
			// We just avoid problem this way
			// Favorite are feed related
			// If admin update default feed then fav are not relevant anymore.
			this.dc.execute("DELETE FROM USERAREA");
			this.dc.execute("DELETE FROM FAVORITERESTAURANT");
			
			this.feed.setPath(urlFeed);
			
		} catch (Exception e) {
			e.printStackTrace();
			urlError = true;
		}
		return "redirect:/admin?urlError=" + urlError;

	}

	@RequestMapping(params = { "action=setDefaultArea" })
	public String setDefaultArea(
			@RequestParam(value = "chkArea[]", required = false) String[] listAreas)
					throws Exception {
		String areanames = "";
		if (listAreas != null) {
			for (int i = 0; i < listAreas.length; i++) {
				areanames += listAreas[i]
						+ (i < listAreas.length - 1 ? "," : "");
			}
		}

		ResultSet results = this.dc.executeQuery("SELECT * FROM PATHFLUX");
		results.next();
		results.updateString("AREANAME", areanames);
		results.updateRow();

		return "redirect:/admin/?areaSubmit=true";
	}

	@RequestMapping(params = { "action=forceFeedUpdate" })
	public String feedUpdate()
			throws Exception {

		Boolean isUpdated = this.feed.update();
		return "redirect:/admin?update=" + isUpdated;
	}
}
