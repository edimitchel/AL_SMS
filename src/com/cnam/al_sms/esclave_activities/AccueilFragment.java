package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;

import shared.Globales;
import shared.Globales.DeviceType;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.gestionsms.SynchroController;
import com.cnam.al_sms.modeles.SMS;

@SuppressLint("ValidFragment")
public class AccueilFragment extends Fragment {

	private Context context;

	private Button m_BTNSync;

	public AccueilFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		context = rootView.getContext();

		m_BTNSync = (Button) rootView.findViewById(R.id.btn_start_sync);

		m_BTNSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Globales.BTService.getState() == BluetoothService.STATE_CONNECTED) {
					if (Globales.getDeviceType(Globales.curActivity) == DeviceType.phone) {
						ArrayList<SMS> list = (ArrayList<SMS>) SynchroController
								.getSmsSince(context);

						byte[] listbytes;
						for (SMS sms : list) {
							listbytes = SMS.getBytes(sms);

							Globales.BTService.send(listbytes);

						}
					}
				} else {
					Toast.makeText(Globales.curActivity,
							"Connexion nécéssaire", Toast.LENGTH_LONG).show();
				}

			}
		});

		return rootView;
	}

}
