package com.wty.app.wificar.base;

import android.app.Application;

import com.wty.app.wificar.util.PreferenceUtil;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceUtil.init(this);
	}

}
