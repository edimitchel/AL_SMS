package com.cnam.al_sms.data.datasource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import shared.Globales;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.modeles.SMS;

public class SMSDataSource {

	private static final String TAG = "ALSMS";
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;

	public static String[] allColumns = { DataBaseHelper.COLUMN_ID,
			DataBaseHelper.COLUMN_THREADID, DataBaseHelper.COLUMN_ADDRESS,
			DataBaseHelper.COLUMN_PERSON, DataBaseHelper.COLUMN_DATE,
			DataBaseHelper.COLUMN_DATESENT, DataBaseHelper.COLUMN_READ,
			DataBaseHelper.COLUMN_STATUS, DataBaseHelper.COLUMN_TYPE,
			DataBaseHelper.COLUMN_SUBJECT, DataBaseHelper.COLUMN_BODY,
			DataBaseHelper.COLUMN_SEEN };

	public SMSDataSource(Context context) {
		dbHelper = new DataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long creerSMS(ContentValues cval) {
		long insertId = database.insert(DataBaseHelper.TABLE_SMS, null, cval);
		return insertId;
	}

	public SMS getSMS(long id) {
		Cursor cursor = database.query(DataBaseHelper.TABLE_SMS, allColumns,
				DataBaseHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		SMS newSMS = cursorToSMS(cursor);
		cursor.close();
		return newSMS;
	}

	public int deleteSms(long id) {
		int result = database.delete(DataBaseHelper.TABLE_SMS,
				DataBaseHelper.COLUMN_ID + " = ?",
				new String[] { String.valueOf(id) });
		return result;
	}

	public List<SMS> getSMSFrom(int person) {
		List<SMS> SMSs = new ArrayList<SMS>();
		Cursor cursor = database.query(DataBaseHelper.TABLE_SMS, allColumns,
				DataBaseHelper.COLUMN_PERSON + " = ?",
				new String[] { String.valueOf(person) }, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SMSs.add(cursorToSMS(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return SMSs;
	}

	public List<SMS> getSmsAfterDate(Date d) {
		List<SMS> SMSs = new ArrayList<SMS>();
		Cursor cursor = database.query(DataBaseHelper.TABLE_SMS, allColumns,
				DataBaseHelper.COLUMN_DATE + " > ?",
				new String[] { d.toString() }, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SMSs.add(cursorToSMS(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return SMSs;
	}
	
	public List<SMS> getNsms(int n) {
		ArrayList<SMS> list = new ArrayList<SMS>();
		Cursor c = database.query(DataBaseHelper.TABLE_SMS,
				allColumns, null, null, null, null, DataBaseHelper.COLUMN_DATE
						+ " DESC", Globales.MESSAGE_COUNT_AFFICHER + "");
		int i =0;
		while (!c.isAfterLast() && i<n) {
			list.add(cursorToSMS(c));
			c.moveToNext();
			i++;
		}
		c.close();
		return list;
	}
	

	public List<SMS> getAll() {
		ArrayList<SMS> list = new ArrayList<SMS>();
		Cursor c = database.query(DataBaseHelper.TABLE_SMS,
				allColumns, null, null, null, null, DataBaseHelper.COLUMN_DATE
						+ " DESC", Globales.MESSAGE_COUNT_AFFICHER + "");

		while (!c.isAfterLast()) {
			list.add(cursorToSMS(c));
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public Cursor getAll(long thread_id) {
		Cursor c = database.query(DataBaseHelper.TABLE_SMS, allColumns,
				DataBaseHelper.COLUMN_THREADID + " = ?",
				new String[] { thread_id + "" }, null, null,
				DataBaseHelper.COLUMN_DATE + " DESC",
				Globales.MESSAGE_COUNT_AFFICHER + "");
		Log.i(TAG,
				"Nombre SMS pour le thread n°" + thread_id + " : "
						+ c.getCount());

		if (c.getCount() == 0)
			return null;
		else {
			return c;
		}
	}

	public long getLastSMSId() {
		Cursor c = database.query(DataBaseHelper.TABLE_SMS,
				new String[] { DataBaseHelper.COLUMN_ID }, null, null, null,
				null, DataBaseHelper.COLUMN_ID + " DESC", "1");
		if (c.getCount() == 0) {
			return 0;
		} else {
			c.moveToFirst();
			long id = c.getLong(c.getColumnIndex(DataBaseHelper.COLUMN_ID));
			return id;
		}

	}

	public Cursor getSmsOfThread(long thread_id) {
		Cursor c = database.query(DataBaseHelper.TABLE_SMS, allColumns,
				DataBaseHelper.COLUMN_THREADID + " = ?",
				new String[] { thread_id + "" }, null, null, null, "1");
		c.moveToFirst();
		return c;
	}

	public SMS cursorToSMS(Cursor cursor) {
		SMS sms = new SMS();
		cursor.moveToFirst();
		int n = cursor.getColumnIndex(DataBaseHelper.COLUMN_ID);
		sms.setId(cursor.getInt(n));
		sms.setFilId(cursor.getLong(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_THREADID)));
		sms.setAdresse(cursor.getString(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_ADDRESS)));
		sms.setPersonne(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_PERSON)));
		sms.setDate(new Date(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_DATE))));
		sms.setDateEnvoi(new Date(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_DATESENT))));
		sms.setLu(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_READ)));
		sms.setStatut(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_STATUS)));
		sms.setType(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_TYPE)));
		sms.setSujet(cursor.getString(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_SUBJECT)));
		sms.setMessage(cursor.getString(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_BODY)));
		sms.setVu(cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COLUMN_SEEN)));
		return sms;
	}
}
