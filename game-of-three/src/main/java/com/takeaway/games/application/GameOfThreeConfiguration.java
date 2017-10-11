package com.takeaway.games.application;

import io.dropwizard.Configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Add dropwizard configurations here
 * @author kvenkate
 *
 */
public class GameOfThreeConfiguration extends Configuration {
	
	@NotEmpty
	private String requestTimeOut;
	private int noOfPlayers;

	@JsonProperty
	public int getNoOfPlayers() {
		return noOfPlayers;
	}
	@JsonProperty
	public void setNoOfPlayers(int noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}
	@JsonProperty
	public String getRequestTimeOut() {
		return requestTimeOut;
	}
	@JsonProperty
	public void setRequestTimeOut(String requestTimeOut) {
		this.requestTimeOut = requestTimeOut;
	}
	
}
