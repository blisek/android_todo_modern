package com.blisek.todo_list;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by bartek on 4/20/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
