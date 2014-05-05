package com.cnam.al_sms.data.datasource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cnam.al_sms.data.SyncSMSDataBaseHelper;
import com.cnam.al_sms.modeles.SMS;
import com.cnam.al_sms.modeles.SyncSMS;

public class SyncDataSource {
	private SQLiteDatabase database;
	private SyncSMSDataBaseHelper dbHelper;
	public static String[] allColumns = { SyncSMSDataBaseHelper.COLUMN_ID,
			SyncSMSDataBaseHelper.COLUMN_DATE,
			SyncSMSDataBaseHelper.COLUMN_TYPE,
			SyncSMSDataBaseHelper.COLUMN_FISRTSMS,
			SyncSMSDataBaseHelper.COLUMN_LASTSMS };
	private Context contexte;

	public SyncDataSource() {
	}

	public SyncDataSource(Context context) {
		dbHelper = new SyncSMSDataBaseHelper(context);
		contexte = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public SyncSMS getSyncSMS(long id) throws Exception {
		if (database != null) {
			Cursor c = database.query(
					SyncSMSDataBaseHelper.TABLE_SYNCHRONISATION, allColumns,
					SyncSMSDataBaseHelper.COLUMN_ID + " = ?",
					new String[] { String.valueOf(id) }, null, null, null);
			return (SyncSMS) cursorToSyncSMS(c);
		} else
			throw new Exception("La base de données n'est pas instanciée.");
	}

	public SyncSMS getLastSyncSMSDate(int type) {
		if (database != null) {
			Cursor c = database.query(
					SyncSMSDataBaseHelper.TABLE_SYNCHRONISATION, allColumns,
					SyncSMSDataBaseHelper.COLUMN_TYPE + " = ?",
					new String[] { String.valueOf(type) }, null, null,
					SyncSMSDataBaseHelper.COLUMN_DATE + " DESC", "1");
			c.moveToFirst();
			return (SyncSMS) cursorToSyncSMS(c);
		}
		return null;
	}

	public List<SMS> getSmsNotSync() {
		open();
		Date d = (Date) getLastSyncSMSDate(1).getDateSync();
		close();
		SMSDataSource sds = new SMSDataSource(contexte);
		sds.open();
		ArrayList<SMS> smsNotSync = (ArrayList<SMS>) sds.getSmsAfterDate(d);
		sds.close();
		return smsNotSync;
	}

	public static SyncSMS newSync(int type, long idPremierSms, long idDernierSms) {
		SyncSMS sSms = new SyncSMS();
		sSms.setType(type);
		sSms.setIdPremierSMS(idPremierSms);
		sSms.setIdDernierSMS(idDernierSms);
		return sSms;
	}

	public SyncSMS addSyncSms(SyncSMS sSms) throws Exception {
		ContentValues cv = new ContentValues();
		Date now = new Date(System.currentTimeMillis());
		cv.put(SyncSMSDataBaseHelper.COLUMN_DATE, now.getTime());
		cv.put(SyncSMSDataBaseHelper.COLUMN_TYPE, sSms.getType());
		cv.put(SyncSMSDataBaseHelper.COLUMN_FISRTSMS, sSms.getIdPremierSMS());
		cv.put(SyncSMSDataBaseHelper.COLUMN_LASTSMS, sSms.getIdDernierSMS());

		long id = database.insert(SyncSMSDataBaseHelper.TABLE_SYNCHRONISATION,
				null, cv);
		return getSyncSMS(id);
	}

	public List<SyncSMS> getAll() {
		ArrayList<SyncSMS> list = new ArrayList<SyncSMS>();
		Cursor c = database.query(SyncSMSDataBaseHelper.TABLE_SYNCHRONISATION,
				allColumns, null, null, null, null, null);

		while (!c.isAfterLast()) {
			list.add(cursorToSyncSMS(c));
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public SyncSMS cursorToSyncSMS(Cursor c) {
		if (c.moveToFirst()) {
			SyncSMS sSms = new SyncSMS();
			sSms.setIdSync(c.getInt(c
					.getColumnIndexOrThrow(SyncSMSDataBaseHelper.COLUMN_ID)));
			sSms.setDateSync(new Date(c.getInt(c
					.getColumnIndexOrThrow(SyncSMSDataBaseHelper.COLUMN_DATE))));
			sSms.setType(c.getInt(c
					.getColumnIndexOrThrow(SyncSMSDataBaseHelper.COLUMN_TYPE)));
			sSms.setIdPremierSMS(c.getInt(c
					.getColumnIndexOrThrow(SyncSMSDataBaseHelper.COLUMN_FISRTSMS)));
			sSms.setIdDernierSMS(c.getInt(c
					.getColumnIndexOrThrow(SyncSMSDataBaseHelper.COLUMN_LASTSMS)));
			c.close();
			return sSms;
		}
		c.close();
		return null;
	}
}
