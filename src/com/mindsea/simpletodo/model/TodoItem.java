package com.mindsea.simpletodo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoItem {
    //
    // Public helpers for loading items from database 
    //
    
    /**
     * Load a list of todo items from the SQLite database using the supplied
     * Cursor.
     * 
     * @param c
     *            A Cursor instance for a query performed on the todolist table.
     * @return A list of TodoItem instances loaded from the todolist table.
     */
    public static List<TodoItem> loadFromCursor(Cursor c) {
        
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
    
    /**
     * Query the todolist database for items and return them as a list.
     * 
     * @param database
     *            The database containing the todolist table.
     * @return A list of TodoItem instances loaded from the todolist table.
     */
    public static List<TodoItem> loadTodoItems(SQLiteDatabase database) {
        final Cursor todoListCursor = database.query("todolist", new String[] {"ROWID", "text", "updated_on", "added_on", "completed"}, null, null, null, null, null);
        final List<TodoItem> todos = loadFromCursor(todoListCursor);
        todoListCursor.close();
        return todos;
    }
    
    /**
     * Query the todolist database for a specific todo item and return it.
     * 
     * @param database
     *            The database containing the todolist table.
     * @param rowId
     *            The rowid of the desired todo item.
     * @return A TodoItem instance for the specified row.
     */
    public static TodoItem loadTodoItemById(SQLiteDatabase database, long rowId) {
        final Cursor todoListCursor = database.query("todolist", new String[] {"ROWID", "text", "updated_on", "added_on", "completed"}, "ROWID = ?", new String[] { Long.toString(rowId) }, null, null, null);
        final List<TodoItem> todos = loadFromCursor(todoListCursor);
        todoListCursor.close();
        return todos.get(0);
    }
    
    //
    
    //
    // Fields for a TodoItem record.
    //
    // The rowId is a Long to allow null, which indicates that the item has
    // no rowId because it has not yet been recorded in the database.  When the
    // item is written to the database using commit(), a rowId will be assigned.
    //
    private Long rowId;
    private String text;
    private Date updated;
    private Date added;
    private boolean completed;
    
    /**
     * Private constructor for creating a TodoItem from the database. Use
     * loadTodoItems or loadTodoItemById to create TodoItem instances from
     * database records.
     */
    private TodoItem(Long rowId, String text, Date updated, Date added, boolean completed) {
        super();
        this.rowId = rowId;
        this.text = text;
        this.updated = updated;
        this.added = added;
        this.completed = completed;
    }
    
    /**
     * Create a new TodoItem with no rowId. A rowId will be generated when the
     * TodoItem is first written to the database using commit().
     */
    public TodoItem(String text) {
        super();
        this.updated = new Date();
        this.added = new Date();
        this.completed = false;
        this.text = text;
    }

    //
    // Simple getters and setters.
    //
    
    public Long getRowId() {
        return rowId;
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
    
    /**
     * Write the TodoItem to the specified database. If the TodoItem has no
     * rowId, a new record will be created and the rowId will be assigned to
     * this TodoItem.
     * 
     * @param db
     *            The database where the TodoItem should be saved.
     */
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
