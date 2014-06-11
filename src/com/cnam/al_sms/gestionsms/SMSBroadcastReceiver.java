package com.cnam.al_sms.gestionsms;

import java.util.ArrayList;
import java.util.Locale;

import shared.Globales;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;
import android.text.Html;
import android.util.Log;

import com.cnam.al_sms.R;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.esclave_activities.MainActivity;
import com.cnam.al_sms.modeles.SMS;

public class SMSBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "ALSMS";

	public static int nbMessage = 0;

	public static int mNotifNouvMessage;

	public static ArrayList<SMS> listNewSms = new ArrayList<SMS>();;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Object[] messages = (Object[]) bundle.get("pdus");
		SmsMessage[] sms = new SmsMessage[messages.length];
		// Create messages for each incoming PDU
		for (int n = 0; n < messages.length; n++) {
			sms[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();

		// Attendre que la base de SMS officielle récupère le sms
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (SmsMessage msg : sms) {
			Long time = msg.getTimestampMillis();
			Log.d(TAG, time + " | " + msg.getIndexOnIcc());
			Long id = sds.creerSMSFromMaster(time);
			if (id != null) {
				listNewSms.add(sds.getSMS(id));
				nbMessage++;
			}
		}
		sds.close();
		ConversationController.updateFils(context);
		Globales.getDeviceType(context);

		if (Globales.isTablet() || true && nbMessage > 0) {

			TypedArray notifText = context.getResources().obtainTypedArray(
					R.array.notif_nouv_message);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(
							String.format(Locale.FRANCE,
									notifText.getString(0), nbMessage,
									nbMessage > 1 ? "x" : "",
									nbMessage > 1 ? "s" : ""))
					.setContentText(notifText.getText(1));

			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

			inboxStyle.setBigContentTitle(String.format(Locale.FRANCE,
					notifText.getString(0), nbMessage, (nbMessage > 1) ? "x"
							: "", (nbMessage > 1) ? "s" : ""));

			for (SMS s : listNewSms) {
				inboxStyle.addLine(Html.fromHtml(String.format(Locale.FRANCE,
						notifText.getString(2), ContactController
								.getContactNameByThread(s.getFilId(), context),
						s.getMessage())));
			}

			notifText.recycle();
			mBuilder.setStyle(inboxStyle);

			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, MainActivity.class);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(mNotifNouvMessage, mBuilder.build());
		}
	}
}
