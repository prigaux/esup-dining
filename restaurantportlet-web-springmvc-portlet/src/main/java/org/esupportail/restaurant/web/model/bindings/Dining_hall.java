
package org.esupportail.restaurant.web.model.bindings;

import java.util.HashMap;
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
    "area",
    "closing",
    "contact",
    "id",
    "infos",
    "lat",
    "lon",
    "opening",
    "short-desc",
    "title"
})
public class Dining_hall {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("area")
    private String area;
    /**
     * 
     */
    @JsonProperty("closing")
    private String closing;
    /**
     * 
     */
    @JsonProperty("contact")
    private String contact;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private Integer id;
    /**
     * 
     */
    @JsonProperty("infos")
    private String infos;
    /**
     * 
     */
    @JsonProperty("lat")
    private Double lat;
    /**
     * 
     */
    @JsonProperty("lon")
    private Double lon;
    /**
     * 
     */
    @JsonProperty("opening")
    private String opening;
    /**
     * 
     */
    @JsonProperty("short-desc")
    private String short_desc;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    private String title;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("area")
    public String getArea() {
        return area;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("area")
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 
     */
    @JsonProperty("closing")
    public String getClosing() {
        return closing;
    }

    /**
     * 
     */
    @JsonProperty("closing")
    public void setClosing(String closing) {
        this.closing = closing;
    }

    /**
     * 
     */
    @JsonProperty("contact")
    public String getContact() {
        return contact;
    }

    /**
     * 
     */
    @JsonProperty("contact")
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    @JsonProperty("infos")
    public String getInfos() {
        return infos;
    }

    /**
     * 
     */
    @JsonProperty("infos")
    public void setInfos(String infos) {
        this.infos = infos;
    }

    /**
     * 
     */
    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    /**
     * 
     */
    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * 
     */
    @JsonProperty("lon")
    public Double getLon() {
        return lon;
    }

    /**
     * 
     */
    @JsonProperty("lon")
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * 
     */
    @JsonProperty("opening")
    public String getOpening() {
        return opening;
    }

    /**
     * 
     */
    @JsonProperty("opening")
    public void setOpening(String opening) {
        this.opening = opening;
    }

    /**
     * 
     */
    @JsonProperty("short-desc")
    public String getShort_desc() {
        return short_desc;
    }

    /**
     * 
     */
    @JsonProperty("short-desc")
    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
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
