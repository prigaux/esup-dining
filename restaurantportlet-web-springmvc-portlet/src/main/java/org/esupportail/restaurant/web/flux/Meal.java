package org.esupportail.restaurant.web.flux;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Meal {

	private int diningHallId;
	private String type;
	private String date;
	private Map<String, Map> foodCategory;
	
	
	public Meal(JSONObject meal) {
		try {
			this.diningHallId = meal.getInt("dining-hall");
			
			this.date = meal.getString("date");
			
			this.type = meal.getString("type");
			
			this.foodCategory = new HashMap<String, Map>();
			
			JSONArray categoryArray = meal.getJSONArray("food-category");
			
			for(int i=0; i<categoryArray.length(); i++) {
				JSONObject cat = categoryArray.getJSONObject(i);
				Map<String, Object> dishes = new HashMap<String, Object>();
				
				
				JSONArray dishesArray = cat.getJSONArray("dishes");
				for(int j=0; j<dishesArray.length(); i++) {
					System.out.println(dishesArray);
					
					System.out.println("---------------------------");
					
					System.out.println(dishesArray.getJSONObject(i));
					
					/*JSONObject dish = dishesArray.getJSONObject(i);
					dishes.put("name", dish.getString("name"));
					dishes.put("code", dish.getInt("code"));
					dishes.put("ingredients", dish.getString("ingredients"));*/
				}
				
				foodCategory.put(cat.getString("name"), null);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getDiningHallId() {
		return this.diningHallId;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public Map getFoodCategory() {
		return this.foodCategory;
	}
	
}
