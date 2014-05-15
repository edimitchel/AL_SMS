package com.cnam.al_sms.data.datasource;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.modeles.Fil;

public class FilDataSource {
	private static final String TAG = "ALSMS";

	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	public static String[] allColumns = { DataBaseHelper.COLUMN_ID,
			DataBaseHelper.COLUMN_SNIPPET,
			DataBaseHelper.COLUMN_MESSAGECOUNT };
	private Context contexte;

	public FilDataSource() {
	}

	public FilDataSource(Context context) {
		dbHelper = new DataBaseHelper(context);
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
		Cursor c = database.query(DataBaseHelper.TABLE_FIL, allColumns,
				DataBaseHelper.COLUMN_ID + " = ?",
				new String[] { String.valueOf(threadid) }, null, null, null);
		c.moveToFirst();
		return (Fil) cursorToFilSMS(c);
	}

	public int updateFil(long threadid) {
		Cursor c = database.query(DataBaseHelper.TABLE_SMS,
				new String[] { DataBaseHelper.COLUMN_SUBJECT,
						DataBaseHelper.COLUMN_BODY },
				DataBaseHelper.COLUMN_THREADID + " = ?",
				new String[] { String.valueOf(threadid) }, null, null,
				DataBaseHelper.COLUMN_DATE + " DESC");
		c.moveToFirst();

		int nb_message = c.getCount();
		String extrait = c.getString(c
				.getColumnIndex(DataBaseHelper.COLUMN_BODY));

		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(DataBaseHelper.COLUMN_MESSAGECOUNT, nb_message);
		cvUpdate.put(DataBaseHelper.COLUMN_SNIPPET, extrait);

		int nbRowUpdate = database.update(DataBaseHelper.TABLE_FIL,
				cvUpdate, DataBaseHelper.COLUMN_ID,
				new String[] { String.valueOf(threadid) });

		return nbRowUpdate;
	}

	public void generateFil() {
		Log.i(TAG, "SELECT " + DataBaseHelper.TABLE_FIL + "."
				+ DataBaseHelper.COLUMN_ID
				+ " THREAD_ID, count(sms.*) NB_MESSAGE, "
				+ DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_BODY + " EXTRAIT " + " FROM "
				+ DataBaseHelper.TABLE_FIL + ", "
				+ DataBaseHelper.TABLE_SMS + " WHERE "
				+ DataBaseHelper.TABLE_FIL + "."
				+ DataBaseHelper.COLUMN_ID + " = "
				+ DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_THREADID + " GROUP BY "
				+ DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_THREADID);

		Cursor c = database.rawQuery("SELECT " + DataBaseHelper.TABLE_FIL
				+ "." + DataBaseHelper.COLUMN_ID
				+ " THREAD_ID, 1 NB_MESSAGE, "
				+ DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_BODY + " EXTRAIT " + " FROM "
				+ DataBaseHelper.TABLE_FIL + ", "
				+ DataBaseHelper.TABLE_SMS + " WHERE "
				+ DataBaseHelper.TABLE_FIL + "."
				+ DataBaseHelper.COLUMN_ID + " = "
				+ DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_THREADID, null);

	}

	public Fil cursorToFilSMS(Cursor c) {
		if (c.moveToFirst()) {
			Fil fil = new Fil();
			fil.setFilId(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID)));
			fil.setExtrait(c.getString(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SNIPPET)));
			fil.setNombreMessage(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_MESSAGECOUNT)));
			c.close();
			return fil;
		}
		return null;
	}

	public Cursor getAll() {
		ArrayList<Fil> list = new ArrayList<Fil>();
		Cursor c = database.query(DataBaseHelper.TABLE_FIL, allColumns,
				null, null, null, null, null);

		return c;
	}

	public Fil cursorToFil(Cursor c) {
		if (c.moveToFirst()) {
			Fil fil = new Fil();
			fil.setFilId(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID)));
			fil.setExtrait(c.getString(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SNIPPET)));
			fil.setNombreMessage(c.getInt(c
					.getColumnIndexOrThrow(DataBaseHelper.COLUMN_MESSAGECOUNT)));
			c.close();
			return fil;
		}
		return null;
	}

}
