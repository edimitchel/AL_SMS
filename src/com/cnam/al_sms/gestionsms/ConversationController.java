package com.cnam.al_sms.gestionsms;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import shared.Globales;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.modeles.SMS;

public abstract class ConversationController {
	private static final Uri URI_SMS = Uri.parse("content://sms");

	public static String[] mois = new String[] { "janvier", "février", "mars",
			"avril", "mai", "juin", "juillet", "août", "septembre", "octobre",
			"novembre", "décembre" };

	public static String[] semaines = new String[] { "lundi", "mardi",
			"mercredi", "jeudi", "vendredi", "samedi", "dimanche" };

	public ConversationController() {
		// TODO Auto-generated constructor stub
	}

	public static void getAllSmsFromMasterBase(final Context context,
			final Handler handler) {
		final ContentResolver cr = context.getContentResolver();
		final SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		long lastID = sds.getLastSMSId();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Long intervalle = Long.valueOf(prefs.getString("intervalle_sync",
				Globales.INTERVALLE_TEMPS_SYNC + "")) * 1000;

		final String[] whereArgs = new String[] {
				lastID + "",
				intervalle < 0 ? "0" : System.currentTimeMillis() - intervalle
						+ "" };

		final Cursor c = cr.query(URI_SMS, SMSDataSource.allColumns,
				DataBaseHelper.COLUMN_ID + " > ? AND "
						+ DataBaseHelper.COLUMN_DATE + " > ?", whereArgs,
				DataBaseHelper.COLUMN_ID);
		c.moveToFirst();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				while (!c.isAfterLast()) {
					synchronized (this) {
						ContentValues vals = new ContentValues();
						DatabaseUtils.cursorRowToContentValues(c, vals);
						long idsms = sds.creerSMS(vals);
						c.moveToNext();
					}
				}
				updateFils(context);
				c.close();
				sds.close();
				handler.sendEmptyMessage(1);
			}
		};
		Thread mythread = new Thread(runnable);
		mythread.start();
	}

	public static void updateFils(final Context context, final Long threadId,
			final Handler handler) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				synchronized (this) {
					updateFils(context, threadId);
					handler.sendEmptyMessage(1);
				}
			}
		};
	}

	public static void updateFils(Context context) {
		updateFils(context, null);
	}

	public static void updateFils(Context context, Long threadId) {
		FilDataSource fds = new FilDataSource(context);
		fds.open();
		fds.updateFils(threadId);
		fds.close();
	}
	
	public static int getMessageUnread(Context context, long threadId){
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		int c = sds.getUnread(threadId);
		sds.close();
		return c;
	}

	public static String getDateTime(Date date) {
		long diffNow = (new Date(System.currentTimeMillis()).getTime() - date
				.getTime()) / 1000;

		String dateStr = (String) DateFormat.format(Globales.dateFormatString,
				date);
		if (diffNow < 60) {
			dateStr = "à l'instant";
		} else if (diffNow < 3600) {
			int min = (int) diffNow / 60;
			dateStr = "il y a " + min + " minute" + (min > 1 ? "s" : "");
		} else if (diffNow < 3600 * 24) {
			int heu = (int) diffNow / 3600;
			dateStr = "il y a " + heu + " heure" + (heu > 1 ? "s" : "");
		} else {
			Calendar c = new GregorianCalendar();
			c.setTime(date);
			dateStr = semaines[c.get(Calendar.DAY_OF_WEEK) - 1].substring(0, 3)
					+ ". " + c.get(Calendar.DATE) + " "
					+ mois[c.get(Calendar.DAY_OF_MONTH) - 1];
		}

		return dateStr;
	}

	public static void clearAllData(Context context) {
		DataBaseHelper dbh = new DataBaseHelper(context);
		SQLiteDatabase db = dbh.getWritableDatabase();
		context.deleteDatabase(dbh.getDatabaseName());
	}

	public static void readFilSMS(Context context, long threadId) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();

		Cursor c = sds.getAll(threadId);
		while (c.moveToNext()) {
			SMS sms = sds.cursorToSMS(c);
			ContentValues cv = new ContentValues();
			cv.put(DataBaseHelper.COLUMN_READ, "1");
			sds.updateSms(sms.getId(), cv);
		}

		c.close();
		sds.close();
	}

	public static void readSMS(Context context, long smsid) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();

		ContentValues cv = new ContentValues();
		cv.put(DataBaseHelper.COLUMN_READ, "1");
		sds.updateSms(smsid, cv);

		sds.close();
	}

	public static void seenFilSMS(Context context, long threadId) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		Cursor c = sds.getAll(threadId);
		while (c.moveToNext()) {
			SMS sms = sds.cursorToSMS(c);
			ContentValues cv = new ContentValues();
			cv.put(DataBaseHelper.COLUMN_SEEN, "1");
			sds.updateSms(sms.getId(), cv);
		}

		c.close();
		sds.close();
	}

	public static void seenSMS(Context context, long smsid) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();

		ContentValues cv = new ContentValues();
		cv.put(DataBaseHelper.COLUMN_SEEN, "1");
		sds.updateSms(smsid, cv);

		sds.close();
	}
}
