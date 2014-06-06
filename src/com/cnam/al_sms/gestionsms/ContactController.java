package com.cnam.al_sms.gestionsms;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.Toast;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.SMSDataSource;

public abstract class ContactController {
	private static final String TAG = "ALSMS";

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
		Cursor contact = sds.getSmsOfThread(thread_id);
		if (contact.getCount() == 0) {
			return "";
		}
		String number = contact.getString(contact
				.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ADDRESS));

		ContentResolver cr = context.getContentResolver();

		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));

		Cursor cur = cr.query(uri, null, null, null, null);
		contact.close();
		String contactName;
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			contactName = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		} else {
			contactName = number;
		}
		cur.close();
		return contactName;
	}

	public static Uri getContactImageUriByThread(long thread_id, Context context) {
		ContentResolver cr = context.getContentResolver();
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		Cursor contact = sds.getSmsOfThread(thread_id);
		if (contact.getCount() == 0) {
			return null;
		}
		String number = contact.getString(contact
				.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ADDRESS));
		contact.close();

		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));

		Cursor cur = cr.query(uri, null, null, null, null);

		Long contactId;

		if (cur.getCount() > 0) {
			cur.moveToFirst();
			contactId = Long.valueOf(cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts._ID)));
		} else {
			return null;
		}
		cur.close();

		return openPhoto(contactId, context);
	}

	public static Uri openPhoto(long contactId, Context context) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { Contacts.Photo.PHOTO }, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				return photoUri;
			}
		} finally {
			cursor.close();
		}
		return null;
	}
}
