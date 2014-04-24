package com.cnam.al_sms.Esclave_Activities;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.Connectivite.BluetoothService;
import com.cnam.al_sms.Connectivite.ConnecBluetooth;

public class ConnexionEsclaveActivity extends Activity {
	private BluetoothService bTService = new BluetoothService(this,
			new Handler());
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion_esclave);

		// On récupère les données du Bundle
		Bundle obunble = this.getIntent().getExtras();
		if (obunble != null && obunble.containsKey("Adresse_MAC")) {
			String adress = this.getIntent().getStringExtra("Adresse_MAC");
			TextView txt_mac = (TextView) findViewById(R.id.txt_mac);
			txt_mac.setText("Vous souhaitez vous connectez à l'adresse MAC :"
					+ adress);
			// création d'un Bluetooth device grace à l'adresse puis connection grace au Bluetooth Service
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(adress);
			bTService.connect(device);
			Toast.makeText(this, "En attente de connexion", Toast.LENGTH_LONG).show();
			ConnecBluetooth.askAcceptBluetooth(device,this);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connexion_esclave, menu);
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
					R.layout.fragment_connexion_esclave, container, false);
			return rootView;
		}
	}

	/* Liaison Bluetooth */
	/*
	 * private void sendMessage(String message) { // Check that we're actually
	 * connected before trying anything if (bTService.getState() !=
	 * BluetoothService.STATE_CONNECTED) { Toast.makeText(this, "Pas connecté",
	 * Toast.LENGTH_SHORT).show(); return; }
	 * 
	 * // Check that there's actually something to send if (message.length() >
	 * 0) { // Get the message bytes and tell the BluetoothChatService to write
	 * byte[] send = message.getBytes(); bTService.write(send);
	 * 
	 * // Reset out string buffer to zero and clear the edit text field
	 * mOutStringBuffer.setLength(0);
	 * 
	 * } }
	 * 
	 * public void onActivityResult(int requestCode, int resultCode, Intent
	 * data) { if(D) Log.d(TAG, "onActivityResult " + resultCode); switch
	 * (requestCode) { case REQUEST_CONNECT_DEVICE: // When DeviceListActivity
	 * returns with a device to connect if (resultCode == Activity.RESULT_OK) {
	 * // Get the device MAC address String address =
	 * data.getExtras().getString("Adresse_MAC"); Log.i(TAG,
	 * "adress: "+address); // Get the BLuetoothDevice object BluetoothDevice
	 * device = mBluetoothAdapter.getRemoteDevice(address); // Attempt to
	 * connect to the device bTService.connect(device); } break; case
	 * REQUEST_ENABLE_BT: // When the request to enable Bluetooth returns if
	 * (resultCode == Activity.RESULT_OK) { // Bluetooth is now enabled, so set
	 * up a chat session
	 * 
	 * } else { // User did not enable Bluetooth or an error occured Log.d(TAG,
	 * "BT not enabled"); Toast.makeText(this, "Bluetooth not enabled",
	 * Toast.LENGTH_SHORT).show(); finish(); } } }
	 */

}
