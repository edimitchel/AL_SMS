package com.cnam.al_sms;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.LauncherActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SynchronisationActivity extends Activity implements
		AdapterView.OnItemSelectedListener {

	private int CODE_ACTIVITY = 2;

	private int REQUEST_ENABLE_BT = 1;

	private ArrayList<BluetoothDevice> mArrayBDevice = new ArrayList<BluetoothDevice>();

	private ArrayList<String> mArrayAdapter = new ArrayList<String>();

	private ListView lv_devices;

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
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
				mArrayBDevice.add(device);

				Log.i("smsview", mArrayAdapter.toString());
			}

			Set set = new HashSet();
			set.addAll(mArrayAdapter);
			mArrayAdapter = new ArrayList(set);

			set = new HashSet();
			set.addAll(mArrayBDevice);
			mArrayBDevice = new ArrayList(set);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					SynchronisationActivity.this,
					android.R.layout.simple_list_item_checked, mArrayAdapter);
			lv_devices.setAdapter(adapter);

			if (!mArrayAdapter.isEmpty()) {
				String s = mArrayAdapter.size() > 1 ? "s" : "";
				showMessage(mArrayAdapter.size() + " périphérique" + s
						+ " trouvé" + s, 1);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronisation);

		lv_devices = (ListView) findViewById(R.id.lstDevice);

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
					mArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
			}
			ba.startDiscovery();

			if (mArrayAdapter.isEmpty()) {
				showMessage(
						"Aucun périphérique n'a été trouvé. Vérifiez votre configuration.",
						0);
			}

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

			lv_devices.setOnItemSelectedListener(this);

			Log.i("smsview", mArrayAdapter.toString());
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
		ListView item = (ListView) parent.getItemAtPosition(position);
		if (!item.isSelected())
			item.setSelected(true);
		else
			item.setSelected(false);

		BluetoothDevice bdToConnect = mArrayBDevice.get(position);

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
