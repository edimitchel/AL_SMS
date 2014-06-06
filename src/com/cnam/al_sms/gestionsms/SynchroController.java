package com.cnam.al_sms.gestionsms;

import java.sql.Date;
import java.util.List;

import shared.Globales;
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

	public static List<SMS> getSmsSince(Context context) {
		SyncDataSource syncds = new SyncDataSource(context);
		SMSDataSource sds = new SMSDataSource(context);
		syncds.open();
		sds.open();
		SyncSMS sSms = syncds.getLastSyncSMSDate();

		Date now = new Date(System.currentTimeMillis());
		Date dateLast = sSms == null ? now : sSms.getDateSync();
		if (dateLast.before(now)) {
			dateLast = now;
		}

		List<SMS> lSms = sds.getSmsAfterDate(dateLast);
		syncds.close();
		sds.close();
		return lSms;
	}

	public static List<SMS> getSMSAfterDate(Context context) {
		SyncDataSource sds = new SyncDataSource(context);
		sds.open();
		List<SMS> r = sds.getSmsNotSync();
		sds.close();
		return r;
	}

	public static boolean synchroPeriode(Context context) {
		SMSDataSource dataSMS = new SMSDataSource(
				Globales.curActivity.getApplicationContext());
		dataSMS.open();
		List<SMS> list = getSmsSince(context);
		long firstSMSId = list.get(0).getId();
		long lastSMSId = list.get(list.size() - 1).getId();

		try {
			enregistrerSyncPeriode(context, firstSMSId, lastSMSId);
		} catch (Exception e) {
			return false;
		}

		byte[] listbytes;
		listbytes = SMS.getBytesFromList(list);

		Globales.BTService.send(listbytes);

		dataSMS.close();
		return true;
	}

	public static boolean synchroVollee() {

		return false;
	}

}
