package com.rga78.utils.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;

import com.rga78.utils.junit.CaptureSystemOutRule;

/**
 * 
 */
public class TaskArgsTest {
    
    /**
     * This Rule captures test output.  The output is printed if and only if the test fails.
     */
    @Rule
    public CaptureSystemOutRule captureSystemOutRule = new CaptureSystemOutRule();

    /**
     * 
     */
    @Test
    public void testGetStringValue() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "--arg2=arg2.value", 
                                       "--arg3=arg3.value",
                                       "--arg4=",       // value = ""
                                       "--arg5" };      // value = null
        
        TaskArgs argMap = new TaskArgs(args);
        
        assertEquals("arg1.value", argMap.getStringValue("--arg1") );
        assertEquals("arg2.value", argMap.getStringValue("--arg2") );
        assertEquals("arg3.value", argMap.getStringValue("--arg3") );
        assertEquals("", argMap.getStringValue("--arg4") );
        assertNull( argMap.getStringValue("--arg5") );
        assertNull( argMap.getStringValue("--argDoesntExist") );
    }

    /**
     * 
     */
    @Test
    public void testVerifyRequiredString() {
        TaskArgs argMap = new TaskArgs(new String[] { "--arg1=arg1.value" } );
        
        // Argument exists.  all good.
        assertEquals("arg1.value", argMap.getRequiredStringValue("--arg1") );
    }
    
    /**
     * 
     */
    @Test(expected=ArgumentRequiredException.class)
    public void testVerifyRequiredStringNullValue() {
        new TaskArgs(new String[] { "--arg1" } ).getRequiredStringValue("--arg1");
    }
    
    /**
     * 
     */
    @Test(expected=ArgumentRequiredException.class)
    public void testVerifyRequiredStringEmptyValue() {
        new TaskArgs(new String[] { "--arg1=" } ).getRequiredStringValue("--arg1");
    }
    
    /**
     * 
     */
    @Test(expected=ArgumentRequiredException.class)
    public void testVerifyRequiredStringDoesntExist() {
        new TaskArgs(new String[] { "--arg1=blah" } ).getRequiredStringValue("--argDoesntExist");
    }
    
    /**
     * 
     */
    @Test
    public void testGetFileValue() throws Exception {
        String fileName = StringUtils.join( Arrays.asList("files", "test", "test.properties"), File.separator );
        
        File file = new TaskArgs( new String[] { "--propsFile=" + fileName } ).getFileValue("--propsFile");
        
        assertTrue( file.exists() );
        assertEquals( "test.properties", file.getName() );
        
        log("testGetFileValue: " + file.getCanonicalPath() );
        
        assertTrue( file.getCanonicalPath().endsWith( fileName ) );
    }
    
    /**
     * 
     */
    @Test
    public void testGetPropsValue() throws Exception {
        String fileName = StringUtils.join( Arrays.asList("files", "test", "test.properties"), File.separator );
        
        Properties props = new TaskArgs( new String[] { "--propsFile=" + fileName } ).getPropsValue("--propsFile");
        
        assertFalse( props.isEmpty() );
        
        assertEquals( "prop1.value", props.getProperty("--prop1"));
        assertEquals( "prop.3.value", props.getProperty("prop.3"));
    }
    
    /**
     * 
     */
    @Test
    public void testIsSpecified() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "--arg2=arg2.value", 
                                       "--arg3=arg3.value",
                                       "--arg4=",       // value = ""
                                       "--arg5" };      // value = null
        
        TaskArgs argMap = new TaskArgs(args);
        
        assertTrue(argMap.isSpecified("--arg1") );
        assertTrue(argMap.isSpecified("--arg2") );
        assertTrue(argMap.isSpecified("--arg3") );
        assertTrue(argMap.isSpecified("--arg4") );
        assertTrue(argMap.isSpecified("--arg5") );
        assertFalse(argMap.isSpecified("--arg6") );
    }

    
    /**
     * Helper method for tracing.
     */
    private static void log(Object obj) {
        System.out.println("ArgMapTest: " + obj);
    }
    
    
    /**
     * 
     */
    @Test
    public void testValidateExpectedArgs() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "--arg2=arg2.value", 
                                       "--arg3=arg3.value",
                                       "--arg4=",       // value = ""
                                       "--arg5" };      // value = null
        
        TaskArgs taskArgs = new TaskArgs(args);
        
        // No unexpected args means no exception thrown
        taskArgs.validateExpectedArgs( Arrays.asList("--arg1", "--arg2", "--arg3", "--arg4", "--arg5") );
    }
    
    /**
     * 
     */
    @Test
    public void testValidateExpectedArgsMissingHyphen() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "-arg2=arg2.value", 
                                       "--arg3=arg3.value",
                                       "--arg4=",       // value = ""
                                       "--arg5" };      // value = null
        
        TaskArgs taskArgs = new TaskArgs(args);
        
        UnrecognizedArgumentException expectedEx = null;
        try {
            taskArgs.validateExpectedArgs( Arrays.asList("--arg1", "--arg2", "--arg3", "--arg4", "--arg5") );
        } catch (UnrecognizedArgumentException ure) {
            expectedEx = ure;
        }
        
        assertNotNull(expectedEx);
        assertEquals("-arg2", expectedEx.getUnrecognizedArg());
        assertEquals("--arg2", expectedEx.getExpectedArg());
    }
    
    /**
     * 
     */
    @Test(expected=UnrecognizedArgumentException.class)
    public void testValidateExpectedArgsBadArg() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "--arg2=arg2.value", 
                                       "--arg3=arg3.value",
                                       "--arg4=",       // value = ""
                                       "--arg5",         // value = null
                                       "--arg6"};     
        
        TaskArgs taskArgs = new TaskArgs(args);
        taskArgs.validateExpectedArgs( Arrays.asList("--arg1", "--arg2", "--arg3", "--arg4", "--arg5") );
    }
    
    /**
     * 
     */
    @Test
    public void testVerifyStringValue() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "--arg2=arg2.value" 
                                       };     
        
        TaskArgs taskArgs = new TaskArgs(args);
        assertEquals( "arg1.value", taskArgs.verifyStringValue("--arg1", 
                                                               Arrays.asList("blah", "arg1.value"),
                                                               "default") );
        
        assertEquals( "default", taskArgs.verifyStringValue("--arg3", 
                                                            Arrays.asList("blah", "arg3.value"),
                                                            "default") );
    }
    
    /**
     * 
     */
    @Test(expected=InvalidArgumentValueException.class)
    public void testVerifyStringValueInvalid() {
        String[] args = new String[] { "--arg1=arg1.value", 
                                       "--arg2=arg2.value" 
                                       };     
        
        TaskArgs taskArgs = new TaskArgs(args);
        assertEquals( "arg1.value", taskArgs.verifyStringValue("--arg1", 
                                                               Arrays.asList("blah"),
                                                               "default") );
    }
    
    
}
