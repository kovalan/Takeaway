package com.takeaway.games.client;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GameOfThreeClient {
	
  private static final Client client = Client.create();
  private static final Random random = new Random();
  private static final String CLIENT_SENDING_NUMBER = "The sending number is %s.";
  private static final String CLIENT_ADDED_NUMBER = "The added number is %s.";

  public static void main(String[] args) {
	  
	  boolean manualMode = isManualMode(args);
	  if (manualMode) {
		  playInManualMode();
	  } else {
		  playInAutoMode();
	  }
  }
  
  private static void playInAutoMode() {
	  
	  try {
			// Start a new game or join an existing game as new player.
			GameOfThreeResponse response = joinGame();
			String gameLink = response.getGameLink();
			
			while(true) {
				
				// Exit when game is over
				if (response.isGameOver()) {
					System.out.println(response.getMessage());
					break;
				}
				
				// Check for the current status
				response = pollGame(gameLink);
				if (!response.isPlay()) {
					Thread.sleep(1000);
					continue;
				}
					
				long next;
				// Send a random number as the first input.
				if(response.getNumber() == 0) {
					
					next = random.nextInt(Integer.MAX_VALUE-1) + 1;
					System.out.println(String.format(CLIENT_SENDING_NUMBER, next));
					
				} else {
					
					int givenNumber = response.getNumber();
					next = Math.round(((double)givenNumber)/ 3);
					
					System.out.println(String.format(CLIENT_ADDED_NUMBER, next*3 - givenNumber));
					System.out.println(String.format(CLIENT_SENDING_NUMBER, next));
				}
				
				response = playGame(response.getGameLink() + "?number="+next);
			}

		  } catch (Exception e) {

			e.printStackTrace();
		  }
	  
  }
  
  private static void playInManualMode() {
	  
	  try {
			
			GameOfThreeResponse response = joinGame();
			String gameLink = response.getGameLink();
			Scanner in = new Scanner(System.in);
			long next;
			
			while(true) {
				
				if (response.isGameOver()) {
					System.out.println(response.getMessage());
					break;
				}
				response = pollGame(gameLink);
				
				if (!response.isPlay()) {
					Thread.sleep(1000);
					continue;
				}
				
				if(response.getNumber() == 0) {
					
					System.out.println("Please enter a whole number to begin: ");
					next = in.nextLong();
					
					System.out.println(String.format(CLIENT_SENDING_NUMBER, next));
				} else {
					
					int givenNumber = response.getNumber();
					next = Math.round(((double)givenNumber)/ 3);
					
					int userInput;
					
					do {
						
						System.out.println("Received number is "+givenNumber +". Please select the appropriate number between -1,0,1: ");
						userInput = in.nextInt();
						if (userInput != (next*3 - givenNumber)) {
							
							System.out.println("Wrong input");
						}
					} while (userInput != (next*3 - givenNumber));
					
					System.out.println(String.format(CLIENT_ADDED_NUMBER, next*3 - givenNumber));
					System.out.println(String.format(CLIENT_SENDING_NUMBER, next));
				}
				response = playGame(response.getGameLink() + "?number="+next);
			}
			
			in.close();

		  } catch (Exception e) {
			e.printStackTrace();
		  }
  }
  
  private static boolean isManualMode (String[] args) {
	  
	  if (args == null || args.length == 0) {
		  return false;
	  }
	  return Boolean.valueOf(args[0]);
	  
  }
  
  private static GameOfThreeResponse joinGame() throws JsonParseException, JsonMappingException, IOException {

	  WebResource webResource = client
			   .resource("http://localhost:9000/api/game");
	  ClientResponse response = webResource.accept("application/json")
	           .post(ClientResponse.class);
	  if (response.getStatus() != 200) {
		  throw new RuntimeException("Failed : HTTP error code : "
				  + response.getStatus());
	  }

	  String output = response.getEntity(String.class);
	  ObjectMapper mapper = new ObjectMapper();
	  GameOfThreeResponse threeResponse = mapper.readValue(output, GameOfThreeResponse.class);
			
	  return threeResponse;
  }
  
  private static GameOfThreeResponse pollGame(String uri) throws JsonParseException, JsonMappingException, IOException {
	  
	  WebResource webResource = client
			  .resource(uri);
	  ClientResponse response = webResource.accept("application/json")
           .get(ClientResponse.class);
	  if (response.getStatus() != 200) {
		  throw new RuntimeException("Failed : HTTP error code : "
				  + response.getStatus());
	  }
	  String output = response.getEntity(String.class);
	  GameOfThreeResponse threeResponse = null;

	  if (output != null && !output.isEmpty()) {
		  ObjectMapper mapper = new ObjectMapper();
		  threeResponse = mapper.readValue(output, GameOfThreeResponse.class);
	  }
	  return threeResponse;
  }
  
  private static GameOfThreeResponse playGame(String uri) throws JsonParseException, JsonMappingException, IOException {
	  
	  WebResource webResource = client
			  .resource(uri);
	  ClientResponse response = webResource.accept("application/json")
			  .put(ClientResponse.class);
	  if (response.getStatus() != 200) {
		  throw new RuntimeException("Failed : HTTP error code : "
	  + response.getStatus());
	  }

	  String output = response.getEntity(String.class);
	  ObjectMapper mapper = new ObjectMapper();
	  GameOfThreeResponse threeResponse = mapper.readValue(output, GameOfThreeResponse.class);
	  return threeResponse;
  }
}