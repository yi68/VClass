package com.top.application;

import android.app.Application;

/**
 * Created by zym on 2017/5/16.
 */
public class DataApplication extends Application {
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
