package com.suk.socketioandroidchat;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by Felix.Zhong on 2018/10/10 11:44
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.getConfig().setGlobalTag("Felix_skiac");
    }
}
