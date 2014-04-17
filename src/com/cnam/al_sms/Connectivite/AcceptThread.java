package com.cnam.al_sms.Connectivite;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class AcceptThread extends Thread {
	private final BluetoothServerSocket mmServerSocket;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	public AcceptThread() {
		BluetoothServerSocket tmp = null;
		try {
			tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
					ConnecBluetooth.APPNAME, ConnecBluetooth.APPUUID);
		} catch (IOException e) {
		}
		mmServerSocket = tmp;
	}

	public void run() {
		BluetoothSocket socket = null;
		while (true) {
			try {
				socket = mmServerSocket.accept();
			} catch (IOException e) {
				break;
			}

			if (socket != null) {
				// DO SOMETHING
				try {
					mmServerSocket.close();
				} catch (IOException e) {
					break;
				}
				break;
			}
		}
	}

	public void cancel() {
		try {
			mmServerSocket.close();
		} catch (IOException e) {
		}
	}

}
