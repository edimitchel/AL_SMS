package com.cnam.al_sms.modeles;

import java.util.Date;

public class SyncSMS {

	private long idSync;
	private Date dateSync;
	private int type;
	private long idPremierSMS;
	private long idDernierSMS;

	public long getIdSync() {
		return idSync;
	}

	public void setIdSync(long idSync) {
		this.idSync = idSync;
	}

	public Date getDateSync() {
		return dateSync;
	}

	public void setDateSync(Date dateSync) {
		this.dateSync = dateSync;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getIdPremierSMS() {
		return idPremierSMS;
	}

	public void setIdPremierSMS(long idPremierSMS) {
		this.idPremierSMS = idPremierSMS;
	}

	public long getIdDernierSMS() {
		return idDernierSMS;
	}

	public void setIdDernierSMS(long idDernierSMS) {
		if (idDernierSMS >= idPremierSMS)
			this.idDernierSMS = idDernierSMS;
		else
			this.idDernierSMS = 0;
	}

	public SyncSMS() {
		
	}

	/**
	 * @param idSync Identifiant de la synchronisation 
	 * @param dateSync Date de la synchronisation
	 * @param type Type de la synchronisation
	 * @param idPremierSMS
	 * @param idDernierSMS
	 */
	public SyncSMS(long idSync, Date dateSync, int type,
			long idPremierSMS, long idDernierSMS) {
		super();
		setIdSync(idSync);
		setDateSync(dateSync);
		setType(type);
		setIdPremierSMS(idPremierSMS);
		setIdDernierSMS(idDernierSMS);
	}
}