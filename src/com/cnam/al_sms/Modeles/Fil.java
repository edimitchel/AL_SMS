package com.cnam.al_sms.modeles;

public class Fil {
	private long filId;
	private String extrait;
	private int nombreMessage;

	public long getFilId() {
		return filId;
	}

	public void setFilId(long filId) {
		this.filId = filId;
	}

	public String getExtrait() {
		return extrait;
	}

	public void setExtrait(String extrait) {
		this.extrait = extrait;
	}

	public int getNombreMessage() {
		return nombreMessage;
	}

	public void setNombreMessage(int nombreMessage) {
		this.nombreMessage = nombreMessage;
	}

	/**
	 * @param filId
	 * @param extrait
	 * @param nombre_message
	 */
	public Fil(long filId, String extrait, int nombre_message) {
		super();
		setFilId(filId);
		setExtrait(extrait);
		setNombreMessage(nombre_message);
	}

	public Fil() {
		// TODO Auto-generated constructor stub
	}

}
