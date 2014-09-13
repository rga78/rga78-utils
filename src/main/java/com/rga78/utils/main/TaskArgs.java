package com.rga78.utils.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;


/**
 * Helper class for parsing command-line arguments.
 * 
 * Arguments are specified via <name>=<value> pairs.
 * No-value arguments are specified with just their name: <name>
 * 
 */
public class TaskArgs extends HashMap<String, Object> {
    
    /**
     * CTOR.
     */
    public TaskArgs(String[] args) {
        parseArgs(args);
    }
    
    /**
     * @return a Map of argName=argValue pairs.
     */
    protected Map<String, Object> parseArgs(String[] args) {
        
        for (String arg : args) {
            put( parseArgName(arg), parseArgValue(arg) );
        }
        
        return this;
    }
    
    /**
     * @return the arg name (e.g. "--argName=argValue" returns "--argName")
     */
    protected String parseArgName(String arg) {
        int idx = arg.indexOf("=");
        return (idx >= 0) ? arg.substring(0, idx) : arg;
    }
    
    /**
     * @return the arg value (e.g. "--argName=argValue" returns "argValue"),
     *         or null if the argument doesn't have a value.
     */
    protected String parseArgValue(String arg) {
        int idx = arg.indexOf("=");
        return (idx >= 0) ? arg.substring(idx + 1) : null;
    }
    
    /**
     * @return the value associated with the given arg.
     */
    public String getStringValue(String argName) {
        return (String) get(argName);
    }
    
    /**
     * @return the long value associated with the given arg
     */
    public Long getLongValue(String argName, Long defaultValue) {
        String val = getStringValue(argName);
        return ( val != null) ? new Long( val ) : defaultValue;
    }

    /**
     * @return the long value associated with the given arg
     */
    public Integer getIntValue(String argName, Integer defaultValue) {
        String val = getStringValue(argName);
        return ( val != null) ? new Integer( val ) : defaultValue;
    }
    
    /**
     * @return true if the given argName was specified (i.e exists in the map).
     */
    public boolean isSpecified(String argName) {
        return containsKey(argName);
    }
    
    /**
     * @return the value associated with the given arg.
     * 
     * @throws IllegalArgumentException if the value is null or empty.
     */
    public String getRequiredStringValue(String argName) {
        String retMe = getStringValue(argName);
        
        if ( StringUtils.isEmpty(retMe) ) {
            throw new ArgumentRequiredException(argName);
        }
        
        return retMe;
    }
    
    /**
     * @return the given key value as a File, or null if the key doesn't exist.
     */
    public File getFileValue( String key ) {
        
        String fileName = getStringValue(key);
        
        return (StringUtils.isEmpty(fileName)) ? null : new File(fileName);
    }
    
    /**
     * 
     * @return the given key value as a Properties object.  The properties are read from
     *         the fileName associated with the given key.  If the key does not exist,
     *         an empty Properties object is returned.
     */
    public Properties getPropsValue( String key ) throws IOException {
        
        Properties retMe = new Properties();
        
        File propsFile = getFileValue( key );
        
        if (propsFile != null) {
            InputStreamReader isr = new InputStreamReader( new FileInputStream(propsFile), Charset.forName("ISO-8859-1"));
            try {
                retMe.load( isr );
            } finally {
                isr.close();
            }
        }
        
        return retMe;
    }
          
    /**
     * Check the given set of expected args against the actual args.
     * 
     * @throws UnrecognizedArgumentException if an argument is not in the list of expectedArgs.
     */
    public TaskArgs validateExpectedArgs(Collection<String> expectedArgs) {
        for (String key : keySet() ) {
            if (! expectedArgs.contains(key) ) {
                handleUnexpectedArg(expectedArgs, key);
            }
        }
        return this;
    }
    
    protected void handleUnexpectedArg(Collection<String> expectedArgs, String unexpectedArg) {
        // Check specifically if the arg has only 1 leading dash (when it should have 2)
        if ( expectedArgs.contains( "-" + unexpectedArg ) ) {
            throw new UnrecognizedArgumentException( unexpectedArg, "-" + unexpectedArg );
        } else {
            throw new UnrecognizedArgumentException( unexpectedArg );
        }
    }
    
    /**
     * Verify the value of the given argName is in the list of permittedValues.
     * 
     * If the value is empty or null, return the defaultValue.
     * 
     * @param argName the arg name
     * @param permittedValues permitted values for this arg
     * @param defaultValue returned if arg value is empty
     * 
     * @return the arg value, if permitted, or defaultValue if arg value is empty
     * 
     * @throws InvalidArgumentValueException
     */
    public String verifyStringValue(String argName, List<String> permittedValues, String defaultValue) {
        String argValue = getStringValue(argName);
        if (StringUtils.isEmpty(argValue)) {
            return defaultValue;
        } else if (permittedValues.contains(argValue)) {
            return argValue;
        } else {
            throw new InvalidArgumentValueException(argName, argValue, permittedValues);
        }
    }
    
}
