package com.mindsea.simpletodo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoItem {
    public static List<TodoItem> load(Cursor c) {
        
        ArrayList<TodoItem> items = new ArrayList<TodoItem>(c.getCount());
        
        c.moveToFirst();
        
        while (!c.isAfterLast()) {
            final long rowId = c.getLong(c.getColumnIndex("rowid"));
            final String text = c.getString(c.getColumnIndex("text"));
            final Date updated = new Date(c.getLong(c.getColumnIndex("updated_on")));
            final Date added = new Date(c.getLong(c.getColumnIndex("added_on")));
            final boolean completed = (c.getInt(c.getColumnIndex("completed")) != 0);
            
            items.add(new TodoItem(rowId, text, updated, added, completed));
            
            c.moveToNext();
        }
        
        return items;
    }
    
    private Long rowId;
    private String text;
    private Date updated;
    private Date added;
    private boolean completed;
    
    private TodoItem(Long rowId, String text, Date updated, Date added, boolean completed) {
        super();
        this.rowId = rowId;
        this.text = text;
        this.updated = updated;
        this.added = added;
        this.completed = completed;
    }

    public TodoItem(String text) {
        super();
        this.updated = new Date();
        this.added = new Date();
        this.completed = false;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getAdded() {
        return added;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public void commit(final SQLiteDatabase db) {
        updated = new Date();
        
        ContentValues cv = new ContentValues();
        cv.put("text", text);
        cv.put("updated_on", updated.getTime());
        cv.put("completed", completed ? 1 : 0);
        
        if (rowId == null) {
            added = new Date();
            cv.put("added_on", added.getTime());
            rowId = db.insert("todolist", null, cv);
        } else {
            db.update("todolist", cv, "rowid = ?", new String[] { rowId.toString() });
        }
    }
}
