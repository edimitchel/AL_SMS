package com.cnam.al_sms.GestionSms;

import android.content.Context;

import com.cnam.al_sms.Data.DataSource.SyncDataSource;
import com.cnam.al_sms.Modeles.SyncSMS;

public class SynchroController {

	public static SyncSMS enregistrerSyncPeriode(Context context,
			long idPremier, long idDernier) {
		SyncDataSource sds = new SyncDataSource(context);
		sds.open();
		SyncSMS r = sds.addSyncSms(SyncDataSource.newSync(0, idPremier,
				idDernier));
		sds.close();
		return r;
	}

	public static SyncSMS enregistrerSyncVolee(Context context,
			long idPremier, long idDernier) {
		SyncDataSource sds = new SyncDataSource(context);
		sds.open();
		SyncSMS r = sds.addSyncSms(SyncDataSource.newSync(1, idPremier,
				idDernier));
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
