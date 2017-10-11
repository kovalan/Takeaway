package com.takeaway.games.exception;

public class GameOfThreeException  extends Throwable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8925422122328163153L;

	public GameOfThreeException(final String msg) {
        super(msg);
    }

    public GameOfThreeException(final String msg, final Throwable ex) {
        super(msg, ex);
    }

    public GameOfThreeException(final Throwable ex) {
        super(ex);
    }
}