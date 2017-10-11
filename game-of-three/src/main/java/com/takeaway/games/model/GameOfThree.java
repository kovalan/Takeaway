package com.takeaway.games.model;

public class GameOfThree {
	
	private int currNumber;
	private final int noOfPlayers;
	private int currPlayerId;
	private int noOfPlayersJoined;
	
	private static final String STATUS_YOU_lOSE = "Game over. You lose. Player %s wins!!!";
	private static final String STATUS_PLAY = "Please play";
	private static final String STATUS_WAIT_FOR_TURN = "Please wait for your turn";
	private static final String STATUS_YOU_WIN = "You win!!!";
	private static final String STATUS_VALID_MOVE = "Valid move.";
	private static final String STATUS_NEW_GAME = "New game started.";
	private static final String STATUS_JOINED_GAME = "Joined a new game.";
	
	public GameOfThree(int noOfPayers) {
		this.noOfPlayers = noOfPayers;
		this.currNumber = 0;
		this.currPlayerId = 1;
		this.noOfPlayersJoined = 1;
	}

	public int getCurrNumber() {
		return currNumber;
	}

	public void setCurrNumber(int currNumber) {
		this.currNumber = currNumber;
	}

	public int getCurrPlayerId() {
		return currPlayerId;
	}
	
	public boolean allPlayersJoined() {
		return this.noOfPlayersJoined == noOfPlayers;
	}
	
	public int joinOneMorePlayer() {
		return ++this.noOfPlayersJoined;
	}

	public void setCurrPlayerId(int currPlayerId) {
		this.currPlayerId = currPlayerId;
	}
	
	public void setNextPlayer() {
		currPlayerId = (currPlayerId % noOfPlayers) +1;
	}
	
	public GameOfThreeResponse startNewGame(int reqPlayerId, String gameLink) {
		
		boolean playNow = this.getCurrPlayerId() == reqPlayerId;
		GameOfThreeResponse response = new GameOfThreeResponse(STATUS_NEW_GAME + STATUS_PLAY, false, gameLink, 0, playNow);
		return response;
	}
	
	public GameOfThreeResponse joinGame(int reqPlayerId, String gameLink) {
		
		boolean gameOverBeforeJoining = this.getCurrNumber()==1;
		boolean playNow = this.getCurrPlayerId() == reqPlayerId;
		GameOfThreeResponse response = new GameOfThreeResponse(STATUS_JOINED_GAME,gameOverBeforeJoining , gameLink, this.getCurrNumber(), playNow);
		return response;
	}
	
	public GameOfThreeResponse getStatus(int reqPlayerId, String gameLink) {
		
		GameOfThreeResponse response = null;
		
		// If the current number is 1, game is over and the player loses
		if (this.getCurrNumber() == 1) {
			response = new GameOfThreeResponse(String.format(STATUS_YOU_lOSE, this.currPlayerId), true, null, this.getCurrNumber(), false);
			return response;
		} 
		
		// If it is the requesting player's turn, return "true" for play flag else return false
		if (this.getCurrPlayerId() == reqPlayerId) {
			
			response = new GameOfThreeResponse(STATUS_PLAY, false, gameLink, this.getCurrNumber(), true);
		} else {
			
			response = new GameOfThreeResponse(STATUS_WAIT_FOR_TURN, false, gameLink, this.getCurrNumber(), false);
		}
		
		return response;
	}
	
	
	public GameOfThreeResponse play(String gameLink, int playerId, int number) {
		
		GameOfThreeResponse response;
		
		// If the returned number is 1 after a move, game is over and the player wins!
		if (number == 1) {
			
			response = new GameOfThreeResponse(STATUS_YOU_WIN, true, gameLink, number, false);
			this.setCurrNumber(1);
		} else {
			
			this.setCurrNumber(number);
			this.setNextPlayer();
			response = new GameOfThreeResponse(STATUS_VALID_MOVE + STATUS_WAIT_FOR_TURN, false, gameLink, number, false);
		}
		
		return response;
		
	}
	
}
