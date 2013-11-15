package com.mindsea.simpletodo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
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
import com.mindsea.simpletodo.util.DatabaseManager;

public class MainActivity extends Activity {
    
    private SQLiteDatabase database;
    private List<TodoItem> todoLists;
    private TodoArrayAdapter listAdapter;
    private ListView todoListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        todoListView = (ListView)findViewById(R.id.list);
        
        database = DatabaseManager.getSharedManager().getDatabase().getWritableDatabase();
        listAdapter = new TodoArrayAdapter(this);
        todoListView.setAdapter(listAdapter);
        todoListView.setOnItemClickListener(todoListViewItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTodoLists();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        database = null;
        todoLists = null;
        todoListView = null;
        listAdapter = null;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void updateTodoLists() {
        final Cursor todoListCursor = database.query("todolist", new String[] {"ROWID", "text", "updated_on", "added_on", "completed"}, null, null, null, null, null);
        
        todoLists = TodoItem.load(todoListCursor);
        
        todoListCursor.close();
        
        listAdapter.clear();
        listAdapter.addAll(todoLists);
        
        listAdapter.refresh();
    }
    
    //
    // OnItemClickListener
    //
    
    private OnItemClickListener todoListViewItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TodoItem selectedTodo = listAdapter.getItem(position);
            
            final Intent startIntent = TodoActivity.newStartIntent(MainActivity.this, selectedTodo.getRowId());
            startActivity(startIntent);
        }
    };
    
    //
    // Menu item listener
    //
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
            
            final View addDialogView = getLayoutInflater().inflate(R.layout.add_dialog, null);
            
            final EditText editText = (EditText) addDialogView.findViewById(R.id.text);
            
            addDialogBuilder.setView(addDialogView);
            addDialogBuilder.setCancelable(true);
            addDialogBuilder.setTitle("Add todo");
            
            addDialogBuilder.setPositiveButton("Add", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
