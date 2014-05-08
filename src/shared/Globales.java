package shared;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Globales {
	public static DeviceType typeAppareil;
	/* enum */
	/* type de périphériques : phone ou tablette */
	public enum DeviceType{ phone, tablette	}
	
	public static DeviceType getDeviceType(Context context){
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			Globales.typeAppareil =  Globales.DeviceType.tablette;
		} else {
			Globales.typeAppareil =  Globales.DeviceType.phone;
		}
		return Globales.typeAppareil;
		
		
		

	}
	
	/* CONSTANTES */
    public static final String TOAST = "toast";

	
	
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
}
