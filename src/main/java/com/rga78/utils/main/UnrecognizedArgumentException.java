package com.rga78.utils.main;

/**
 * Thrown when a supplied argument is not expected by the task.
 */
public class UnrecognizedArgumentException extends RuntimeException {

    /**
     * The unexpected arg
     */
    private String unrecognizedArg;
    
    /**
     * The expected argument that the user may have meant to specify.
     * This will be filled in only if the unexpectedArg is a close match
     * to the expectedArg (e.g if it's only missing a leading "-").
     */
    private String expectedArg;
    
    public UnrecognizedArgumentException(String unrecognizedArg) {
        this(unrecognizedArg, null);
    }
    
    public UnrecognizedArgumentException(String unrecognizedArg, String expectedArg) {
        this.unrecognizedArg = unrecognizedArg;
        this.expectedArg = expectedArg;
    }

    /**
     * @return expectedArg
     */
    public String getExpectedArg() {
        return expectedArg;
    }

    /**
     * @return unrecognizedArg
     */
    public String getUnrecognizedArg() {
        return unrecognizedArg;
    }
    
}
