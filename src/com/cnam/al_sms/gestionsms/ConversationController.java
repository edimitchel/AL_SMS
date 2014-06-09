package com.cnam.al_sms.gestionsms;

import shared.TacheSMSFromMaster;
import shared.TacheUpdate;
import android.app.Activity;
import android.content.Context;

public abstract class ConversationController {

	public ConversationController() {
		// TODO Auto-generated constructor stub
	}
	
	public static void getAllSmsFromMasterBase(Context context, boolean show) {
		new TacheSMSFromMaster(context, show).execute();
	}

	public static void updateFils(Context context, boolean show) {
		new TacheUpdate(context, show).execute();
	}

	public static void updateFils(Context context, long threadId, boolean show) {
		new TacheUpdate(context, threadId, show).execute();
	}
}
