package com.rga78.utils.main.tasks;

import com.rga78.utils.main.Task;
import com.rga78.utils.main.TaskArgs;
import com.rga78.utils.main.TaskIO;



/**
 * Utility task for dropping a collection or an entire db.
 * 
 */
public class ExampleTask extends Task<ExampleTask> {

    @Override
    public String getTaskName() {
        return "example";
    }

    @Override
    public String getTaskHelp() {
        return getTaskName() + " [ usage statement ]";
    }

    @Override
    public String getTaskDescription() {
        return " TODO: description ";
    }

    @Override
    public int handleTask(TaskIO taskIO, String[] args) throws Exception {
        
        TaskArgs taskArgs = new TaskArgs(args);
        
        // TODO do something...
        
        return 0;
    }

    
}
