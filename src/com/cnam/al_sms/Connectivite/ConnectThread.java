package com.cnam.al_sms.Connectivite;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

class ConnectThread extends Thread {
		   private final BluetoothSocket mmSocket;
		   private final BluetoothDevice mmDevice;
		   private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


		   public ConnectThread(BluetoothDevice device) {
		       BluetoothSocket tmp = null;
		       mmDevice = device;
		       try {
		           tmp = device.createRfcommSocketToServiceRecord(ConnecBluetooth.uuid);
		       } catch (IOException e) { }
		       mmSocket = tmp;
		   }

		   public void run() {
		       mBluetoothAdapter.cancelDiscovery();
		       try {
		           mmSocket.connect();
		       } catch (IOException connectException) {
		           try {
		               mmSocket.close();
		           } catch (IOException closeException) { }
		           return;
		       }
		        //DO SOMETHING
		   }

		   public void cancel() {
		       try {
		           mmSocket.close();
		       } catch (IOException e) { }
		   }

		
}
