package com.cnam.al_sms.connectivite;

import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class ConnecBluetooth {
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	private final static int REQUEST_CODE_VISIBILITY_BLUETOOTH = 1;
	public final static UUID APPUUID = UUID
			.fromString("a60f35f0-b93a-11de-8a39-08102009c666");
	public final static String APPNAME = "ALSMS";

	/**
	 * 
	 * @param a
	 * @return un booleen indiquant si le bluetooth est activité
	 * @throws Exception
	 *             si le bluetooth n'est pas supproté
	 */
	public static boolean checkBluetoothConnection(Activity a) throws Exception {
		boolean res = true;

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			throw new Exception("Bluetooth non supporté sur cet appareil");
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

	/**
	 * Demande à changer la visiblité et à activer le bluetooth s'il le faut.
	 * @param a
	 * @return toujours true
	 * @throws Exception
	 *             si le bluetooth n'est pas supproté
	 */
	public static boolean checkBluetoothVisibility(Activity a) throws Exception {
		boolean res = true;

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			throw new Exception("Bluetooth non supporté sur cet appareil");
		}

		if (checkBluetoothConnection(a)) {
			Intent changeVisibilityBlueTooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			a.startActivityForResult(changeVisibilityBlueTooth,
					REQUEST_CODE_VISIBILITY_BLUETOOTH);
		}
		return res;
	}

}
