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

/**
 * An Adapter is used to provide views for any widget that can display multiple
 * views. Most commonly, this includes ListViews, ViewPagers, and Spinners (eg.
 * selection boxes).
 * 
 * ArrayAdapter is a convenient adapter implementation that provides content
 * from an array or list of content items. By default, it will create views
 * using a layout specified at construction, and configure a TextView in that
 * layout by calling .toString() on the content items.
 * 
 * We want custom views, so we override getView() for customization.
 */
public class TodoArrayAdapter extends ArrayAdapter<TodoItem> {
    
    public TodoArrayAdapter(Context context) {
        super(context, R.layout.list_item_todo);
    }
    
    /**
     * getView returns a view for the specified item position. By overriding
     * getView, we can use a custom row layout for each item.
     * 
     * @param position
     *            The position of the item/row in the ListView.
     * @param convertView
     *            A view that was created by a previous call to getView, but
     *            which has gone offscreen and can be reconfigured for this row.
     *            If no such view is available, then this will be null;
     *            otherwise, we can reconfigure the view for this row's content
     *            and avoid inflating the layout. This is essentially view reuse
     *            to save time inflating layouts.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the todo row layout from list_item_todo.xml so we can
            // configure it.
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_todo, parent, false);
        }
        
        // If convertView was NOT null when getView was called, then it contains
        // a previously-inflated View that was configured for a different row.
        //
        // In order to handle this case, we have to be sure to configure every
        // aspect of the layout that can change between rows, because the layout
        // might be configured for any possible state. In other words, we can't
        // rely on the default state of view elements from the layout.
        
        final TodoItem todoItem = getItem(position);

        final TextView textView = (TextView) convertView.findViewById(R.id.text);
        
        textView.setText(todoItem.getText());
        textView.setTextColor(todoItem.isCompleted() ? Color.GRAY : Color.BLACK);
        textView.setTypeface(Typeface.SANS_SERIF, 
                todoItem.isCompleted() ? Typeface.ITALIC : Typeface.NORMAL);
        
        return convertView;
    }
    
    /**
     * Update the list for new content.
     * 
     * If our list contents changed, we'll re-sort the list to ensure we're
     * displaying the content in a presentable order, and then notify the
     * ListView that our data set changed so it can fetch new views for the new
     * data set.
     */
    public void refresh() {
        sort(todoDateComparator);
        
        notifyDataSetChanged();
    }
    
    /**
     * Comparator that orders TodoItem instances by their added date.
     */
    private Comparator<TodoItem> todoDateComparator = new Comparator<TodoItem>() {
        @Override
        public int compare(TodoItem lhs, TodoItem rhs) {
            return rhs.getAdded().compareTo(lhs.getAdded());
        }
    };
}
