package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.Date;

import shared.Globales;

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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.connectivite.ConnecBluetooth;
import com.cnam.al_sms.modeles.SMS;

public class ConnexionEsclaveActivity extends AlsmsActivity implements OnLongClickListener{
	private BluetoothService bTService = new BluetoothService(Globales.mHandler);
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion_esclave);

		// On récupère les données du Bundle
		Bundle obunble = this.getIntent().getExtras();
		if (obunble != null && obunble.containsKey("Adresse_MAC") ) {
			String adress = this.getIntent().getStringExtra("Adresse_MAC");
			/*String name = this.getIntent().getStringExtra("Device_Name");
			
			TextView txt_mac = (TextView) findViewById(R.id.txtv_attente);
			txt_mac.setText("@string/attenteCo à " + name + " ["
					+ adress + "]");*/
			// création d'un Bluetooth device grace à l'adresse puis connection grace au Bluetooth Service
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(adress);
			bTService.connect(device);
			Toast.makeText(this, "En attente de connexion", Toast.LENGTH_LONG).show();
			//ConnecBluetooth.askAcceptBluetooth(device,this);
			//txt_mac.setOnLongClickListener(this);
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
	public boolean onLongClick(View v) {
		
		ArrayList<SMS> aList =new ArrayList<SMS>();
		aList.add(new SMS(123,123,"adress",1,new Date(),new Date(),1,2,3,"Salut","Salut les amis",2));
		aList.add(new SMS(43,43,"adrhhess",1,new Date(),new Date(),1,2,3,"Salut","Comment sa va ?",2));

		byte[] data = SMS.getBytesFromList(aList);
		
        bTService.send(data);
		Toast.makeText(this, "Transfet d'un message", Toast.LENGTH_LONG).show();

        		return false;
	}

}
