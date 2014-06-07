package com.cnam.al_sms.gestionsms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.widget.Toast;

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

	public static void sendSMS(Context context, String phoneNumber,
			String message) {
		try {

			String SENT = "sent";
			String DELIVERED = "delivered";

			Intent sentIntent = new Intent(SENT);
			/* Create Pending Intents */
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
					sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			Intent deliveryIntent = new Intent(DELIVERED);

			PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
					deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			context.registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					String result = "";

					switch (getResultCode()) {

					case Activity.RESULT_OK:
						result = "Message bien envoyé";
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						result = "Le message ne s'est pas envoyé..";
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						result = "Réseau mobile désactivé";
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						result = "Le PDU n'est pas défini";
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						result = "Le service n'est pas accessible";
						break;
					}

					Toast.makeText(context, result, Toast.LENGTH_LONG).show();
				}

			}, new IntentFilter(SENT));

			context.registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					Toast.makeText(context, "Message délivré", Toast.LENGTH_LONG)
							.show();
				}

			}, new IntentFilter(DELIVERED));

			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, message, sentPI,
					deliverPI);
		} catch (Exception ex) {
			Toast.makeText(context, ex.getMessage().toString(),
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
	}
}
