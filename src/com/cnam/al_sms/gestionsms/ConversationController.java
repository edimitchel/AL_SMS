package com.cnam.al_sms.gestionsms;

import shared.TacheSMSFromMaster;
import shared.TacheUpdate;
import android.app.Activity;

public abstract class ConversationController {

	public ConversationController() {
		// TODO Auto-generated constructor stub
	}
	
	public static void getAllSmsFromMasterBase(Activity activity) {
		new TacheSMSFromMaster(activity).execute();
	}

	public static void updateFils(Activity activity) {
		new TacheUpdate(activity).execute();
	}
}
