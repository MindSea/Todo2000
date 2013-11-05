package com.mindsea.simpletodo;

import android.app.Application;

public class TodoApplication extends Application {
    
    private static TodoApplication instance;
    
    public static TodoApplication getInstance() {
        return instance;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
