package com.cnam.al_sms.Maitre_Activities;

import com.cnam.al_sms.Globales;
import com.cnam.al_sms.R;
import com.cnam.al_sms.Connectivite.BluetoothService;
import com.cnam.al_sms.R.id;
import com.cnam.al_sms.R.layout;
import com.cnam.al_sms.R.menu;


import android.R.integer;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class ConnexionMaitreActivity extends Activity  {
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    // Member object for the chat services
    private BluetoothService bTService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion_maitre);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		bTService =  new BluetoothService(this, mHandler);
		 if (bTService != null) {
	            // Seulement si le statut de la connection Bluetooth est NONE (pas de connection pour le moment)
	            if (bTService.getState() == BluetoothService.STATE_NONE) {
	              // on lance le service
	            	bTService.start();
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
                
                break;
            case Globales.MESSAGE_DEVICE_NAME:
            	byte[] readB = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
            	String mConnectedDeviceName = msg.getData().getString("Device");
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case Globales.MESSAGE_TOAST:
            	byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessag = new String(readBuf, 0, msg.arg1);
                Toast.makeText(getApplicationContext(), msg.getData().getString(readMessag),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	/* tmp */

	

}
