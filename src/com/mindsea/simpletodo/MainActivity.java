package com.mindsea.simpletodo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.mindsea.simpletodo.model.TodoItem;
import com.mindsea.simpletodo.util.TodoListDatabase;

public class MainActivity extends Activity {
    
    private SQLiteDatabase database;
    private TodoArrayAdapter listAdapter;
    private ListView todoListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        todoListView = (ListView)findViewById(R.id.list);
        
        database = TodoListDatabase.getShared().getWritableDatabase();
        listAdapter = new TodoArrayAdapter(this);
        todoListView.setAdapter(listAdapter);
        todoListView.setOnItemClickListener(todoListViewItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // We update the list every time we come onscreen in case
        // anything was changed (eg. by another activity or dialog).
        updateTodoLists();
    }
    
    /**
     * Inflate the activity's options menu; this adds items to the action bar if
     * it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Reload the todo lists from the database and display them in the list.
     */
    private void updateTodoLists() {
        final List<TodoItem> todoLists = TodoItem.loadTodoItems(database);
        
        listAdapter.clear();
        listAdapter.addAll(todoLists);
        
        listAdapter.refresh();
    }
    
    //
    // Listeners
    //
    
    /**
     * Item click listener that starts an instance TodoActivity which shows
     * details for the selected TodoItem.
     */
    private OnItemClickListener todoListViewItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TodoItem selectedTodo = listAdapter.getItem(position);
            
            final Intent startIntent = TodoActivity.newStartIntent(MainActivity.this, selectedTodo.getRowId());
            startActivity(startIntent);
        }
    };
    
    /**
     * Respond to menu item selections.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            
            // We'll build a dialog with an EditText widget so the user can
            // create a new todo.
            
            AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
            
            final View addDialogView = getLayoutInflater().inflate(R.layout.add_dialog, null);
            
            addDialogBuilder.setView(addDialogView);
            addDialogBuilder.setCancelable(true);
            addDialogBuilder.setTitle("Add todo");

            addDialogBuilder.setPositiveButton("Add", new OnClickListener() {
                /**
                 * Create a new TodoItem and add it to the database.
                 * 
                 * Called if the user taps the 'Add' button in the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText editText = (EditText) addDialogView.findViewById(R.id.text);
                    
                    final String text = editText.getText().toString();
                    TodoItem newItem = new TodoItem(text);
                    newItem.commit(database);
                    
                    updateTodoLists();
                }
            });
            
            addDialogBuilder.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
