package com.takeaway.games.application;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.takeaway.games.resource.GameOfThreeResource;

/**
 * 
 * @author Kovalan
 * 
 * Application launcher that starts the server and deploys the resources to the server
 *
 */
public class GameOfThreeServer extends
		Application<GameOfThreeConfiguration> {
	public static void main(String[] args) throws Exception {
		new GameOfThreeServer().run( "server" , "src/main/resources/config.yml");
	}

	@Override
	public void run(GameOfThreeConfiguration config, Environment env)
			throws Exception {
		// message consumer endpoint
		GameOfThreeResource.REQUEST_TIME_OUT = Integer.parseInt(config.getRequestTimeOut());
		GameOfThreeResource.NO_OF_PLAYERS = config.getNoOfPlayers();
		env.jersey().register(GameOfThreeResource.class);
	}
	
	@Override
	public void initialize(Bootstrap<GameOfThreeConfiguration> bootstrap) {
		super.initialize(bootstrap);
	}
	
}
