package org.esupportail.dining.web.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({ "name", "value" })
public class Nutritionitem {

	/**
     * 
     */
	@JsonProperty("name")
	private String name;
	/**
     * 
     */
	@JsonProperty("value")
	private Double value;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
     * 
     */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
     * 
     */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
     * 
     */
	@JsonProperty("value")
	public Double getValue() {
		return value;
	}

	/**
     * 
     */
	@JsonProperty("value")
	public void setValue(Double value) {
		this.value = value;
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
