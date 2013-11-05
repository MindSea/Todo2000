package com.mindsea.simpletodo;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mindsea.simpletodo.model.TodoItem;
import com.mindsea.simpletodo.util.DatabaseManager;

public class MainActivity extends Activity implements OnItemClickListener {
    
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
        todoListView.setOnItemClickListener(this);
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
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(todoListView)) {
            final TodoItem selectedTodo = listAdapter.getItem(position);
            
            selectedTodo.setCompleted(!selectedTodo.isCompleted());
            
            selectedTodo.commit(database);
            
            listAdapter.refresh();
        }
    }
}
