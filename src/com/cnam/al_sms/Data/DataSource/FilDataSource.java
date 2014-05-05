package com.cnam.al_sms.Data.DataSource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cnam.al_sms.Data.FilSMSDataBaseHelper;
import com.cnam.al_sms.Data.SMSDataBaseHelper;
import com.cnam.al_sms.Modeles.Fil;

public class FilDataSource {
	private static final String TAG = "ALSMS";

	private SQLiteDatabase database;
	private FilSMSDataBaseHelper dbHelper;
	public static String[] allColumns = { FilSMSDataBaseHelper.COLUMN_ID,
			FilSMSDataBaseHelper.COLUMN_SNIPPET,
			FilSMSDataBaseHelper.COLUMN_MESSAGECOUNT };
	private Context contexte;

	public FilDataSource() {
	}

	public FilDataSource(Context context) {
		dbHelper = new FilSMSDataBaseHelper(context);
		contexte = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
		database.close();
	}

	public Fil getFil(long threadid) {
		Cursor c = database.query(FilSMSDataBaseHelper.TABLE_FIL, allColumns,
				FilSMSDataBaseHelper.COLUMN_ID + " = ?",
				new String[] { String.valueOf(threadid) }, null, null, null);
		c.moveToFirst();
		return (Fil) cursorToFilSMS(c);
	}

	public int updateFil(long threadid) {
		Cursor c = database.query(SMSDataBaseHelper.TABLE_SMS,
				new String[] { SMSDataBaseHelper.COLUMN_SUBJECT,
						SMSDataBaseHelper.COLUMN_BODY },
				SMSDataBaseHelper.COLUMN_THREADID + " = ?",
				new String[] { String.valueOf(threadid) }, null, null,
				SMSDataBaseHelper.COLUMN_DATE + " DESC");
		c.moveToFirst();

		int nb_message = c.getCount();
		String extrait = c.getString(c
				.getColumnIndex(SMSDataBaseHelper.COLUMN_BODY));

		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(FilSMSDataBaseHelper.COLUMN_MESSAGECOUNT, nb_message);
		cvUpdate.put(FilSMSDataBaseHelper.COLUMN_SNIPPET, extrait);

		int nbRowUpdate = database.update(FilSMSDataBaseHelper.TABLE_FIL,
				cvUpdate, FilSMSDataBaseHelper.COLUMN_ID,
				new String[] { String.valueOf(threadid) });

		return nbRowUpdate;
	}

	public void generateFil() {
		Log.i(TAG, "SELECT " + FilSMSDataBaseHelper.TABLE_FIL + "."
				+ FilSMSDataBaseHelper.COLUMN_ID
				+ " THREAD_ID, count(sms.*) NB_MESSAGE, "
				+ SMSDataBaseHelper.TABLE_SMS + "."
				+ SMSDataBaseHelper.COLUMN_BODY + " EXTRAIT " + " FROM "
				+ FilSMSDataBaseHelper.TABLE_FIL + ", "
				+ SMSDataBaseHelper.TABLE_SMS + " WHERE "
				+ FilSMSDataBaseHelper.TABLE_FIL + "."
				+ FilSMSDataBaseHelper.COLUMN_ID + " = "
				+ SMSDataBaseHelper.TABLE_SMS + "."
				+ SMSDataBaseHelper.COLUMN_THREADID + " GROUP BY "
				+ SMSDataBaseHelper.TABLE_SMS + "."
				+ SMSDataBaseHelper.COLUMN_THREADID);
		/*Cursor c = database.rawQuery("SELECT " + FilSMSDataBaseHelper.TABLE_FIL
				+ "." + FilSMSDataBaseHelper.COLUMN_ID
				+ " THREAD_ID, count(sms.*) NB_MESSAGE, "
				+ SMSDataBaseHelper.TABLE_SMS + "."
				+ SMSDataBaseHelper.COLUMN_BODY + " EXTRAIT " + " FROM "
				+ FilSMSDataBaseHelper.TABLE_FIL + ", "
				+ SMSDataBaseHelper.TABLE_SMS + " WHERE "
				+ FilSMSDataBaseHelper.TABLE_FIL + "."
				+ FilSMSDataBaseHelper.COLUMN_ID + " = "
				+ SMSDataBaseHelper.TABLE_SMS + "."
				+ SMSDataBaseHelper.COLUMN_THREADID + " GROUP BY "
				+ SMSDataBaseHelper.TABLE_SMS + "."
				+ SMSDataBaseHelper.COLUMN_THREADID, null);*/
	}

	public Fil cursorToFilSMS(Cursor c) {
		if (c.moveToFirst()) {
			Fil fil = new Fil();
			fil.setFilId(c.getInt(c
					.getColumnIndexOrThrow(FilSMSDataBaseHelper.COLUMN_ID)));
			fil.setExtrait(c.getString(c
					.getColumnIndexOrThrow(FilSMSDataBaseHelper.COLUMN_SNIPPET)));
			fil.setNombreMessage(c.getInt(c
					.getColumnIndexOrThrow(FilSMSDataBaseHelper.COLUMN_MESSAGECOUNT)));
			c.close();
			return fil;
		}
		return null;
	}

	public List<Fil> getAll() {
		ArrayList<Fil> list = new ArrayList<Fil>();
		Cursor c = database.query(FilSMSDataBaseHelper.TABLE_FIL, allColumns,
				null, null, null, null, null);

		while (!c.isAfterLast()) {
			list.add(cursorToFilSMS(c));
			c.moveToNext();
		}
		return list;
	}

	public Fil cursorToSyncSMS(Cursor c) {
		if (c.moveToFirst()) {
			Fil fil = new Fil();
			fil.setFilId(c.getInt(c
					.getColumnIndexOrThrow(FilSMSDataBaseHelper.COLUMN_ID)));
			fil.setExtrait(c.getString(c
					.getColumnIndexOrThrow(FilSMSDataBaseHelper.COLUMN_SNIPPET)));
			fil.setNombreMessage(c.getInt(c
					.getColumnIndexOrThrow(FilSMSDataBaseHelper.COLUMN_MESSAGECOUNT)));
			c.close();
			return fil;
		}
		return null;
	}

}
