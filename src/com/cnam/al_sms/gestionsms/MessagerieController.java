package com.cnam.al_sms.gestionsms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.telephony.SmsManager;

import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.modeles.SMS;

public abstract class MessagerieController {

	public MessagerieController() {
	}

	public static Cursor getConversations(Context context) {
		FilDataSource fds = new FilDataSource(context);
		fds.open();
		final Cursor c = fds.getAll();
		return c;
	}

	public static List<SMS> getSMS(long thread_id, Context context) {
		ArrayList<SMS> smsList = new ArrayList<SMS>();
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		Cursor sms = sds.getAll(thread_id);
		sms.moveToLast();
		while (!sms.isBeforeFirst()) {
			smsList.add(sds.cursorToSMS(sms));
			sms.moveToPrevious();
		}
		sds.close();
		sms.close();
		return smsList;
	}

	private static void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
}
