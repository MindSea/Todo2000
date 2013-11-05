package com.mindsea.simpletodo.util;

import com.mindsea.simpletodo.TodoApplication;

public class DatabaseManager {
    private static final DatabaseManager INSTANCE = new DatabaseManager();
    
    public static DatabaseManager getSharedManager() {
        return INSTANCE;
    }
    
    private DatabaseManager() {
        database = new TodoListDatabase(TodoApplication.getInstance().getApplicationContext());
    }
    
    private TodoListDatabase database;
    
    public TodoListDatabase getDatabase() {
        return database;
    }
}
