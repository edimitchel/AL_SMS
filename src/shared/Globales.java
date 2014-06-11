package shared;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.esclave_activities.AlsmsActivity;
import com.cnam.al_sms.esclave_activities.MainActivity;
import com.cnam.al_sms.gestionsms.ConversationController;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.modeles.SMS;

public class Globales {
	public static DeviceType typeAppareil;

	/* enum */
	/* type de périphériques : phone ou tablette */
	public enum DeviceType {
		phone, tablette
	}

	public static DeviceType getDeviceType(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean isSlave = prefs.getBoolean("as_slave", false);

		if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE
				|| isSlave) {
			Globales.typeAppareil = Globales.DeviceType.tablette;
		} else {
			Globales.typeAppareil = Globales.DeviceType.phone;
		}
		return Globales.typeAppareil;
	}

	public static boolean isPhone() {
		return getDeviceType(curActivity) == DeviceType.phone;
	}

	public static boolean isTablet() {
		return getDeviceType(curActivity) == DeviceType.tablette;
	}

	/* BLUETOOTH SERVICE */
	public static BluetoothService BTService;

	/* Activity Courantes */
	public static AlsmsActivity curActivity;

	/* CONSTANTES */
	public static final String TOAST = "toast";

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_RECEIVED = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_CONNECTED = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_FAILED = 6;
	public static final int MESSAGE_LOST = 7;

	/* Nombre de messages à afficher (premettre de la configurer) */

	public static final int MESSAGE_COUNT_AFFICHER = 150;

	public final static Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			String readMessag;
			switch (msg.what) {
			case Globales.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:

					break;
				case BluetoothService.STATE_CONNECTING:
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					break;
				}
				break;
			case Globales.MESSAGE_WRITE:
				break;
			case Globales.MESSAGE_RECEIVED:
				byte[] readBuff = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				SMS sms = SMS.getFromBytes(readBuff);
				// String readMessage = new String(readBuff, 0, msg.arg1);

				SMSDataSource smsdata = new SMSDataSource(curActivity);
				smsdata.open();
				smsdata.creerSMS(sms.contentValuesFromSMS());
				smsdata.close();
				ConversationController.updateFils(
						curActivity.getApplicationContext(), sms.getFilId());

				if (Globales.isPhone()) {
					MessagerieController.sendSMS(Globales.curActivity,
							"0605116117", sms.getMessage());
					// 0687974971 or 0605116117

					Toast.makeText(
							curActivity,
							"Envoi d'un sms " + ":\"" + sms.getMessage() + "\"",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(curActivity, "Récupération de SMS en cours",
							Toast.LENGTH_SHORT).show();
					if (curActivity instanceof MainActivity) {
						((MainActivity) curActivity).refreshConv(null);
					}
				}

				break;
			case Globales.MESSAGE_CONNECTED:
				// construct a string from the valid bytes in the buffer
				String mConnectedDeviceName = msg.getData().getString("Device");
				Toast.makeText(curActivity,
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				curActivity.onConnected();
				break;
			case Globales.MESSAGE_TOAST:
				// construct a string from the valid bytes in the buffer
				readMessag = msg.getData().getString(Globales.TOAST);
				Toast.makeText(curActivity, readMessag, Toast.LENGTH_SHORT)
						.show();
				break;

			case Globales.MESSAGE_FAILED:
				// construct a string from the valid bytes in the buffer
				readMessag = msg.getData().getString(Globales.TOAST);
				Toast.makeText(curActivity, readMessag, Toast.LENGTH_SHORT)
						.show();
				curActivity.onFailedConnection();
				break;

			case Globales.MESSAGE_LOST:
				// construct a string from the valid bytes in the buffer
				readMessag = msg.getData().getString(Globales.TOAST);
				Toast.makeText(curActivity, readMessag, Toast.LENGTH_SHORT)
						.show();

				break;
			}
		}
	};

	/**
	 * Intervalle de temps pour la synchronisation des SMS en millisecondes
	 * Surchargé par les préférences default: 24 h
	 */
	public static final long INTERVALLE_TEMPS_SYNC = 60 * 60 * 24;

	public static CharSequence dateFormatString = "dd/MM/yy à hh/h:mm";

	public static android.text.format.DateFormat dateFormat;

	public static void init(Context context) {
		dateFormat = new android.text.format.DateFormat();
	}
}
