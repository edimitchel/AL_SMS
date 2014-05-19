package com.cnam.al_sms.modeles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.provider.Telephony.Sms;

public class Contact implements Serializable {

	public static byte[] getBytes(Contact contact) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] smsBytes = null;
		try {
			try {
				out = new ObjectOutputStream(bos);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.writeObject(contact);
			} catch (IOException e) {
				e.printStackTrace();
			}
			smsBytes = bos.toByteArray();

		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return smsBytes;

	}

	public static Contact getFromBytes(byte[] byteSms) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteSms);
		ObjectInput in = null;
		Contact contact = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			contact = (Contact) o;
			bis.close();
			if (in != null) {
				in.close();
			}
		} catch (StreamCorruptedException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return contact;

	}

	public static byte[] getBytesFromList(ArrayList<Contact> contacts) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] contactBytes = null;
		try {
			try {
				out = new ObjectOutputStream(bos);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.writeObject(contacts);
			} catch (IOException e) {
				e.printStackTrace();
			}
			contactBytes = bos.toByteArray();

		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return contactBytes;

	}

	public static ArrayList<Contact> getListFromBytes(byte[] byteContact) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteContact);
		ObjectInput in = null;
		ArrayList<Contact> ContactRe = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			ContactRe = (ArrayList<Contact>) o;
			bis.close();
			if (in != null) {
				in.close();
			}
		} catch (StreamCorruptedException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		return ContactRe;
	}
}
