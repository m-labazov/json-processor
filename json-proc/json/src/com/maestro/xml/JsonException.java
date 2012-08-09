package com.maestro.xml;

/**
 * The JSONException is thrown by the JSON.org classes then things are amiss.
 *
 * @author JSON.org
 * @version 3
 */
@SuppressWarnings("serial")
public class JsonException extends Exception {
    private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     *
     * @param message Detail about the reason for the exception.
     */
    public JsonException(String message) {
        super(message);
    }
    
    public JsonException(String message, Throwable cause) {
    	super(message);
    	this.cause = cause;
    }

    public JsonException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }
}