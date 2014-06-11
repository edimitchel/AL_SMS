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
import java.sql.Date;
import java.util.List;

import android.content.ContentValues;

import com.cnam.al_sms.data.DataBaseHelper;

public class SMS implements Serializable {
	private static final long serialVersionUID = 3668704935456458913L;
	private long id;
	private long filId;
	private String adresse;
	private int personne;
	private Date date;
	private Date dateEnvoi;
	private int lu;
	private int statut;
	private int type;
	private String sujet;
	private String message;
	private int vu;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFilId() {
		return filId;
	}

	public void setFilId(long filId) {
		this.filId = filId;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public int getPersonne() {
		return personne;
	}

	public void setPersonne(int personne) {
		this.personne = personne;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public int getLu() {
		return lu;
	}

	public void setLu(int lu) {
		this.lu = lu;
	}

	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getVu() {
		return vu;
	}

	public void setVu(int vu) {
		this.vu = vu;
	}

	public SMS() {
	}

	public SMS(String message, Date date, int type) {
		this.message = message;
		this.date = date;
		this.type = type;
	}

	/**
	 * @param id
	 * @param fil_id
	 * @param adresse
	 * @param personne
	 * @param date
	 * @param date_envoi
	 * @param lu
	 * @param statut
	 * @param type
	 * @param sujet
	 * @param message
	 * @param vu
	 */
	public SMS(long id, long filId, String adresse, int personne, Date date,
			Date dateEnvoi, int lu, int statut, int type, String sujet,
			String message, int vu) {
		setId(id);
		setFilId(filId);
		setAdresse(adresse);
		setPersonne(personne);
		setDate(date);
		setDateEnvoi(dateEnvoi);
		setLu(lu);
		setStatut(statut);
		setType(type);
		setSujet(sujet);
		setMessage(message);
		setVu(vu);
	}

	public static byte[] getBytes(SMS sms) {
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
				out.writeObject(sms);
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

	public static SMS getFromBytes(byte[] byteSms) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteSms);
		ObjectInput in = null;
		SMS smsRe = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			smsRe = (SMS) o;
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

		return smsRe;

	}

	public static byte[] getBytesFromList(List<SMS> list) {
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
				out.writeObject(list);
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

	@SuppressWarnings("unchecked")
	public static ArrayList<SMS> getListFromBytes(byte[] byteSms) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteSms);
		ObjectInput in = null;
		ArrayList<SMS> smsRe = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			smsRe = (ArrayList<SMS>) o;
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

		return smsRe;

	}

	public ContentValues contentValuesFromSMS() {
		ContentValues cValues = new ContentValues();
		cValues.put(DataBaseHelper.COLUMN_ID, this.id);
		cValues.put(DataBaseHelper.COLUMN_THREADID, this.filId);
		cValues.put(DataBaseHelper.COLUMN_ADDRESS, this.adresse);
		cValues.put(DataBaseHelper.COLUMN_PERSON, this.personne);
		cValues.put(DataBaseHelper.COLUMN_DATE, this.date.toString());
		cValues.put(DataBaseHelper.COLUMN_DATESENT, this.dateEnvoi.toString());
		cValues.put(DataBaseHelper.COLUMN_READ, this.lu);
		cValues.put(DataBaseHelper.COLUMN_STATUS, this.statut);
		cValues.put(DataBaseHelper.COLUMN_TYPE, this.type);
		cValues.put(DataBaseHelper.COLUMN_SUBJECT, this.sujet);
		cValues.put(DataBaseHelper.COLUMN_BODY, this.message);
		cValues.put(DataBaseHelper.COLUMN_SEEN, this.vu);
		return cValues;
	}
}
