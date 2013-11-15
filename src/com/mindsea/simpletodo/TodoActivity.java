package com.mindsea.simpletodo;

import java.text.DateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mindsea.simpletodo.model.TodoItem;
import com.mindsea.simpletodo.util.TodoListDatabase;

public class TodoActivity extends Activity {
    
    public static final String INTENT_ARG_TODO_ROWID = TodoActivity.class.getName() + ".INTENT_ARG_TODO_ROWID";
    
    /**
     * Construct an Intent to launch this activity for a specific todo item.
     * 
     * @param c Context for the newly-created Intent.
     * @param todoRowId Database ROWID for the todo list item that we will be editing.
     * @return An Intent that can be used to launch TodoActivity to edit the specified item.
     */
    public static Intent newStartIntent(final Context c, final long todoRowId) {
        final Intent startIntent = new Intent(c, TodoActivity.class);
        startIntent.putExtra(INTENT_ARG_TODO_ROWID, todoRowId);
        return startIntent;
    }
    
    //
    
    private TodoItem selectedTodo;
    private SQLiteDatabase database;

    /**
     * Get the row ID parameter from the Intent used to start this activity.
     * 
     * @return The row ID of the todo item that this activity is viewing.
     */
    private long getRowId() {
        return getIntent().getLongExtra(INTENT_ARG_TODO_ROWID, -1);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        // Load todo from database
        database = TodoListDatabase.getShared().getWritableDatabase();
        final Cursor todoListCursor = database.query("todolist", new String[] {"ROWID", "text", "updated_on", "added_on", "completed"}, "ROWID = ?", new String[] { Long.toString(getRowId()) }, null, null, null);
        selectedTodo = TodoItem.loadFromCursor(todoListCursor).get(0);
        
        // Set listeners
        final CheckBox completedView = (CheckBox)findViewById(R.id.completedCheckBox);
        completedView.setOnCheckedChangeListener(completedViewCheckedChangeListener);
        
        final EditText todoNameView = (EditText)findViewById(R.id.todoName);
        // Whenever the EditText content changes, we'll update the TodoItem and commit to the database.
        todoNameView.addTextChangedListener(todoNameTextWatcher);
        
        // Add some views to our LinearLayout programmatically, so that we'll need to scroll.
        final LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
        for(int i = 0; i < 100; i++) {
            final TextView newTextView = new TextView(this);
            newTextView.setText(String.format((Locale)null, "Extra item %d", i + 1));
            
            // For a child view of LinearLayout, we need to use LinearLayout.LayoutParams
            final LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            
            layout.addView(newTextView, layoutParams);
        }
        
        // Populate text fields, etc.
        updateView();
    }
    
    /**
     * Update the view by populating text fields and date views to match the current database record. 
     */
    public void updateView() {
        final EditText todoNameView = (EditText)findViewById(R.id.todoName);
        final CheckBox completedView = (CheckBox)findViewById(R.id.completedCheckBox);
        final TextView addedDateView = (TextView)findViewById(R.id.added);
        final TextView updatedDateView = (TextView)findViewById(R.id.updated);
        
        final DateFormat dateFormat = DateFormat.getDateTimeInstance();
        
        todoNameView.setText(selectedTodo.getText());
        completedView.setChecked(selectedTodo.isCompleted());
        addedDateView.setText("Added: " + dateFormat.format(selectedTodo.getAdded()));
        updatedDateView.setText("Last updated: " + dateFormat.format(selectedTodo.getUpdated()));
    }
    
    //
    // Listeners
    //
    
    /**
     * Update the todo item when the "completed" checkbox is changed.
     */
    private final OnCheckedChangeListener completedViewCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          selectedTodo.setCompleted(isChecked);
          selectedTodo.commit(database);
          updateView();
        }
    };
    
    /**
     * Update the todo item when the text changes.
     */
    private TextWatcher todoNameTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        
        @Override
        public void afterTextChanged(Editable s) {
            selectedTodo.setText(s.toString());
            selectedTodo.commit(database);
        }
    };
}
