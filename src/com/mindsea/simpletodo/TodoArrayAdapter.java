package com.mindsea.simpletodo;

import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindsea.simpletodo.model.TodoItem;

public class TodoArrayAdapter extends ArrayAdapter<TodoItem> {

    public TodoArrayAdapter(Context context) {
        super(context, R.layout.list_item_todo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_todo, parent, false);
        }
        
        final TodoItem todoItem = getItem(position);

        final TextView textView = (TextView) convertView.findViewById(R.id.text);
        
        textView.setText(todoItem.getText());
        textView.setTextColor(todoItem.isCompleted() ? Color.GRAY : Color.BLACK);
        textView.setTypeface(Typeface.SANS_SERIF, 
                todoItem.isCompleted() ? Typeface.ITALIC : Typeface.NORMAL);
        
        return convertView;
    }
    
    public void refresh() {
        sort(todoDateComparator);
        
        notifyDataSetChanged();
    }
    
    private Comparator<TodoItem> todoDateComparator = new Comparator<TodoItem>() {
        @Override
        public int compare(TodoItem lhs, TodoItem rhs) {
            return rhs.getAdded().compareTo(lhs.getAdded());
        }
    };
}
