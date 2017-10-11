package com.takeaway.games.resource;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.ManagedAsync;

import com.takeaway.games.exception.GameOfThreeException;
import com.takeaway.games.model.GameOfThree;
import com.takeaway.games.model.GameOfThreeResponse;

@Path("/api/game")
@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public class GameOfThreeResource {
	
	public static int REQUEST_TIME_OUT = 20; // Default
	private static TreeMap<Integer, GameOfThree> gameDB = new TreeMap<Integer, GameOfThree>();
	public static int NO_OF_PLAYERS = 2; // Default
	public AtomicInteger gameIdCounter = new AtomicInteger (0);
	
	/**
	 * Example - http://localhost:8080/api/game/
	 * @param uriInfo
	 * @param asyncResponse
	 */
	@POST
	@ManagedAsync
	public void joinGame(@Context UriInfo uriInfo,
			@Suspended final AsyncResponse asyncResponse) {
		
		setResponseTimeOut(asyncResponse);
		
		GameOfThree currGame;
		int playerId;
		int gameId;
		String gameLink;
		GameOfThreeResponse response;
		
		if (gameDB.isEmpty() || gameDB.get(gameDB.lastKey()).allPlayersJoined()) {
			
			currGame = new GameOfThree(NO_OF_PLAYERS);
			gameId = gameIdCounter.incrementAndGet();
			gameDB.put(gameId, currGame);
			playerId = 1;
			gameLink = constructGameURI(uriInfo, gameId, playerId);
			response = currGame.startNewGame(playerId, gameLink);
			
		} else {
			
			gameId = gameDB.lastKey();
			currGame = gameDB.get(gameDB.lastKey());
			playerId = currGame.joinOneMorePlayer();
			gameLink = constructGameURI(uriInfo, gameId, playerId);
			response = currGame.joinGame(playerId, gameLink);
		}
		
		asyncResponse.resume(response);
	}
	
	/**
	 * Example - http://localhost:8080/api/game/1/player/1?number=3
	 * @param gameId
	 * @param playerId
	 * @param colNum
	 * @param uriInfo
	 * @param asyncResponse
	 * @throws GameOfThreeException
	 */
	@PUT
	@Path("/{gameId}/player/{playerId}")
	@ManagedAsync
	public void play(@PathParam("gameId") int gameId,
			@PathParam("playerId") int playerId,
			@QueryParam("number") int number,
			@Context UriInfo uriInfo,
			@Suspended final AsyncResponse asyncResponse) throws GameOfThreeException{
		
		setResponseTimeOut(asyncResponse);
		
		GameOfThree currGame = gameDB.get(new Integer(gameId));
		String gameLink = constructGameURI(uriInfo, gameId, playerId);
		GameOfThreeResponse response = currGame.play(gameLink, playerId, number);
		
		asyncResponse.resume(response);
	}
	
	/**
	 * Example - http://localhost:8080/api/game/1/player/1
	 * @param gameId
	 * @param playerId
	 * @param uriInfo
	 * @param asyncResponse
	 * @throws GameOfThreeException
	 */
	@GET
	@Path("/{gameId}/player/{playerId}")
	@ManagedAsync
	public void getGameStatus(@PathParam("gameId") int gameId,
			@PathParam("playerId") int playerId,
			@Context UriInfo uriInfo,
			@Suspended final AsyncResponse asyncResponse) throws GameOfThreeException{
		
		setResponseTimeOut(asyncResponse);
		GameOfThree currGame = gameDB.get(new Integer(gameId));
		String gameLink = constructGameURI(uriInfo, gameId, playerId);
		GameOfThreeResponse response = currGame.getStatus(playerId, gameLink);
		
		asyncResponse.resume(response);
	}
	
	private String constructGameURI(final UriInfo uriInfo, final int gameId, final int playerId) {
		
		String gameUri = uriInfo.getBaseUriBuilder()
				.path(GameOfThreeResource.class)
				.path(""+gameId)
				.path("player")
				.path(""+playerId)
				.build()
				.toString();
		
		return gameUri;
	}
	
	private static void setResponseTimeOut(final AsyncResponse asyncResponse) {
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
	                    .entity("Operation time out.").build());
			}
	      
	    });
	    asyncResponse.setTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);
	}
	
}
