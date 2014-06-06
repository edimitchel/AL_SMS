package com.cnam.al_sms.esclave_activities;

import shared.Globales;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnam.al_sms.R;

public class ConnexionEsclaveActivity extends AlsmsActivity {

	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion_esclave);

		// On récupère les données du Bundle
		Bundle obunble = this.getIntent().getExtras();
		if (obunble != null && obunble.containsKey("Adresse_MAC")) {
			String adress = this.getIntent().getStringExtra("Adresse_MAC");
			String name = this.getIntent().getStringExtra("Device_Name");

			TextView txt_mac = (TextView) findViewById(R.id.txt_attenteCo);
			txt_mac.setText(getString(R.string.AttenteCo) + " à " + name + " ["
					+ adress + "]");
			// création d'un Bluetooth device grace à l'adresse puis connection
			// grace au Bluetooth Service
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(adress);
			Globales.BTService.connect(device);
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

	@Override
	public void onConnected() {
		this.setResult(RESULT_OK);
		this.finish();

	}

	@Override
	public void onFailedConnection() {
		finish();

	}

}
