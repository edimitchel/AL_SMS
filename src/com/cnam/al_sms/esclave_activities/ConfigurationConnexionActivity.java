package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.Set;

import shared.BluetoothDeviceAdapter;
import shared.BluetoothDeviceGroup;
import shared.Globales;
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
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.ConnecBluetooth;
import com.cnam.al_sms.connectivite.ConnectiviteFactory;

public class ConfigurationConnexionActivity extends AlsmsActivity implements
		Runnable {
	private static final String TAG = "ALSMS";

	protected static final long TEMPS_RAFFRAICHISSEMENT_RECHERCHE = 5000;

	protected static final long DUREE_RECHERCHE = 5000;

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;

	private Activity activity = this;

	private BluetoothDeviceGroup mPairedDeviceGroup = new BluetoothDeviceGroup(
			"P�riph�riques appareill�s");

	private BluetoothDeviceGroup mBluetoothDeviceGroup = new BluetoothDeviceGroup(
			"Appareils visibles");

	private ArrayList<BluetoothDeviceGroup> listeGroupes;

	private BluetoothAdapter ba;

	private int nb_recherche = 0;

	BluetoothDeviceAdapter adapter = null;

	// Permet le rafraichissement de la d�couverte des p�riph�riques
	private Handler handler = new Handler();

	private ExpandableListView mExpendableList;

	private Menu optionsMenu;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			Log.i(TAG, "P�riph�rique trouv�..");
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				if (!mBluetoothDeviceGroup.contains(device)
						&& device.getName() != "null"
						&& !device.getName().isEmpty())
					mBluetoothDeviceGroup.add(device);
				if (adapter != null)
					adapter.notifyDataSetChanged();
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
			setRechercheMode(true);

			Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					mPairedDeviceGroup.add(device);
				}
			}
		}

		if (ba.isEnabled()) {
			handler.postDelayed(this, TEMPS_RAFFRAICHISSEMENT_RECHERCHE);
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
		if (b)
			showMessage("Recherche en cours ...", 1);
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

	public void run() {
		if (ba.isDiscovering())
			ba.cancelDiscovery();

		ba.startDiscovery();
		setRechercheMode(true);

		listeGroupes = new ArrayList<BluetoothDeviceGroup>();

		// Suppression du device appareill� de la liste des appareils visibles.
		for (BluetoothDevice bd : mPairedDeviceGroup.getDevices()) {
			if (!mBluetoothDeviceGroup.contains(bd)) {
				mPairedDeviceGroup.getDevices().remove(
						mPairedDeviceGroup.getDevices().indexOf(bd));
			}
		}

		if (mBluetoothDeviceGroup.size() > 0)
			listeGroupes.add(mBluetoothDeviceGroup);
		if (mPairedDeviceGroup.size() > 0)
			listeGroupes.add(mPairedDeviceGroup);

		if (null == adapter) {
			Log.d(TAG, "Instanciation de adapter");
			adapter = new BluetoothDeviceAdapter(this, listeGroupes);
			mExpendableList.setAdapter(adapter);

		}

		for (int i = 0; i < adapter.getGroupCount(); i++) {
			mExpendableList.expandGroup(i);
		}

		int nb_devices = mBluetoothDeviceGroup.size();
		if (nb_devices != 0) {
			String s = nb_devices > 1 ? "s" : "";
			showMessage(nb_devices + " p�riph�rique" + s + " trouv�" + s, 1);
		} else {
			showMessage("Aucun p�riph�rique n'a �t� trouv�.", 0);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Globales.BTService.getState() == ConnectiviteFactory.STATE_CONNECTED) {
			this.finish();
		}

	}

	@Override
	public void onConnected() {
		// Do Nothing

	}

	@Override
	public void onFailedConnection() {
		// Do Nothing

	}

}
