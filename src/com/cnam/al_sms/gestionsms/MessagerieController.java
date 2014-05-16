package com.cnam.al_sms.gestionsms;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cnam.al_sms.data.datasource.FilDataSource;

public abstract class MessagerieController {

	public MessagerieController() {
	}

	public static Cursor getConversations(Context context) {
		FilDataSource fds = new FilDataSource(context);
		fds.open();
		final Cursor c = fds.getAll();
		return c;
	}

	/*
	 * TODO
	public static Cursor getSMS(Context context, long thread_id) {
		FilDataSource fds = new FilDataSource(context);
		fds.open();
		Cursor c = fds.getAll();
		fds.close();
		return c;
	}
	 */
}
