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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cnam.al_sms.BuildConfig;
import com.cnam.al_sms.R;
import com.cnam.al_sms.Connectivite.ConnecBluetooth;

public class ConfigurationConnexionActivity extends Activity implements Runnable {

	protected static final long TEMPS_RAFFRAICHISSEMENT_RECHERCHE = 5000;

	protected static final long DUREE_RECHERCHE = 5000;

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;
	
	private Activity activity = this;

	private BluetoothDeviceGroup mPairedDeviceGroup = new BluetoothDeviceGroup(
			"Périphériques appareillés");

	private BluetoothDeviceGroup mBluetoothDeviceGroup = new BluetoothDeviceGroup(
			"Appareils connectés");

	private ArrayList<BluetoothDeviceGroup> listeGroupes;

	private BluetoothAdapter ba;

	private boolean enRecherche = true;

	private Calendar calendrier = Calendar.getInstance();

	private Timestamp firstOrForcedRefresh = null;

	// Permet le rafraichissement de la découverte des périphériques
	private Handler handler = new Handler();
	private Runnable raffraichitPeripheriques = this;

	private ExpandableListView mExpendableList;

	private ProgressBar mRoueRecherche;

	private Menu optionsMenu;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			Log.i(BuildConfig.TAG, "Périphérique trouvé..");
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
			raffraichitPeripheriques.run();

			Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
			if (pairedDevices.size() > 0) {
				// Loop through paired devices
				for (BluetoothDevice device : pairedDevices) {
					// Add the name and address to an array adapter to show in a
					// ListView
					mPairedDeviceGroup.add(device);
				}
			}
		}
	}

	protected void setRechercheMode(boolean b) {
		setRefreshActionButtonState(b);
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
		this.optionsMenu = menu;
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

	public void setRefreshActionButtonState(final boolean refreshing) {
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
	
	public void run(){
		if (firstOrForcedRefresh == null)
			firstOrForcedRefresh = new Timestamp(
					calendrier.get(Calendar.SECOND));

		if (!enRecherche
				|| calendrier.get(Calendar.SECOND)
						- firstOrForcedRefresh.getTime() > DUREE_RECHERCHE) {
			enRecherche = false;
			return;
		}

		if (!ba.isDiscovering() && ba.isEnabled()) {
			ba.startDiscovery();
			setRechercheMode(true);
		} else {
			try {
				ConnecBluetooth.checkBluetoothConnection(activity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i(BuildConfig.TAG,e.getLocalizedMessage());
			}
			setRechercheMode(false);
			return;
		}

		listeGroupes = new ArrayList<BluetoothDeviceGroup>();

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

		BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(
				ConfigurationConnexionActivity.this, listeGroupes);
		mExpendableList.setAdapter(adapter);
		int nb_group = mExpendableList.getCount();
		for (int i = 0; i < nb_group; i++) {
			mExpendableList.expandGroup(i);
		}

		int nb_devices = mBluetoothDeviceGroup.size();

		if (nb_devices != 0) {
			String s = nb_devices > 1 ? "s" : "";
			showMessage(nb_devices + " périphérique" + s + " trouvé" + s, 1);
		} else {
			showMessage("Aucun périphérique n'a été trouvé.", 0);
		}

		handler.postDelayed(raffraichitPeripheriques,
				TEMPS_RAFFRAICHISSEMENT_RECHERCHE);
	}

}
