package com.cnam.al_sms.GestionSms;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.util.Log;

import com.cnam.al_sms.Data.DataSource.SMSDataSource;
import com.cnam.al_sms.Data.DataSource.SyncDataSource;
import com.cnam.al_sms.Modeles.SyncSMS;

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

	public static void getAllSmsFromMaster(Context context) {
		ContentResolver cr = context.getContentResolver();
		Uri uri_sms = Uri.parse("content://sms");
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();

		Cursor c = cr.query(uri_sms, SMSDataSource.allColumns, null, null,
				Sms.DATE + " ASC");
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ContentValues vals = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(c, vals);
			long idsms = sds.creerSMS(vals);
			Log.i(TAG, "SMS id " + idsms + " copié.");
			c.moveToNext();
		}
		sds.close();
	}

	public static boolean synchroPeriode() {

		return false;
	}

	public static boolean synchroVollee() {

		return false;
	}

}
