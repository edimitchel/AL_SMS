package com.cnam.al_sms.Data.DataSource;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cnam.al_sms.Data.SMSDataBaseHelper;
import com.cnam.al_sms.Data.SyncSMSDataBaseHelper;
import com.cnam.al_sms.Modeles.SyncSMS;

public class SyncDataSource {
	private SQLiteDatabase database;
	private SyncSMSDataBaseHelper dbHelper;
	private String[] allColumns = { SyncSMSDataBaseHelper.COLUMN_ID,
			SyncSMSDataBaseHelper.COLUMN_DATE,
			SyncSMSDataBaseHelper.COLUMN_TYPE,
			SyncSMSDataBaseHelper.COLUMN_FISRTSMS,
			SyncSMSDataBaseHelper.COLUMN_LASTSMS };

	public SyncDataSource() {
	}

	public SyncDataSource(Context context) {
		dbHelper = new SyncSMSDataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
		database.close();
	}

	public SyncSMS getSyncSMS(long id) {
		if (database != null) {
			Cursor c = database.query(
					SyncSMSDataBaseHelper.TABLE_SYNCHRONISATION, allColumns,
					SyncSMSDataBaseHelper.COLUMN_ID + " = ?",
					new String[] { String.valueOf(id) }, null, null, null);
			return (SyncSMS) cursorToSyncSMS(c);
		}
		return null;
	}

	public SyncSMS getLastSyncSMSDate(int type) {
		if (database != null) {
			Cursor c = database.query(
					SyncSMSDataBaseHelper.TABLE_SYNCHRONISATION, allColumns,
					SyncSMSDataBaseHelper.COLUMN_TYPE + " = ?",
					new String[] { String.valueOf(type) }, null, null,
					"ORDER BY " + SyncSMSDataBaseHelper.COLUMN_DATE + " DESC","1");
			c.moveToFirst();
			return (SyncSMS) cursorToSyncSMS(c);
		}
		return null;
	}

	public SyncSMS cursorToSyncSMS(Cursor c) {
		SyncSMS sSms = new SyncSMS();
		sSms.setIdSync(c.getLong(c
				.getColumnIndex(SyncSMSDataBaseHelper.COLUMN_ID)));
		sSms.setDateSync(new Date(c.getInt(c
				.getColumnIndex(SyncSMSDataBaseHelper.COLUMN_DATE))));
		sSms.setType(c.getInt(c
				.getColumnIndex(SyncSMSDataBaseHelper.COLUMN_TYPE)));
		sSms.setIdPremierSMS(c.getLong(c
				.getColumnIndex(SyncSMSDataBaseHelper.COLUMN_FISRTSMS)));
		sSms.setIdDernierSMS(c.getLong(c
				.getColumnIndex(SyncSMSDataBaseHelper.COLUMN_LASTSMS)));

		return sSms;
	}

}
