
package org.esupportail.restaurant.web.model.bindings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "code",
    "ingredients",
    "name",
    "nutrition-items"
})
public class Dish {

    /**
     * 
     */
    @JsonProperty("code")
    private Integer code;
    /**
     * 
     */
    @JsonProperty("ingredients")
    private String ingredients;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     */
    @JsonProperty("nutrition-items")
    private List<Nutrition_item> nutrition_items = new ArrayList<Nutrition_item>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    /**
     * 
     */
    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 
     */
    @JsonProperty("ingredients")
    public String getIngredients() {
        return ingredients;
    }

    /**
     * 
     */
    @JsonProperty("ingredients")
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     */
    @JsonProperty("nutrition-items")
    public List<Nutrition_item> getNutrition_items() {
        return nutrition_items;
    }

    /**
     * 
     */
    @JsonProperty("nutrition-items")
    public void setNutrition_items(List<Nutrition_item> nutrition_items) {
        this.nutrition_items = nutrition_items;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
