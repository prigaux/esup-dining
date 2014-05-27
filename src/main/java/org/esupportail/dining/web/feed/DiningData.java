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
