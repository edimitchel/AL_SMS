package com.cnam.al_sms.Esclave_Activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import shared.BluetoothDeviceAdapter;
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
import android.widget.ListView;
import android.widget.TextView;

import com.cnam.al_sms.R;

public class ConfigurationConnexionActivity extends Activity implements
		AdapterView.OnItemSelectedListener {

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;

	private List<BluetoothDevice> mArrayPairedDevice = new ArrayList<BluetoothDevice>();

	private List<BluetoothDevice> mArrayBluetoothDevice = new ArrayList<BluetoothDevice>();

	private ListView mPairedDevices;
	
	private ListView mOtherDevices;

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
				if (device.getName() != "null")
					mArrayPairedDevice.add(device);
			}

			BluetoothDeviceAdapter adapterPairedDevices = new BluetoothDeviceAdapter(
					context, mArrayPairedDevice);
			mPairedDevices.setAdapter(adapterPairedDevices);

			BluetoothDeviceAdapter adapterOtherDevice = new BluetoothDeviceAdapter(
					context, mArrayBluetoothDevice);
			mOtherDevices.setAdapter(adapterOtherDevice);

			int nb_devices = mArrayPairedDevice.size()
					+ mArrayPairedDevice.size();

			if (mArrayPairedDevice.size() + mArrayPairedDevice.size() != 0) {
				String s = nb_devices > 1 ? "s" : "";
				showMessage(nb_devices + " p�riph�rique" + s + " trouv�" + s, 1);
			} else {
				showMessage(
						"Aucun p�riph�rique n'a �t� trouv�. V�rifiez votre configuration.",
						0);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronisation);
		mPairedDevices = (ListView) findViewById(R.id.lstPairedDevice);
		mOtherDevices = (ListView) findViewById(R.id.lstOtherDevice);
		ba = BluetoothAdapter.getDefaultAdapter();
		if (ba != null) {
			if (!ba.isEnabled()) {
				Intent iEnableBlue = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(iEnableBlue, REQUEST_ENABLE_BT);
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
					showMessage("En fait si �a marche du tonner!", 1);
					return true;
				}
			});

			ObjectAnimator anim = ObjectAnimator.ofFloat(tvMessage, "alpha",
					1f, 0.8f);
			anim.setDuration(500).setRepeatMode(ObjectAnimator.INFINITE);
			anim.start();

			mPairedDevices.setOnItemSelectedListener(this);
			mOtherDevices.setOnItemSelectedListener(this);
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
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		BluetoothDevice bdToConnect = (BluetoothDevice) parent.getItemAtPosition(position);
		Intent DeviceToConnect = new Intent(ConfigurationConnexionActivity.this,MainActivity.class);
		DeviceToConnect.putExtra("Adresse_MAC", bdToConnect.getAddress());
		startActivityForResult(DeviceToConnect, 1);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}