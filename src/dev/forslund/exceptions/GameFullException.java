package dev.forslund.exceptions;

public class GameFullException extends Exception {
    /**
     * Exception that handles full games.
     * @param message message that is given.
     */
    public GameFullException(String message) {
        super(message);
    }
}
