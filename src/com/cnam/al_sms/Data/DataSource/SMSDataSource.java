package com.cnam.al_sms.Data.DataSource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cnam.al_sms.Data.SMSDataBaseHelper;
import com.cnam.al_sms.Modeles.SMS;

public class SMSDataSource {

	private SQLiteDatabase database;
	private SMSDataBaseHelper dbHelper;
	private String[] allColumns = { SMSDataBaseHelper.COLUMN_ID,
			SMSDataBaseHelper.COLUMN_THREADID,
			SMSDataBaseHelper.COLUMN_ADDRESS,
			SMSDataBaseHelper.COLUMN_PERSON,
			SMSDataBaseHelper.COLUMN_DATE,
			SMSDataBaseHelper.COLUMN_DATESENT,
			SMSDataBaseHelper.COLUMN_READ,
			SMSDataBaseHelper.COLUMN_STATUS,
			SMSDataBaseHelper.COLUMN_TYPE,
			SMSDataBaseHelper.COLUMN_SUBJECT,
			SMSDataBaseHelper.COLUMN_BODY,
			SMSDataBaseHelper.COLUMN_SEEN };

	public SMSDataSource(Context context) {
		dbHelper = new SMSDataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public SMS creerSMS(int auteur, String message) {
		ContentValues values = new ContentValues();
		values.put(SMSDataBaseHelper.COLUMN_BODY, message);
		values.put(SMSDataBaseHelper.COLUMN_PERSON, auteur);
		long insertId = database.insert(SMSDataBaseHelper.TABLE_SMS, null,
				values);
		Cursor cursor = database.query(SMSDataBaseHelper.TABLE_SMS,
				allColumns,
				SMSDataBaseHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		SMS newSMS = cursorToSMS(cursor);
		cursor.close();
		return newSMS;
	}

	public int deleteSms(long id) {
		int result = database.delete(SMSDataBaseHelper.TABLE_SMS,
				SMSDataBaseHelper.COLUMN_ID + " = ?",
				new String[] { String.valueOf(id) });
		return result;
	}

	public List<SMS> getSMS(int person) {
		List<SMS> SMSs = new ArrayList<SMS>();
		Cursor cursor = database.query(SMSDataBaseHelper.TABLE_SMS,
				allColumns, SMSDataBaseHelper.COLUMN_PERSON + " = ?",
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
		Cursor cursor = database.query(SMSDataBaseHelper.TABLE_SMS,
				allColumns, SMSDataBaseHelper.COLUMN_ID + " > ?",
				new String[] { d.toString() }, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SMSs.add(cursorToSMS(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return SMSs;
	}

	private SMS cursorToSMS(Cursor cursor) {
		SMS sms = new SMS();
		sms.setId(cursor.getLong(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_ID)));
		sms.setMessage(cursor.getString(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_BODY)));
		sms.setPersonne(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_PERSON)));
		return sms;
	}
}
