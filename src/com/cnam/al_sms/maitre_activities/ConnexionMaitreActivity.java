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
		
		boolean beginSniffing = false;
		
		try {
			ConnecBluetooth.checkBluetoothConnection(this);
			ConnecBluetooth.checkBluetoothVisibility(this);
			beginSniffing = true;
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			finishActivity(-1);
		} finally {
			while(!beginSniffing){}
			
			if (Globales.BTService != null) {
				// Seulement si le statut de la connection Bluetooth est NONE
				// (pas
				// de connection pour le moment)
				if (Globales.BTService.getState() == BluetoothService.STATE_NONE) {
					// on lance le service
					Globales.BTService.start();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
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
