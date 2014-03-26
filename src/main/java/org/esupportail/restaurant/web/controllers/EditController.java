package org.esupportail.restaurant.web.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.esupportail.restaurant.web.domain.beans.User;
import org.esupportail.restaurant.web.domainservices.services.auth.Authenticator;
import org.esupportail.restaurant.web.feed.RestaurantFeed;
import org.esupportail.restaurant.web.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("EDIT")
public class EditController extends AbstractExceptionController {

    @Autowired
    private Authenticator authenticator;
    @Autowired
    private DatabaseConnector dc;
    @Autowired
    private RestaurantFeed flux;

    @RequestMapping
    public ModelAndView renderEditView(RenderRequest request, RenderResponse response) throws Exception {

        ModelMap model = new ModelMap();

        User user = authenticator.getUser();
        model.put("user", user);

        int[] code = {1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15};
        model.put("nutritionCodes", code);

        try {

            ResultSet prefUser = dc.executeQuery("SELECT NUTRITIONCODE FROM nutritionPreferences WHERE USERNAME='" + user.getLogin() + "';");

            Set<String> nutritionPrefs = new HashSet<String>();

            while (prefUser.next()) {
                nutritionPrefs.add(prefUser.getString("NUTRITIONCODE"));
            }

            model.put("nutritionPrefs", nutritionPrefs);

            prefUser.close();
        } catch (SQLException e) { /**/ }

        try {
            Set<String> areaList = new HashSet<String>();
            for (Restaurant r : flux.getFlux().getRestaurants()) {
                areaList.add(r.getArea());
            }

            model.put("areaList", areaList);

            String userArea[] = null;
            try {
                ResultSet results = dc.executeQuery("SELECT AREANAME FROM USERAREA WHERE USERNAME='" + user.getLogin() + "';");
                results.next();
                userArea = results.getString("AREANAME").split(",");
                results.close();
            } catch (SQLException e) {
                // here we are if the user doesn't already have a specific area setting.
                ResultSet results = dc.executeQuery("SELECT AREANAME FROM PATHFLUX");
                results.next();
                try {
                    userArea = results.getString("AREANAME").split(",");
                } catch (SQLException e2) {
                    // No default area for all user, admin must configure the portlet. 
                    // No need to throw an exception
                }
                results.close();

            }
            model.put("defaultArea", userArea);

            Set<String> favResults = new HashSet<String>();
            try {
                ResultSet results = dc.executeQuery("SELECT RESTAURANTID FROM FAVORITERESTAURANT WHERE USERNAME='" + user.getLogin() + "'");
                while (results.next()) {
                    favResults.add(results.getString("restaurantId"));
                }
                results.close();
            } catch (Exception e) {
                // No data available, doesn't matter 
            }
            model.put("favList", favResults);

            List<Restaurant> listRestaurant = flux.getFlux().getRestaurants();
            List<Restaurant> listFavRestaurant = new ArrayList<Restaurant>();

            for (Restaurant r : listRestaurant) {
                for (String favId : favResults) {
                    if (r.getId() == Integer.parseInt(favId, 10)) {
                        listFavRestaurant.add(r);
                    }
                }
            }

            model.put("listFavRestaurant", listFavRestaurant);

        } catch (NullPointerException e) { /**/ }

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

    @RequestMapping(value = {"EDIT"}, params = {"action=nutritionPreferences"})
    public void setUserNutritionPreferences(ActionRequest request, ActionResponse response) throws Exception {

        String userLogin = authenticator.getUser().getLogin();

        Map parameters = request.getParameterMap();


        int[] code = {1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15};

        for (int i = 0; i < code.length; i++) {

            if (parameters.get("code-" + code[i]) != null) {

                try {
                    dc.executeUpdate("INSERT INTO nutritionPreferences (USERNAME, NUTRITIONCODE) VALUES ('" + userLogin + "', '" + code[i] + "');");
                } catch (SQLException e) { /**/ }

            } else {
                /* uncheck boxes need to be deleted from the db
                 if it goes into the catch it mean the row didn't exist and it doesn't need specific treatment */
                try {
                    dc.executeUpdate("DELETE FROM nutritionPreferences WHERE USERNAME='" + userLogin + "' AND  NUTRITIONCODE='" + code[i] + "';");
                } catch (SQLException e) { /**/ }

            }
        }

        response.setRenderParameter("nutritSubmit", "true");
    }

    @RequestMapping(params = {"action=setUserArea"})
    public void setUserArea(ActionRequest request, ActionResponse response, @RequestParam(value = "chkArea[]", required = false) String[] listAreas) throws Exception {

        User user = authenticator.getUser();

        String areanames = "";

        if (listAreas != null) {

            for (int i = 0; i < listAreas.length; i++) {
                areanames += listAreas[i] + (i < listAreas.length - 1 ? "," : "");
            }


            try {
                ResultSet results = dc.executeQuery("SELECT * FROM USERAREA WHERE USERNAME='" + user.getLogin() + "';");
                results.next();
                results.updateString("AREANAME", areanames);
                results.updateRow();
                results.close();
            } catch (SQLException e) {
                dc.executeUpdate("INSERT INTO USERAREA (USERNAME, AREANAME) VALUES ('" + user.getLogin() + "', '" + areanames + "');");
            }

            response.setRenderParameter("zoneSubmit", "true");
        }
    }
}
