package com.rga78.utils.main;

import java.util.List;

public class InvalidArgumentValueException extends IllegalArgumentException {

    private String argName;
    private String invalidValue;
    private List<String> permittedValues;
    
    public InvalidArgumentValueException(String argName, String invalidValue, List<String> permittedValues) {
        
        super( "Invalid argument: " + argName + "=" + invalidValue + ". Permitted values: " + String.valueOf(permittedValues) );
        
        this.argName = argName;
        this.invalidValue = invalidValue;
        this.permittedValues = permittedValues;
    }

    public String getArgName() {
        return argName;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public List<String> getPermittedValues() {
        return permittedValues;
    }
}
