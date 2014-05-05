package com.cnam.al_sms;

public class Globales {
	public static DeviceType typeAppareil;
	/* enum */
	/* type de périphériques : phone ou tablette */
	public enum DeviceType{ phone, tablette	}
	
	/* CONSTANTES */
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
}
