package org.esupportail.dining.web.feed;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FeedResponse {

	@JsonProperty("results")
	private List<FeedInformation> feedInformationList;

	public List<FeedInformation> getFeedInformationList() {
		return feedInformationList;
	}

	public void setFeedInformationList(List<FeedInformation> feedInformationList) {
		this.feedInformationList = feedInformationList;
	}
	
	
}
