package com.cnam.al_sms.gestionsms;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.data.datasource.SyncDataSource;
import com.cnam.al_sms.modeles.SMS;
import com.cnam.al_sms.modeles.SyncSMS;

public abstract class SynchroController {

	private static final String TAG = "ALSMS";

	public static SyncSMS enregistrerSyncPeriode(Context context,
			long idPremier, long idDernier) throws Exception {
		SyncDataSource sds = new SyncDataSource(context);
		sds.open();
		SyncSMS r = sds.addSyncSms(SyncDataSource.newSync(0, idPremier,
				idDernier));
		sds.close();
		return r;
	}

	public static SyncSMS enregistrerSyncVolee(Context context, long idPremier,
			long idDernier) throws Exception {
		SyncDataSource sds = new SyncDataSource(context);
		sds.open();
		SyncSMS r = sds.addSyncSms(SyncDataSource.newSync(1, idPremier,
				idDernier));
		sds.close();
		return r;
	}

	private static ProgressDialog dialog_getsms;

	public static void getAllSmsFromMasterBase(final Context context) {
		final Uri URI_SMS = Uri.parse("content://sms");
		final ContentResolver cr = context.getContentResolver();
		final SMSDataSource sds = new SMSDataSource(context);

		Cursor c_nb = context.getContentResolver().query(URI_SMS, null, null,
				null, null);
		final int nombreSMS = c_nb.getCount();
		c_nb.close();

		dialog_getsms = ProgressDialog.show(context,
				"Récupération des SMS de la base officiel.", "En cours", false);
		sds.open();

		long last_id = sds.getLastSMSId();
		final String[] whereArgs = new String[] { last_id + "" };

		Thread thread_premieresynchro = new Thread(new Runnable() {

			@Override
			public void run() {
				int progress = 0;
				int num_sms = 0;

				Cursor c = cr.query(URI_SMS, SMSDataSource.allColumns,
						DataBaseHelper.COLUMN_ID + " > ?", whereArgs,
						DataBaseHelper.COLUMN_ID);
				c.moveToFirst();

				while (!c.isAfterLast()) {
					ContentValues vals = new ContentValues();
					DatabaseUtils.cursorRowToContentValues(c, vals);
					long idsms = sds.creerSMS(vals);
					Log.i(TAG, "SMS id " + idsms + " copié.");
					progress = Math.round((num_sms / nombreSMS) * 100);
					/*
					 * dialog_getsms.setProgress(progress);
					 * dialog_getsms.setMessage(num_sms + "/" + nombreSMS);
					 */
					num_sms++;
					c.moveToNext();
				}
				c.close();
				sds.close();
				dialog_getsms.dismiss();
				SynchroController.updateFils(context);
			}
		});
		thread_premieresynchro.start();
	}

	private static ProgressDialog dialog_update;

	public static void updateFils(final Context context) {
		final FilDataSource fds = new FilDataSource(context);

		Thread thread_updateFil = new Thread(new Runnable() {

			@Override
			public void run() {
				fds.open();
				fds.updateFils();
				fds.close();
			}
		});
		thread_updateFil.start();
	}

	public static void getLastSMSId(Context context) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		sds.getLastSMSId();
		sds.close();
	}

	public static List<SMS> getSMSAfterDate(Context context) {
		SyncDataSource sds = new SyncDataSource(context);
		sds.open();
		List<SMS> r = sds.getSmsNotSync();
		sds.close();
		return r;
	}

	public static boolean synchroPeriode() {

		return false;
	}

	public static boolean synchroVollee() {

		return false;
	}

}
