package com.cnam.al_sms.Esclave_Activities;

import java.util.ArrayList;
import java.util.Set;

import shared.BluetoothDeviceAdapter;
import shared.BluetoothDeviceGroup;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cnam.al_sms.R;

public class ConfigurationConnexionActivity extends Activity {

	protected static final long TEMPS_RAFFRAICHISSEMENT_RECHERCHE = 5000;

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;

	private BluetoothDeviceGroup mArrayPairedDevice = new BluetoothDeviceGroup(
			"Périphériques appareillés");

	private BluetoothDeviceGroup mArrayBluetoothDevice = new BluetoothDeviceGroup(
			"Appareils connectés");

	private ArrayList<BluetoothDeviceGroup> listeGroupes;

	private BluetoothAdapter ba;

	// Permet le rafraichissement de la découverte des périphériques
	private Handler handler = new Handler();
	private Runnable raffraichitPeripheriques = new Runnable() {
		public void run() {
			setRechercheMode(true);
			ba.startDiscovery();
			handler.postDelayed(raffraichitPeripheriques,
					TEMPS_RAFFRAICHISSEMENT_RECHERCHE);
		}
	};

	private ExpandableListView mExpendableList;

	private ProgressBar mRoueRecherche;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			mArrayBluetoothDevice.getDevices().clear();

			Log.i("ALSMS", "Recherche de périphériques..");
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				if (device.getName() != "null" && !device.getName().isEmpty())
					mArrayBluetoothDevice.add(device);
			}

			listeGroupes = new ArrayList<BluetoothDeviceGroup>();

			if (mArrayPairedDevice.size() > 0)
				listeGroupes.add(mArrayPairedDevice);
			if (mArrayBluetoothDevice.size() > 0)
				listeGroupes.add(mArrayBluetoothDevice);

			BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(
					ConfigurationConnexionActivity.this, listeGroupes);
			mExpendableList.setAdapter(adapter);
			int nb_group = mExpendableList.getCount();
			for (int i = 0; i < nb_group; i++) {
				mExpendableList.expandGroup(i);
			}

			int nb_devices = mArrayBluetoothDevice.size()
					+ mArrayPairedDevice.size();

			setRechercheMode(false);

			if (nb_devices != 0) {
				String s = nb_devices > 1 ? "s" : "";
				showMessage(nb_devices + " périphérique" + s + " trouvé" + s, 1);
			} else {
				showMessage(
						"Aucun périphérique n'a été trouvé.\nVérifiez votre configuration.",
						0);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronisation);

		mExpendableList = (ExpandableListView) findViewById(R.id.connectedDevices);

		mRoueRecherche = (ProgressBar) findViewById(R.id.roueRecherche);
		ba = BluetoothAdapter.getDefaultAdapter();
		if (ba != null) {
			if (!ba.isEnabled()) {
				Intent iEnableBlue = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(iEnableBlue, REQUEST_ENABLE_BT);
				ba.startDiscovery();
			}

			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter);

			Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
			if (pairedDevices.size() > 0) {
				// Loop through paired devices
				for (BluetoothDevice device : pairedDevices) {
					// Add the name and address to an array adapter to show in a
					// ListView
					mArrayPairedDevice.add(device);
				}
			}

			raffraichitPeripheriques.run();
		}
	}

	protected void setRechercheMode(boolean b) {
		mRoueRecherche.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
	}

	private void showMessage(String s, int type) {
		TextView tvMessage = (TextView) findViewById(R.id.tv_msg_sync);

		tvMessage.setText(s);
		if (type == 0) { // ERROR
			tvMessage.setTextColor(Color.parseColor("red"));
		} else if (type == 1) { // WELL DONE
			tvMessage.setTextColor(Color.parseColor("#3e8940"));
		} else if (type == 2) { // WARNING
			tvMessage.setTextColor(Color.parseColor("orange"));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
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
		} else if (id == R.id.raffraichirRercherche) {
			setRechercheMode(true);
			ba.startDiscovery();
		}
		return super.onOptionsItemSelected(item);
	}

	public void envoyerAdressePeripherique(BluetoothDevice bd) {
		Intent DeviceToConnect = new Intent(
				ConfigurationConnexionActivity.this,
				ConnexionEsclaveActivity.class);
		DeviceToConnect.putExtra("Adresse_MAC", bd.getAddress());
		startActivity(DeviceToConnect);

	}

}
