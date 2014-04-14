package com.cnam.al_sms.Connectivite;

import com.cnam.al_sms.Esclave_Activities.MainActivity;

import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ConnecBluetooth {
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	
	public static boolean checkBluetoothConnection(Activity a){
		
		
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null){
			if (!bluetoothAdapter.isEnabled()) {
				   Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				   a.startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
				}
		}
		else{}
		return false;
			
		   //Toast.makeText(AL_SMS.this, "Avec Bluetooth",        Toast.LENGTH_SHORT).show();
	}
}
