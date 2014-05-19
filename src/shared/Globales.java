package shared;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.esclave_activities.AlsmsActivity;
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
		if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			Globales.typeAppareil = Globales.DeviceType.tablette;
		} else {
			Globales.typeAppareil = Globales.DeviceType.phone;
		}
		return Globales.typeAppareil;

	}

	/* BLUETOOTH SERVICE */
	public static BluetoothService BTService;

	/* Activity Courantes */
	public static AlsmsActivity curActivity;

	/* CONSTANTES */
	public static final String TOAST = "toast";

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_CONNECTED = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_FAILED = 6;
	public static final int MESSAGE_LOST = 7;

	/* Nombre de messages à afficher (premettre de la configurer) */

	public static final int MESSAGE_COUNT_AFFICHER = 150;

	public final static Handler mHandler = new Handler() {
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
			case Globales.MESSAGE_READ:
				byte[] readBuff = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				ArrayList<SMS> readMessage = SMS.getListFromBytes(readBuff);
				// String readMessage = new String(readBuff, 0, msg.arg1);
				Toast.makeText(curActivity, readMessage.get(0).getMessage(),
						Toast.LENGTH_SHORT).show();

				Toast.makeText(curActivity, readMessage.get(1).getMessage(),
						Toast.LENGTH_SHORT).show();
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
}
