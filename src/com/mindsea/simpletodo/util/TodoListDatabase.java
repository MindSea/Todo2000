package com.mindsea.simpletodo.util;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mindsea.simpletodo.TodoApplication;

public class TodoListDatabase extends SQLiteOpenHelper {
    private static TodoListDatabase INSTANCE = new TodoListDatabase(TodoApplication.getInstance().getApplicationContext());
    
    public static TodoListDatabase getShared() {
        return INSTANCE;
    }
    
    //
    // Instance methods
    //
    
    private TodoListDatabase(Context context) {
        super(context, "todolists.db", null, 1);
    }
    
    /**
     * Create a todolist database from scratch.
     * 
     * We have one table, todolist, which we use to store a number of todo
     * items.
     * 
     * There are helpers on TodoItem to load TodoItem instances from the
     * database easily: TodoItem.loadTodoItems, TodoItem.loadTodoItemById
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todolist (text TEXT, completed INTEGER, updated_on INTEGER, added_on INTEGER);");
        
        // Create a starter todo list
        for(int i = 0; i < 5; i++) {
            ContentValues cv = new ContentValues();
            cv.put("text", "Item " + (i + 1));
            cv.put("added_on", new Date().getTime() + i);
            cv.put("updated_on", new Date().getTime() + i);
            cv.put("completed", i < 3);
            db.insert("todolist", null, cv);
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
