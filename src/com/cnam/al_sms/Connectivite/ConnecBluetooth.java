package com.cnam.al_sms.connectivite;

import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class ConnecBluetooth {
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	public final static UUID APPUUID = UUID
			.fromString("a60f35f0-b93a-11de-8a39-08102009c666");
	public final static String APPNAME = "ALSMS";

	/**
	 * 
	 * @param a
	 * @return un booleen indiquant si le bluetooth est activit�
	 * @throws Exception
	 *             si le bluetooth n'est pas supprot�
	 */
	public static boolean checkBluetoothConnection(Activity a) throws Exception {
		boolean res = true;

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			throw new Exception("Bluetooth non support� sur cet appareil");
		}

		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBlueTooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			a.startActivityForResult(enableBlueTooth,
					REQUEST_CODE_ENABLE_BLUETOOTH);
			if (!bluetoothAdapter.isEnabled()) {
				res = false;
			}
		}
		return res;

	}

}
