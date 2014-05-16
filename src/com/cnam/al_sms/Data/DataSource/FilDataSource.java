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
			DataBaseHelper.COLUMN_SNIPPET, DataBaseHelper.COLUMN_MESSAGECOUNT };
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
		return (Fil) cursorToFil(c);
	}

	public void updateFils() {
		Cursor cThreads = database.query(DataBaseHelper.TABLE_SMS,
				new String[] { DataBaseHelper.COLUMN_THREADID,
						DataBaseHelper.COLUMN_BODY, "count(*)" }, null, null,
				DataBaseHelper.COLUMN_THREADID, null, null);

		cThreads.moveToFirst();
		while (!cThreads.isAfterLast()) {
			long thread_id = cThreads.getLong(cThreads
					.getColumnIndex(DataBaseHelper.COLUMN_THREADID));
			int nb_message = cThreads.getInt(2);
			String extrait = cThreads.getString(cThreads
					.getColumnIndex(DataBaseHelper.COLUMN_BODY));

			ContentValues cvUpdate = new ContentValues();
			cvUpdate.put(DataBaseHelper.COLUMN_MESSAGECOUNT, nb_message);
			cvUpdate.put(DataBaseHelper.COLUMN_SNIPPET, extrait);

			Cursor cFil = database.query(DataBaseHelper.TABLE_FIL, null,
					DataBaseHelper.COLUMN_ID + " = ?",
					new String[] { String.valueOf(thread_id) }, null, null,
					null);

			long nbRowUpdate = 0;
			if (cFil.getCount() == 1) {
				nbRowUpdate = database.update(DataBaseHelper.TABLE_FIL,
						cvUpdate, DataBaseHelper.COLUMN_ID,
						new String[] { String.valueOf(thread_id) });
			} else {
				cvUpdate.put(DataBaseHelper.COLUMN_ID, thread_id);
				nbRowUpdate = database.insert(DataBaseHelper.TABLE_FIL, null,
						cvUpdate);
			}

			cFil.close();
			cThreads.moveToNext();
		}
		cThreads.close();
	}

	public void generateFil() {
		Log.i(TAG, "SELECT " + DataBaseHelper.TABLE_FIL + "."
				+ DataBaseHelper.COLUMN_ID
				+ " THREAD_ID, count(sms.*) NB_MESSAGE, "
				+ DataBaseHelper.TABLE_SMS + "." + DataBaseHelper.COLUMN_BODY
				+ " EXTRAIT " + " FROM " + DataBaseHelper.TABLE_FIL + ", "
				+ DataBaseHelper.TABLE_SMS + " WHERE "
				+ DataBaseHelper.TABLE_FIL + "." + DataBaseHelper.COLUMN_ID
				+ " = " + DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_THREADID + " GROUP BY "
				+ DataBaseHelper.TABLE_SMS + "."
				+ DataBaseHelper.COLUMN_THREADID);

	}

	public Cursor getAll() {
		return getAll(allColumns);
	}

	public Cursor getAll(String[] columns) {
		Cursor c = database.query(DataBaseHelper.TABLE_FIL, columns, null,
				null, null, null, DataBaseHelper.COLUMN_ID + " ASC");
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
