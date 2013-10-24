
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
    "date",
    "foodcategory",
    "dininghall",
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
    @JsonProperty("foodcategory")
    private List<Foodcategory> foodcategory = new ArrayList<Foodcategory>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dininghall")
    private Integer dininghall;
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
    @JsonProperty("foodcategory")
    public List<Foodcategory> getFoodcategory() {
        return foodcategory;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("foodcategory")
    public void setFoodcategory(List<Foodcategory> foodcategory) {
        this.foodcategory = foodcategory;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dininghall")
    public Integer getDininghall() {
        return dininghall;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dininghall")
    public void setDininghall(Integer dininghall) {
        this.dininghall = dininghall;
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
