package com.cnam.al_sms.data.datasource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.modeles.SMS;
import com.cnam.al_sms.modeles.SyncSMS;

public class SyncDataSource {
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	public static String[] allColumns = { DataBaseHelper.COLUMN_ID,
			DataBaseHelper.COLUMN_DATE, DataBaseHelper.COLUMN_TYPE,
			DataBaseHelper.COLUMN_FISRTSMS, DataBaseHelper.COLUMN_LASTSMS };
	private Context contexte;

	public SyncDataSource() {
	}

	public SyncDataSource(Context context) {
		dbHelper = new DataBaseHelper(context);
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
			Cursor c = database.query(DataBaseHelper.TABLE_SYNCHRONISATION,
					allColumns, DataBaseHelper.COLUMN_ID + " = ?",
					new String[] { String.valueOf(id) }, null, null, null);
			return (SyncSMS) cursorToSyncSMS(c);
		} else
			throw new Exception("La base de données n'est pas instanciée.");
	}

	public SyncSMS getLastSyncSMSDate() {
		if (database != null) {
			Cursor c = database.query(DataBaseHelper.TABLE_SYNCHRONISATION,
					allColumns, null, null, null, null,
					DataBaseHelper.COLUMN_DATE + " DESC", "1");
			c.moveToFirst();
			return (SyncSMS) cursorToSyncSMS(c);
		}
		return null;
	}

	public List<SMS> getSmsNotSync() {
		Date d = (Date) getLastSyncSMSDate().getDateSync();
		close();
		SMSDataSource sds = new SMSDataSource(contexte);
		sds.open();
		List<SMS> smsNotSync = sds.getSmsAfterDate(d);
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
		cv.put(DataBaseHelper.COLUMN_DATE, now.getTime());
		cv.put(DataBaseHelper.COLUMN_TYPE, sSms.getType());
		cv.put(DataBaseHelper.COLUMN_FISRTSMS, sSms.getIdPremierSMS());
		cv.put(DataBaseHelper.COLUMN_LASTSMS, sSms.getIdDernierSMS());

		long id = database.insert(DataBaseHelper.TABLE_SYNCHRONISATION, null,
				cv);
		return getSyncSMS(id);
	}

	public List<SyncSMS> getAll() {
		ArrayList<SyncSMS> list = new ArrayList<SyncSMS>();
		Cursor c = database.query(DataBaseHelper.TABLE_SYNCHRONISATION,
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
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID)));
			sSms.setDateSync(new Date(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_DATE))));
			sSms.setType(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TYPE)));
			sSms.setIdPremierSMS(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_FISRTSMS)));
			sSms.setIdDernierSMS(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_LASTSMS)));
			c.close();
			return sSms;
		}
		c.close();
		return null;
	}
}
