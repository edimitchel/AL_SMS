package com.cnam.al_sms.Modeles;

import java.io.Serializable;
import java.util.Date;

public class SMS implements Serializable {
	private static final long serialVersionUID = 3668704935456458913L;
	private long id;
	private long fil_id;
	private String adresse;
	private int personne;
	private Date date;
	private Date date_envoi;
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

	public long getFil_id() {
		return fil_id;
	}

	public void setFil_id(long fil_id) {
		this.fil_id = fil_id;
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

	public Date getDate_envoi() {
		return date_envoi;
	}

	public void setDate_envoi(Date date_envoi) {
		this.date_envoi = date_envoi;
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
	public SMS(long id, long fil_id, String adresse, int personne, Date date,
			Date date_envoi, int lu, int statut, int type, String sujet,
			String message, int vu) {
		setId(id);
		setFil_id(fil_id);
		setAdresse(adresse);
		setPersonne(personne);
		setDate(date);
		setDate_envoi(date_envoi);
		setLu(lu);
		setStatut(statut);
		setType(type);
		setSujet(sujet);
		setMessage(message);
		setVu(vu);
	}
}
