package com.rga78.utils.main;

public class ArgumentRequiredException extends RuntimeException {

    public ArgumentRequiredException(String argName) {
        super("Missing required argument: " + argName);
    }
   
}
