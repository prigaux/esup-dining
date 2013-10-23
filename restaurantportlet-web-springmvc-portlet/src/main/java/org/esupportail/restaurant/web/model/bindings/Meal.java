
package org.esupportail.restaurant.web.model.bindings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "date",
    "food-category",
    "dining-hall",
    "type"
})
public class Meal {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    private String date;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("food-category")
    private List<Food_category> food_category = new ArrayList<Food_category>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dining-hall")
    private Integer dining_hall;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("type")
    private String type;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("food-category")
    public List<Food_category> getFood_category() {
        return food_category;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("food-category")
    public void setFood_category(List<Food_category> food_category) {
        this.food_category = food_category;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dining-hall")
    public Integer getDining_hall() {
        return dining_hall;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dining-hall")
    public void setDining_hall(Integer dining_hall) {
        this.dining_hall = dining_hall;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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
