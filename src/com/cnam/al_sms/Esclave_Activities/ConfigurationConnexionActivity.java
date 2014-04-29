package com.cnam.al_sms.Esclave_Activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import shared.BluetoothDeviceAdapter;
import shared.BluetoothDeviceGroup;
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
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.Connectivite.ConnecBluetooth;

public class ConfigurationConnexionActivity extends Activity implements
		Runnable {
	private static final String TAG = "ALSMS";

	protected static final long TEMPS_RAFFRAICHISSEMENT_RECHERCHE = 5000;

	protected static final long DUREE_RECHERCHE = 5000;

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;

	private Activity activity = this;

	private BluetoothDeviceGroup mPairedDeviceGroup = new BluetoothDeviceGroup(
			"Périphériques appareillés");

	private BluetoothDeviceGroup mBluetoothDeviceGroup = new BluetoothDeviceGroup(
			"Appareils visibles");

	private ArrayList<BluetoothDeviceGroup> listeGroupes;

	private BluetoothAdapter ba;

	private int nb_recherche = 0;

	BluetoothDeviceAdapter adapter = null;

	// Permet le rafraichissement de la découverte des périphériques
	private Handler handler = new Handler();

	private ExpandableListView mExpendableList;

	private Menu optionsMenu;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			Log.i(TAG, "Périphérique trouvé..");
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				if (!mBluetoothDeviceGroup.getDevices().contains(device)
						&& device.getName() != "null"
						&& !device.getName().isEmpty())
					mBluetoothDeviceGroup.add(device);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronisation);

		mExpendableList = (ExpandableListView) findViewById(R.id.connectedDevices);

		ba = BluetoothAdapter.getDefaultAdapter();
		if (ba != null) {
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter);
			ba.startDiscovery();
			
			Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					mPairedDeviceGroup.add(device);
				}
			}
		}

		if (ba.isEnabled()) {
			run();
		} else {
			boolean is_enabled = false;
			try {
				is_enabled = ConnecBluetooth.checkBluetoothConnection(activity);
			} catch (Exception e) {
				Log.i(TAG, e.getLocalizedMessage());
			}

			if (is_enabled) {
				run();
			} else {
				showMessage("Activez votre bluetooth, s'il vous plait.", 0);
			}
		}
	}

	protected void setRechercheMode(boolean b) {
		setRefreshActionButtonState(b);
	}

	private void showMessage(String s, int type) {
		TextView tvMessage = (TextView) findViewById(R.id.tv_msg_sync);

		tvMessage.setText(s);
		if (type == 0) {
			tvMessage.setTextColor(Color.parseColor("red"));
		} else if (type == 1) {
			tvMessage.setTextColor(Color.parseColor("#3e8940"));
		} else if (type == 2) {
			tvMessage.setTextColor(Color.parseColor("orange"));
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		getMenuInflater().inflate(R.menu.connexion_esclave, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.raffraichirRercherche) {
			run();
		}
		return super.onOptionsItemSelected(item);
	}

	public void setRefreshActionButtonState(boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.raffraichirRercherche);
			if (refreshItem != null) {
				if (refreshing) {
					refreshItem.setActionView(R.layout.menu_action_is_refresh);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	public void envoyerAdressePeripherique(BluetoothDevice bd) {
		Intent DeviceToConnect = new Intent(
				ConfigurationConnexionActivity.this,
				ConnexionEsclaveActivity.class);
		DeviceToConnect.putExtra("Adresse_MAC", bd.getAddress());
		startActivity(DeviceToConnect);
	}

	public void run() {
		if (ba.isDiscovering())
			ba.cancelDiscovery();

		ba.startDiscovery();
		setRechercheMode(true);
		
		showMessage("Recherche en cours ...", 1);

		listeGroupes = new ArrayList<BluetoothDeviceGroup>();

		// Suppression du device appareillé de la liste des appareils visibles.
		for (BluetoothDevice bd : mPairedDeviceGroup.getDevices()) {
			if (!mBluetoothDeviceGroup.getDevices().contains(bd)) {
				mPairedDeviceGroup.getDevices().remove(
						mPairedDeviceGroup.getDevices().indexOf(bd));
			}
		}

		if (mBluetoothDeviceGroup.size() > 0)
			listeGroupes.add(mBluetoothDeviceGroup);
		if (mPairedDeviceGroup.size() > 0)

			listeGroupes.add(mPairedDeviceGroup);
		adapter = new BluetoothDeviceAdapter(
				ConfigurationConnexionActivity.this, listeGroupes);
		mExpendableList.setAdapter(adapter);

		for (int i = 0; i < mExpendableList.getChildCount(); i++) {
			mExpendableList.expandGroup(i);
		}

		int nb_devices = mBluetoothDeviceGroup.size();
		if (nb_devices != 0) {
			String s = nb_devices > 1 ? "s" : "";
			showMessage(nb_devices + " périphérique" + s + " trouvé" + s, 1);
		} else {
			showMessage("Aucun périphérique n'a été trouvé.", 0);
		}
		
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (ba.isDiscovering())
					handler.postDelayed(this, TEMPS_RAFFRAICHISSEMENT_RECHERCHE);
				else {
					nb_recherche++;
					setRechercheMode(false);
				}
			}
		}, TEMPS_RAFFRAICHISSEMENT_RECHERCHE);
	}

}
