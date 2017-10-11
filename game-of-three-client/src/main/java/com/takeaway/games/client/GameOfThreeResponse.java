package com.takeaway.games.client;

public class GameOfThreeResponse {
	
	private String message;
	private int number;
	private boolean gameOver;
	private String gameLink;
	private boolean play;
	

	public GameOfThreeResponse() {
		
	}
	
	public GameOfThreeResponse(String msg, boolean gameOver, String gameLink, int number, boolean play) {
		
		this.message = msg;
		this.gameOver = gameOver;
		this.gameLink = gameLink;
		this.number = number;
		this.play = play;
	}
	
	public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public String getGameLink() {
		return gameLink;
	}

	public void setGameLink(String gameLink) {
		this.gameLink = gameLink;
	}
	
	
	
}
