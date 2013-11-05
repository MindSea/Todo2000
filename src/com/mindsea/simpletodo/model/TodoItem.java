package com.mindsea.simpletodo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

public class TodoItem {
    public static List<TodoItem> load(Cursor c) {
        
        ArrayList<TodoItem> items = new ArrayList<TodoItem>(c.getCount());
        
        c.moveToFirst();
        
        while (!c.isAfterLast()) {
            final long rowId = c.getLong(c.getColumnIndex("rowid"));
            final String text = c.getString(c.getColumnIndex("text"));
            final Date updated = new Date(c.getLong(c.getColumnIndex("updated")));
            final boolean completed = (c.getInt(c.getColumnIndex("completed")) != 0);
            
            items.add(new TodoItem(rowId, text, updated, completed));
            
            c.moveToNext();
        }
        
        return items;
    }
    
    private long rowId;
    private String text;
    private Date updated;
    private boolean completed;
    
    public TodoItem(long rowId, String text, Date updated, boolean completed) {
        super();
        this.rowId = rowId;
        this.text = text;
        this.updated = updated;
        this.completed = completed;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
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

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    
}
