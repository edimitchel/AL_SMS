package com.cnam.al_sms;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class Al_SMS extends Application {
	private static Context context;
	private static Activity activity;

	@Override
	public void onCreate() {
		super.onCreate();
		Al_SMS.context = getApplicationContext();
	}

	public synchronized static Context getAppContext() {
		return Al_SMS.context;
	}

	public static void setCurrentActivity(Activity currentActivity) {
		activity = currentActivity;
	}

	public static Activity currentActivity() {
		return activity;
	}
}
