package com.cnam.al_sms.maitre_activities;

import java.util.ArrayList;

import shared.Globales;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.modeles.SMS;

public class ConnexionMaitreActivity extends Activity {
    // Member object for the chat services
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion_maitre);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		Globales.BTService =  new BluetoothService(this, mHandler);
		 if (Globales.BTService != null) {
	            // Seulement si le statut de la connection Bluetooth est NONE (pas de connection pour le moment)
	            if (Globales.BTService.getState() == BluetoothService.STATE_NONE) {
	              // on lance le service
	            	Globales.BTService.start();
	            }
	        }
		
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connexion_maitre, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_connexion_maitre, container, false);
			return rootView;
		}
	}
	
	/* tmp */
	// The Handler that gets information back from the BluetoothChatService
  
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
            	//String readMessage = new String(readBuff, 0, msg.arg1);
                Toast.makeText(getApplicationContext(), readMessage.get(0).getMessage(),
                        Toast.LENGTH_SHORT).show();
                
                Toast.makeText(getApplicationContext(), readMessage.get(1).getMessage(),
                        Toast.LENGTH_SHORT).show();
                break;
            case Globales.MESSAGE_DEVICE_NAME:
            	// construct a string from the valid bytes in the buffer
            	String mConnectedDeviceName = msg.getData().getString("Device");
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case Globales.MESSAGE_TOAST:
            	// construct a string from the valid bytes in the buffer
                String readMessag = msg.getData().getString(Globales.TOAST);
                Toast.makeText(getApplicationContext(), readMessag,
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	/* tmp */

}
