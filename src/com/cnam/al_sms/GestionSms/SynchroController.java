package com.cnam.al_sms.gestionsms;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.util.Log;

import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.data.datasource.SyncDataSource;
import com.cnam.al_sms.modeles.SyncSMS;

public class SynchroController {

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

	private static ProgressDialog dialog;

	/**
	 * /!\ Il faut trouver un moyen de faire comme ça :
	 * http://stackoverflow.com/
	 * questions/3343490/progressdialog-working-in-thread-on-android
	 * 
	 * @param context
	 */
	public static void getAllSmsFromMaster(final Context context) {
		final Uri URI_SMS = Uri.parse("content://sms");
		final ContentResolver cr = context.getContentResolver();
		final SMSDataSource sds = new SMSDataSource(context);

		Cursor c_nb = context.getContentResolver().query(URI_SMS, null, null,
				null, null);
		final int nombreSMS = c_nb.getCount();
		c_nb.close();

		dialog = ProgressDialog.show(context,
				"Récupération des SMS de la base officiel.", "En cours");
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);

		sds.open();
		
		Thread thread_premieresynchro = new Thread(new Runnable() {

			@Override
			public void run() {
				int progress = 0;
				int num_sms = 0;

				Cursor c = cr.query(URI_SMS, SMSDataSource.allColumns, null,
						null, Sms.DATE + " ASC");
				c.moveToFirst();

				while (!c.isAfterLast()) {
					ContentValues vals = new ContentValues();
					DatabaseUtils.cursorRowToContentValues(c, vals);
					long idsms = sds.creerSMS(vals);
					Log.i(TAG, "SMS id " + idsms + " copié.");
					num_sms++;
					progress = Math.round((num_sms / nombreSMS) * 100);
					dialog.setProgress(progress);
					c.moveToNext();
				}
				sds.close();
				dialog.dismiss();
			}
		});
		thread_premieresynchro.start();
	}	/**
	 * /!\ Il faut trouver un moyen de faire comme ça :
	 * http://stackoverflow.com/
	 * questions/3343490/progressdialog-working-in-thread-on-android
	 * 
	 * @param context
	 */
	public static void updateFils(final Context context) {
		final FilDataSource fds = new FilDataSource(context);

		dialog = ProgressDialog.show(context,
				"Mise à jour des fils de conversations", "En cours");
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setIndeterminate(false);

		fds.open();
		
		Thread thread_updateFil = new Thread(new Runnable() {

			@Override
			public void run() {
				fds.updateFils();
				fds.close();
				dialog.dismiss();
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

	public static boolean synchroPeriode() {

		return false;
	}

	public static boolean synchroVollee() {

		return false;
	}

}
