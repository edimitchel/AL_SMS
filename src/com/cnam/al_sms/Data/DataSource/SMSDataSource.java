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

	public static String[] allColumns = { SMSDataBaseHelper.COLUMN_ID,
			SMSDataBaseHelper.COLUMN_THREADID,
			SMSDataBaseHelper.COLUMN_ADDRESS, SMSDataBaseHelper.COLUMN_PERSON,
			SMSDataBaseHelper.COLUMN_DATE, SMSDataBaseHelper.COLUMN_DATESENT,
			SMSDataBaseHelper.COLUMN_READ, SMSDataBaseHelper.COLUMN_STATUS,
			SMSDataBaseHelper.COLUMN_TYPE, SMSDataBaseHelper.COLUMN_SUBJECT,
			SMSDataBaseHelper.COLUMN_BODY, SMSDataBaseHelper.COLUMN_SEEN };

	public SMSDataSource(Context context) {
		dbHelper = new SMSDataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long creerSMS(ContentValues cval) {
		long insertId = database
				.insert(SMSDataBaseHelper.TABLE_SMS, null, cval);
		return insertId;
	}

	public SMS getSMS(long id) {
		Cursor cursor = database.query(SMSDataBaseHelper.TABLE_SMS, allColumns,
				SMSDataBaseHelper.COLUMN_ID + " = " + id, null, null,
				null, null);
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

	public List<SMS> getSMSFrom(int person) {
		List<SMS> SMSs = new ArrayList<SMS>();
		Cursor cursor = database.query(SMSDataBaseHelper.TABLE_SMS, allColumns,
				SMSDataBaseHelper.COLUMN_PERSON + " = ?",
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
		Cursor cursor = database.query(SMSDataBaseHelper.TABLE_SMS, allColumns,
				SMSDataBaseHelper.COLUMN_ID + " > ?",
				new String[] { d.toString() }, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SMSs.add(cursorToSMS(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return SMSs;
	}

	/*
	 * SMSDataBaseHelper.COLUMN_ID, SMSDataBaseHelper.COLUMN_THREADID,
	 * SMSDataBaseHelper.COLUMN_ADDRESS, SMSDataBaseHelper.COLUMN_PERSON,
	 * SMSDataBaseHelper.COLUMN_DATE, SMSDataBaseHelper.COLUMN_DATESENT,
	 * SMSDataBaseHelper.COLUMN_READ, SMSDataBaseHelper.COLUMN_STATUS,
	 * SMSDataBaseHelper.COLUMN_TYPE, SMSDataBaseHelper.COLUMN_SUBJECT,
	 * SMSDataBaseHelper.COLUMN_BODY, SMSDataBaseHelper.COLUMN_SEEN
	 */

	private SMS cursorToSMS(Cursor cursor) {
		SMS sms = new SMS();
		sms.setId(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_ID)));
		sms.setFilId(cursor.getLong(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_THREADID)));
		sms.setAdresse(cursor.getString(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_ADDRESS)));
		sms.setPersonne(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_PERSON)));
		sms.setDate(new Date(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_DATE))));
		sms.setDateEnvoi(new Date(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_DATESENT))));
		sms.setLu(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_READ)));
		sms.setStatut(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_STATUS)));
		sms.setType(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_TYPE)));
		sms.setSujet(cursor.getString(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_SUBJECT)));
		sms.setMessage(cursor.getString(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_BODY)));
		sms.setVu(cursor.getInt(cursor
				.getColumnIndex(SMSDataBaseHelper.COLUMN_SEEN)));
		return sms;
	}
}
