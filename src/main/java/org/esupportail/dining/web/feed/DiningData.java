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
package org.esupportail.dining.web.feed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esupportail.dining.domain.beans.User;
import org.esupportail.dining.web.dao.DatabaseConnector;
import org.esupportail.dining.web.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

public class DiningData {

	@Autowired
	private DiningFeed feed;
	@Autowired
	DiningCache cache;
	@Autowired
	private DatabaseConnector dc;

	public DiningData() {

	}

	public List<Restaurant> getFavoriteList(User user) {
		List<Restaurant> favorites = new ArrayList<Restaurant>();

		try {
			ResultSet favList = this.dc
					.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='"
							+ user.getLogin() + "';");

			if (favList.next()) {
				for (Restaurant r : this.feed.getFeed().getRestaurants()) {

					do {
						if (r.getId() == favList.getInt("RESTAURANTID")) {
							favorites.add(r);
						}
					} while (favList.next());

					favList.first();
				}
			}

		} catch (SQLException e) {
			// Nothing to do here.
		} catch (NullPointerException e2) {
			// nop
		}

		return favorites;
	}

	public List<Restaurant> getRestaurantList(String zone) {
		List<Restaurant> dininghallList = new ArrayList<Restaurant>();
		try {
			for (Restaurant restaurant : this.feed.getFeed().getRestaurants()) {
				if (restaurant.getArea().equalsIgnoreCase(zone)) {
					dininghallList.add(restaurant);
				}
			}

		} catch (Exception e) { /**/
		}
		return dininghallList;
	}

	public Set<String> getNutritionItems(User user) {
		Set<String> nutritionPrefs = new HashSet<String>();

		try {

			ResultSet prefUser = this.dc
					.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"
							+ user.getLogin() + "';");

			while (prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}

		} catch (SQLException e) { /**/
		}

		return nutritionPrefs;
	}

}
