package com.cnam.al_sms.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_FIL = "fil";
	public static final String TABLE_SMS = "sms";
	public static final String TABLE_SYNCHRONISATION = "synchronisation";

	/*
	 * Champs communs
	 */
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TYPE = "type";

	/*
	 * Champs SMS
	 */
	public static final String COLUMN_THREADID = "thread_id";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_PERSON = "person";
	public static final String COLUMN_DATESENT = "date_sent";
	public static final String COLUMN_READ = "read";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_SEEN = "seen";

	/*
	 * Champs Fil (Conversation
	 */
	public static final String COLUMN_SNIPPET = "snippet";
	public static final String COLUMN_MESSAGECOUNT = "message_count";

	/**
	 * <h1>Type de la synchronisation</h1>
	 * <ul>
	 * <li>0: Synchronisation sur période</li>
	 * <li>1: Synchronisation à la volée</li>
	 * </ul>
	 */
	public static final String COLUMN_FISRTSMS = "first_sms";
	public static final String COLUMN_LASTSMS = "last_sms";

	private static final String DATABASE_NAME = "alsms.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_SMS_CREATE = "create table " + TABLE_SMS
			+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_THREADID + " INTEGER," + COLUMN_ADDRESS + " TEXT,"
			+ COLUMN_PERSON + " INTEGER," + COLUMN_DATE + " INTEGER,"
			+ COLUMN_DATESENT + " INTEGER DEFAULT 0," + COLUMN_READ
			+ " INTEGER DEFAULT 0," + COLUMN_STATUS + " INTEGER DEFAULT -1,"
			+ COLUMN_TYPE + " INTEGER," + COLUMN_SUBJECT + " TEXT,"
			+ COLUMN_BODY + " TEXT," + COLUMN_SEEN + " INTEGER DEFAULT 0"
			+ ");";

	private static final String TABLE_SYNC_CREATE = "create table "
			+ TABLE_SYNCHRONISATION + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + " INTEGER," + COLUMN_DATE
			+ " INTEGER," + COLUMN_TYPE + " INTEGER," + COLUMN_FISRTSMS
			+ " INTEGER," + COLUMN_LASTSMS + " INTEGER" + ");";

	private static final String TABLE_FIL_CREATE = "create table " + TABLE_FIL
			+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ " INTEGER," + COLUMN_SNIPPET + " TEXT," + COLUMN_MESSAGECOUNT
			+ " INTEGER" + ");";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DataBaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(DataBaseHelper.class.getName(), "Création de la table SMS, FIL et SYNCHRONISATION");
		db.execSQL(TABLE_SMS_CREATE);
		db.execSQL(TABLE_SYNC_CREATE);
		db.execSQL(TABLE_FIL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != newVersion) {
			Log.w(DataBaseHelper.class.getName(),
					"Mise à jour de la base de donnée depuis la version  " + oldVersion + " vers la version "
							+ newVersion + ", les données seront supprimées.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIL);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCHRONISATION);
			onCreate(db);
		}
	}

}
