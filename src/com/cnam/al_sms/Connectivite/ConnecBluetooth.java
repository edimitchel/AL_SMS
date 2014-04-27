package com.cnam.al_sms.Connectivite;

import java.util.UUID;

import com.cnam.al_sms.Al_SMS;
import com.cnam.al_sms.Esclave_Activities.MainActivity;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

public class ConnecBluetooth {
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	public final static UUID APPUUID = UUID
			.fromString("a60f35f0-b93a-11de-8a39-08102009c666");
	public final static String APPNAME = "ALSMS";
	private static boolean allowConnection = false;
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
	
	public static boolean askAcceptBluetooth(BluetoothDevice bd,Context c){
		ConnecBluetooth.allowConnection = false;
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	        	ConnecBluetooth.allowConnection = true;	           
	        		break;

	        case DialogInterface.BUTTON_NEGATIVE:
	        	ConnecBluetooth.allowConnection = false;
	            break;
	        }
	    }
	};

	//Log.i("ALSMS-test",Al_SMS.getAppContext().toString());
	AlertDialog.Builder builder = new AlertDialog.Builder(c);
	builder.setMessage("Se connecter au péréphique suivant : "+bd.getName() +"?").setPositiveButton("Ok", dialogClickListener)
	    .setNegativeButton("Annuler", dialogClickListener).show();
	return ConnecBluetooth.allowConnection = false;
	}
}
