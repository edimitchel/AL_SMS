package com.cnam.al_sms.gestionsms;

import java.util.Iterator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.SMSDataSource;

public abstract class ContactController {
	// Fonction permettant de rcupérer les cotact du téléphone
	public static void getContactFromMasterBase(Context context) {
		int cpt = 0;
		ContentResolver cr = context.getContentResolver();

		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext() && cpt < 11) {
						String phoneNo = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Toast.makeText(context.getApplicationContext(),
								"Name: " + name + ", Phone No: " + phoneNo,
								Toast.LENGTH_SHORT).show();
						cpt++;
					}
					pCur.close();
				}
			}
		}
	}

	public static String getContactByThread(long thread_id, Context context) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		Cursor sms = sds.getAllOfThread(thread_id);

		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		CursorJoiner cj = new CursorJoiner(sms, new String[] { DataBaseHelper.COLUMN_PERSON }, cur, new String[] { ContactsContract.Contacts._ID });
		
		for(CursorJoiner.Result joinerResult : cj){
			switch (joinerResult) {
			case LEFT:
				
				break;
			case RIGHT:
				
				break;
			case BOTH:
					Iterator it = cj.iterator();
				break;
			}
		}
		return "0605116117";
	}
}
