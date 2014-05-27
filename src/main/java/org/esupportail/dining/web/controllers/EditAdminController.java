package org.esupportail.dining.web.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("EDIT")
public class EditAdminController extends AbstractExceptionController {

	@Autowired
	private Authenticator authenticator;
	@Autowired
	private DatabaseConnector dc;
	@Autowired
	private DiningFeed feed;

	@RequestMapping(params = { "action=adminSettings" })
	public ModelAndView renderEditAdminView(RenderRequest request,
			RenderResponse response) throws Exception {

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

		String urlflux = null;
		String[] areanames = null;
		ResultSet results = null;
		try {
			results = this.dc.executeQuery("SELECT URLFLUX, AREANAME FROM PATHFLUX");
			results.next();
		} catch (SQLException e) {
			// URL isn't set yet...
		}

		try {
			areanames = results.getString("AREANAME").split(",");
		} catch (Exception e) {
			// URL may be set be default area is not
		}
		try {
			urlflux = results.getString("URLFLUX");
		} catch (Exception e) {
			// same issue as before
		}

		model.put("urlfluxdb", urlflux);
		model.put("defaultArea", areanames);

		// } catch (NullPointerException e) { e.printStackTrace(); }

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

	@RequestMapping(params = { "action=adminStats" })
	public ModelAndView renderStatsAdminView(RenderRequest request,
			RenderResponse response) throws Exception {

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
	public void setURLFeed(ActionRequest request, ActionResponse response,
			@RequestParam(value = "url", required = true) String url)
					throws Exception {
		response.setRenderParameter("action", "adminSettings");
		try {
			URL urlFeed = new URL(url);
			this.feed.setPath(urlFeed);
			response.setRenderParameter("urlError", "false");
			// If URL is correct, then we can insert this into the database.
			ResultSet results = this.dc.executeQuery("SELECT * FROM PATHFLUX");
			results.next();
			results.updateString("URLFLUX", url);
			results.updateRow();
		} catch (SQLException e) {
			// We get a SQLException if the row doesn't exist in the table
			this.dc.executeUpdate("INSERT INTO PATHFLUX (URLFLUX) VALUES ('" + url
					+ "')");
		} catch (Exception e) {
			response.setRenderParameter("urlError", "true");
		}
	}

	@RequestMapping(params = { "action=setDefaultArea" })
	public void setDefaultArea(
			ActionRequest request,
			ActionResponse response,
			@RequestParam(value = "chkArea[]", required = false) String[] listAreas)
					throws Exception {
		String areanames = "";
		if (listAreas != null) {
			for (int i = 0; i < listAreas.length; i++) {
				areanames += listAreas[i]
						+ (i < listAreas.length - 1 ? "," : "");
			}
		}

		response.setRenderParameter("action", "adminSettings");
		response.setRenderParameter("areaSubmit", "true");

		ResultSet results = this.dc.executeQuery("SELECT * FROM PATHFLUX");
		results.next();
		results.updateString("AREANAME", areanames);
		results.updateRow();
	}

	@RequestMapping(params = { "action=forceFeedUpdate" })
	public void feedUpdate(ActionRequest request, ActionResponse response)
			throws Exception {

		Boolean isUpdated = this.feed.update();

		response.setRenderParameter("action", "adminSettings");
		response.setRenderParameter("update", isUpdated.toString());
	}
}
