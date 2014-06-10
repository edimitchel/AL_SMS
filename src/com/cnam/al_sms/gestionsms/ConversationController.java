package com.cnam.al_sms.gestionsms;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import shared.Globales;
import shared.TacheSMSFromMaster;
import shared.TacheUpdate;
import android.content.Context;
import android.text.format.DateFormat;

public abstract class ConversationController {

	public static String[] mois = new String[] { "janvier", "février", "mars",
			"avril", "mai", "juin", "juillet", "août", "septembre", "octobre",
			"novembre", "décembre" };

	public static String[] semaines = new String[] { "lundi", "mardi",
			"mercredi", "jeudi", "vendredi", "samedi", "dimanche" };

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

	public static String getDateTime(Date date) {
		long diffNow = (new Date(System.currentTimeMillis()).getTime() - date
				.getTime()) / 1000;

		String dateStr = (String) DateFormat.format(Globales.dateFormatString,
				date);
		if (diffNow < 60) {
			dateStr = "à l'instant";
		} else if (diffNow < 3600) {
			int min = (int) diffNow / 60;
			dateStr = "il y a " + min + " minute" + (min > 1 ? "s" : "");
		} else if (diffNow < 3600 * 24) {
			int heu = (int) diffNow / 3600;
			dateStr = "il y a " + heu + " heure" + (heu > 1 ? "s" : "");
		} else {
			Calendar c = new GregorianCalendar();
			c.setTime(date);
			dateStr = semaines[c.get(Calendar.DAY_OF_WEEK) - 1].substring(0, 3)
					+ ". " + c.get(Calendar.DATE) + " "
					+ mois[c.get(Calendar.DAY_OF_MONTH) - 1];
		}

		return dateStr;
	}
}
