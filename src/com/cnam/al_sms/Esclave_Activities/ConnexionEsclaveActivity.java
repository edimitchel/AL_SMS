package com.cnam.al_sms.Esclave_Activities;

import com.cnam.al_sms.Connectivite.BluetoothService;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ConnexionEsclaveActivity extends Activity{
    private BluetoothService bTService=new BluetoothService(this, new Handler());

	
	/* Liaison Bluetooth */
	/*private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (bTService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "Pas connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            bTService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
           
        }
    }
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if(D) Log.d(TAG, "onActivityResult " + resultCode);
	        switch (requestCode) {
	        case REQUEST_CONNECT_DEVICE:
	            // When DeviceListActivity returns with a device to connect
	            if (resultCode == Activity.RESULT_OK) {
	                // Get the device MAC address
	                String address = data.getExtras().getString("Adresse_MAC");
	                Log.i(TAG, "adress: "+address);
					// Get the BLuetoothDevice object
	                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	                // Attempt to connect to the device
	                bTService.connect(device);
	            }
	            break;
	        case REQUEST_ENABLE_BT:
	            // When the request to enable Bluetooth returns
	            if (resultCode == Activity.RESULT_OK) {
	                // Bluetooth is now enabled, so set up a chat session
	                
	            } else {
	                // User did not enable Bluetooth or an error occured
	                Log.d(TAG, "BT not enabled");
	                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
	                finish();
	            }
	        }
	    }*/
}
