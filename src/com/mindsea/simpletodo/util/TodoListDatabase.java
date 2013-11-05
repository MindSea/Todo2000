package com.mindsea.simpletodo.util;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListDatabase extends SQLiteOpenHelper {
    
    TodoListDatabase(Context context) {
        super(context, "todolists.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todolist (text TEXT, updated INTEGER, completed INTEGER);");
        
        // Create a starter todo list
        for(int i = 0; i < 5; i++) {
            ContentValues cv = new ContentValues();
            cv.put("text", "Item " + i);
            cv.put("updated", new Date().getTime());
            cv.put("completed", i < 3);
            db.insert("todolist", null, cv);
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
