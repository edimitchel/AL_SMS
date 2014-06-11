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
			DataBaseHelper.COLUMN_SNIPPET, DataBaseHelper.COLUMN_MESSAGECOUNT,
			DataBaseHelper.COLUMN_DATE };
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
		
		Fil f = (Fil) cursorToFil(c);
		c.close();
		return f;
	}

	public void updateFils(Long _threadId) {

		final String DATE_S = "dateS";

		ArrayList<String> cols = new ArrayList<String>();
		for (int i = 0; i < SMSDataSource.allColumns.length; i++) {
			cols.add(SMSDataSource.allColumns[i]);

		}
		cols.add("" + DATE_S);

		String where = DataBaseHelper.COLUMN_THREADID + " = ?";
		String[] whereArgs = new String[] { _threadId + "" };

		if (_threadId == null) {
			where = null;
			whereArgs = null;
		}

		Cursor cThreads = database.query(DataBaseHelper.TABLE_SMS,
				new String[] {
						DataBaseHelper.COLUMN_THREADID,
						DataBaseHelper.COLUMN_BODY,
						"count(*) " + DataBaseHelper.COLUMN_MESSAGECOUNT,
						"max(" + DataBaseHelper.COLUMN_DATE + ") "
								+ DataBaseHelper.COLUMN_DATE }, where,
				whereArgs, DataBaseHelper.COLUMN_THREADID, null,
				DataBaseHelper.COLUMN_THREADID);

		cThreads.moveToFirst();
		while (!cThreads.isAfterLast()) {
			long thread_id = cThreads.getLong(cThreads
					.getColumnIndex(DataBaseHelper.COLUMN_THREADID));
			int nb_message = cThreads.getInt(cThreads
					.getColumnIndex(DataBaseHelper.COLUMN_MESSAGECOUNT));
			String extrait = cThreads.getString(cThreads
					.getColumnIndex(DataBaseHelper.COLUMN_BODY));
			long date = cThreads.getLong(cThreads
					.getColumnIndex(DataBaseHelper.COLUMN_DATE));

			ContentValues cvUpdate = new ContentValues();
			cvUpdate.put(DataBaseHelper.COLUMN_MESSAGECOUNT, nb_message);
			cvUpdate.put(DataBaseHelper.COLUMN_SNIPPET, extrait);
			cvUpdate.put(DataBaseHelper.COLUMN_DATE, date);

			Cursor cFil = database.query(DataBaseHelper.TABLE_FIL, null,
					DataBaseHelper.COLUMN_ID + " = ?",
					new String[] { String.valueOf(thread_id) }, null, null,
					null);

			if (cFil.getCount() == 1) {
				database.update(DataBaseHelper.TABLE_FIL, cvUpdate,
						DataBaseHelper.COLUMN_ID + " = ?",
						new String[] { String.valueOf(thread_id) });
				Log.i(TAG, "Mise à jour du fil de conversation n°" + thread_id);
			} else {
				cvUpdate.put(DataBaseHelper.COLUMN_ID, thread_id);
				database.insert(DataBaseHelper.TABLE_FIL, null, cvUpdate);
				Log.i(TAG, "Insertion fil de conversation n°" + thread_id);
			}

			cFil.close();
			cThreads.moveToNext();
		}
		cThreads.close();
	}

	public Cursor getAll() {
		return getAll(allColumns);
	}

	public Cursor getAll(String[] columns) {
		Cursor c = database.query(DataBaseHelper.TABLE_FIL, null, null, null,
				null, null, DataBaseHelper.COLUMN_DATE + " DESC");
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
			return fil;
		}
		return null;
	}

}
