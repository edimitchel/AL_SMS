package com.cnam.al_sms.gestionsms;

import shared.Globales;
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
import com.cnam.al_sms.modeles.Contact;

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

	public static String getContactNameByThread(long thread_id, Context context) {
		Contact c = getContact(context, thread_id);
		return c.getNom();
	}

	public static Contact getContact(Context context, Long thread_id) {
		SMSDataSource sds = new SMSDataSource(context);
		sds.open();
		Cursor contact = sds.getSmsOfThread(thread_id);
		if (contact.getCount() == 0) {
			return new Contact(0L, "null", "null", null, 0L);
		}

		Contact newContact = new Contact();

		String number = contact.getString(contact
				.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ADDRESS));

		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));

		Cursor cur = cr.query(uri, null, null, null, null);
		contact.close();
		Long contactId;
		String contactName;
		Uri imageUri;
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			contactId = cur.getLong(cur
					.getColumnIndex(ContactsContract.Contacts._ID));
			contactName = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			imageUri = ContactController.openPhoto(contactId, context);

			newContact.setId(contactId);
			newContact.setNom(contactName);
			newContact.setNumero(number);
			newContact.setImageURI(imageUri);
			newContact.setThreadId(thread_id);
			
		} else {
			newContact.setId(0L);
			newContact.setNom(number);
			newContact.setNumero(number);
			newContact.setThreadId(thread_id);
		}
		cur.close();
		sds.close();
		return newContact;
	}

	public static Uri getContactImageUriByThread(long thread_id, Context context) {
		Contact c = getContact(context, thread_id);
		return openPhoto(c.getId(), context);
	}

	public static Uri openPhoto(long contactId, Context context) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { Contacts.Photo.PHOTO }, null, null, null);
		if (cursor == null || cursor.getCount() == 0) {
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
