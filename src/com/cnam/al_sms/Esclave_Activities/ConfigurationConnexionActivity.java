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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnam.al_sms.R;

public class ConfigurationConnexionActivity extends Activity implements
		AdapterView.OnItemClickListener {

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;

	private BluetoothDeviceGroup mArrayPairedDevice = new BluetoothDeviceGroup(
			"Périphériques appareillés");

	private BluetoothDeviceGroup mArrayBluetoothDevice = new BluetoothDeviceGroup(
			"Appareils connectés");

	private ArrayList<BluetoothDeviceGroup> listeGroupes;

	private ListView mPairedDevices;

	private ListView mOtherDevices;

	private ExpandableListView mExpendableList;

	private BluetoothAdapter ba;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			Log.i("smsview", "Receiver");
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				if (device.getName() != "null" && !device.getName().isEmpty())
					mArrayPairedDevice.add(device);
			}

			listeGroupes = new ArrayList<BluetoothDeviceGroup>();

			if (mArrayPairedDevice.size() > 0)
				listeGroupes.add(mArrayPairedDevice);
			if (mArrayBluetoothDevice.size() > 0)
				listeGroupes.add(mArrayBluetoothDevice);

			BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(
					ConfigurationConnexionActivity.this, listeGroupes);
			mExpendableList.setAdapter(adapter);
			mExpendableList.expandGroup(0);

			int nb_devices = mArrayBluetoothDevice.size()+mArrayPairedDevice.size();

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
					mArrayBluetoothDevice.add(device);
				}
			}
			ba.startDiscovery();

			TextView tvMessage = (TextView) findViewById(R.id.tv_msg_sync);
			tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					showMessage("En fait si ça marche du tonner!", 1);
					return true;
				}
			});

			ObjectAnimator anim = ObjectAnimator.ofFloat(tvMessage, "alpha",
					1f, 0.8f);
			anim.setDuration(500).setRepeatMode(ObjectAnimator.INFINITE);
			anim.start();
		}
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
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BluetoothDevice bdToConnect = (BluetoothDevice) parent
				.getItemAtPosition(position);
		Intent DeviceToConnect = new Intent(
				ConfigurationConnexionActivity.this, ConnexionEsclaveActivity.class);
		DeviceToConnect.putExtra("Adresse_MAC", bdToConnect.getAddress());
		startActivity(DeviceToConnect);
		
	}

}
