package org.esupportail.restaurant.web.springmvc;

import java.io.File;
import java.net.URL;
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

import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esupportail.restaurant.domain.beans.User;
import org.esupportail.restaurant.services.auth.Authenticator;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.dao.IInitializationService;
import org.esupportail.restaurant.web.flux.RestaurantCache;
import org.esupportail.restaurant.web.flux.RestaurantData;
import org.esupportail.restaurant.web.flux.RestaurantFeed;
import org.esupportail.restaurant.web.flux.RestaurantParser;
import org.esupportail.restaurant.web.model.Manus;
import org.esupportail.restaurant.web.model.Restaurant;
import org.esupportail.restaurant.web.model.RestaurantFeedRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class ViewController extends AbstractExceptionController {

	@Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private RestaurantFeed flux;
	
	private RestaurantFeedRoot restaurants;

	private IInitializationService initializationService;

	@Autowired
	private RestaurantCache cache;

	@RequestMapping
	public ModelAndView renderMainView(RenderRequest request, RenderResponse response) throws Exception {
	  
		ModelMap model = new ModelMap();

		User user = authenticator.getUser();
		
		String areaToDisplay = new String();

		PortletSession session = request.getPortletSession(true);
		if (session.getAttribute("isAdmin") == null) {
			initializationService.initialize(request);
		}

		try {
			ResultSet results = dc
					.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='"
							+ user.getLogin() + "';");
			results.next();
			areaToDisplay = results.getString("AREANAME");
		} catch (SQLException e) {
			// we are here if the user doesn't set a specific area for himself
			ResultSet results = dc
					.executeQuery("SELECT AREANAME FROM PATHFLUX");
			results.next();
			try {
				areaToDisplay = results.getString("AREANAME");
			} catch (SQLException e2) {
				// If there is no default area, then the admin must configure
				// the portlet before.
			    return new ModelAndView("error", new ModelMap("err", e2.getMessage()));
			}
		}

		try {
			ResultSet favList = dc
					.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='"
							+ user.getLogin() + "';");
			List<Restaurant> favorites = new ArrayList<Restaurant>();

			if (favList.next()) {
				for (Restaurant r : flux.getFlux().getRestaurants()) {

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
		}

		model.put("area", areaToDisplay);

		try {
		    
		    restaurants = flux.getFlux();
		    
			List<Restaurant> dininghallList = new ArrayList<Restaurant>();

			for (Restaurant restaurant : restaurants.getRestaurants()) {
				if (restaurant.getArea().equalsIgnoreCase(areaToDisplay)) {
				    
				    // Check if the restaurant is currently closed
				    // create an additionnal property
				    restaurant.setAdditionalProperties("isClosed", flux.isClosed(restaurant));                  
	                
				    dininghallList.add(restaurant);
				}
			}
			model.put("dininghalls", dininghallList);

		} catch (Exception e) {
            return new ModelAndView("error", new ModelMap("err", e.getMessage()));
		}

		return new ModelAndView("view", model);
	}

	@RequestMapping(params = { "action=viewRestaurant" })
	public ModelAndView renderRestaurantView(RenderRequest request,
			RenderResponse response,
			@RequestParam(value = "id", required = true) int id)
			throws Exception {
		ModelMap model = new ModelMap();

		User user = authenticator.getUser();

		Restaurant restaurant = cache.getCachedRestaurant(id);
		model.put("restaurant", restaurant);
		ResultSet results = dc
				.executeQuery("SELECT * FROM FAVORITERESTAURANT WHERE USERNAME='"
						+ user.getLogin() + "' AND RESTAURANTID='" + id + "';");
		if (results.next()) {
			model.put("isFavorite", true);
		}

		if (flux.isClosed(restaurant)) {
		    model.put("restaurantClosed", true);
		}
		
		return new ModelAndView("restaurant", model);
	}

	@RequestMapping(params = { "action=viewMeals" })
	public ModelAndView renderMealsView(RenderRequest request,
			RenderResponse response,
			@RequestParam(value = "id", required = true) int id)
			throws Exception {

		ModelMap model = new ModelMap();
		User user = authenticator.getUser();

		try {

			ResultSet prefUser = dc
					.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"
							+ user.getLogin() + "';");

			Set<String> nutritionPrefs = new HashSet<String>();

			while (prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}

			model.put("nutritionPrefs", nutritionPrefs);

		} catch (SQLException e) { /**/}

		try {
			Restaurant restaurant = cache.getCachedRestaurant(id);
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
		} catch (Exception e) {
            return new ModelAndView("error", new ModelMap("err", e.getMessage()));
		}

		return new ModelAndView("meals", model);
	}

	@RequestMapping(params = { "action=setFavorite" })
	public void setFavorite(ActionRequest request, ActionResponse response,
			@RequestParam(value = "id", required = true) String id)
			throws Exception {

		User user = authenticator.getUser();

		try {
			dc.executeUpdate("INSERT INTO FAVORITERESTAURANT VALUES ('"
					+ user.getLogin() + "', '" + id + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		response.setRenderParameter("id", id);
		response.setRenderParameter("action", "viewRestaurant");
	}

	@RequestMapping(params = { "action=viewDish" })
	public ModelAndView renderDish(
			RenderRequest request,
			RenderResponse response,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "ingredients", required = false) String ingredients,
			@RequestParam(value = "nutritionitems", required = false) String nutritionitems,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "id", required = true) int id)
			throws Exception {

		User user = authenticator.getUser();
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

			ResultSet prefUser = dc
					.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='"
							+ user.getLogin() + "';");
			Set<String> nutritionPrefs = new HashSet<String>();

			while (prefUser.next()) {
				nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
			}

			model.put("nutritionPrefs", nutritionPrefs);

		} catch (SQLException e) { /**/ }

		return new ModelAndView("dish", model);
	}

	@Required
	@Resource(name = "sessionSetup")
	public void setInitializationServices(final IInitializationService service) {
		this.initializationService = service;
	}

}
