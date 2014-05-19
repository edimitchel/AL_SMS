package com.cnam.al_sms.gestionsms;

import java.util.List;

import shared.TacheSMSFromMaster;
import shared.TacheUpdate;
import android.app.Activity;
import android.content.Context;

import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.data.datasource.SyncDataSource;
import com.cnam.al_sms.modeles.SMS;
import com.cnam.al_sms.modeles.SyncSMS;

public abstract class SynchroController {

	@SuppressWarnings("unused")
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

	public static void getAllSmsFromMasterBase(Activity activity) {
		new TacheSMSFromMaster(activity).execute();
	}

	public static void updateFils(Activity activity) {
		new TacheUpdate(activity).execute();
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
