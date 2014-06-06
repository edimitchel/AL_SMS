package com.cnam.al_sms.maitre_activities;

import shared.Globales;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.connectivite.ConnecBluetooth;
import com.cnam.al_sms.connectivite.ConnectiviteFactory;
import com.cnam.al_sms.esclave_activities.AlsmsActivity;

public class ConnexionMaitreActivity extends AlsmsActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion_maitre);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		try {
			ConnecBluetooth.checkBluetoothVisibility(this);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			finishActivity(-1);
		}

		if (Globales.BTService != null) {
			// Seulement si le statut de la connection Bluetooth est NONE (pas
			// de connection pour le moment)
			if (Globales.BTService.getState() == BluetoothService.STATE_NONE) {
				// on lance le service
				Globales.BTService.start();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connexion_maitre, menu);
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
					R.layout.fragment_connexion_maitre, container, false);
			return rootView;
		}
	}

	@Override
	public void onConnected() {
		finish();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Globales.BTService.getState() == ConnectiviteFactory.STATE_LISTEN) {
			Globales.BTService.stop();
		}
	}

	@Override
	public void onFailedConnection() {
		// Do Nothing

	}

}
