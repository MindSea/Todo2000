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
