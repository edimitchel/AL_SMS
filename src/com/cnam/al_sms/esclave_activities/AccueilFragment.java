package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.Date;

import shared.Globales;
import shared.Globales.DeviceType;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.gestionsms.SynchroController;
import com.cnam.al_sms.modeles.SMS;

@SuppressLint("ValidFragment")
public class AccueilFragment extends Fragment {

	private Button m_BTNSync;

	public AccueilFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		m_BTNSync = (Button) rootView.findViewById(R.id.btn_start_sync);

		m_BTNSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Globales.BTService.getState() == BluetoothService.STATE_CONNECTED) {
					if (Globales.getDeviceType(getActivity()
							.getApplicationContext()) == DeviceType.phone) {
						SynchroController.synchroPeriode(getActivity()
								.getApplicationContext());
					} else if (Globales.getDeviceType(getActivity()
							.getApplicationContext()) == DeviceType.tablette) {
						byte[] listbytes;
						listbytes = SMS.getBytes(new SMS(1, 1, "0687974971", 0,
								new Date(0), new Date(0), 1, 1, 1, "", "test",
								1));

						Globales.BTService.send(listbytes);
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Connexion nécéssaire. Veuillez vous connecter.",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		return rootView;
	}

}
