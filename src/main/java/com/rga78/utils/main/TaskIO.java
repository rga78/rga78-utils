package com.rga78.utils.main;

import java.io.Console;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskIO {

    /**
     * STDOUT/STDOUT/STDERR streams.
     */
    private Console stdin;
    private PrintStream stdout;
    private PrintStream stderr;
    
    public TaskIO(Console stdin, PrintStream stdout, PrintStream stderr) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public Console getStdin() {
        return stdin;
    }

    public PrintStream getStdout() {
        return stdout;
    }
    
    /**
     * @param message a message to issue to stdout.
     */
    public void info(String message) {
        stdout.println(getTimestamp() + " " + message);
    }
    
    /**
     * @return a timestamp string for messages
     */
    protected String getTimestamp() {
        return new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss.SSS Z]").format( new Date() );
    }
    
}
