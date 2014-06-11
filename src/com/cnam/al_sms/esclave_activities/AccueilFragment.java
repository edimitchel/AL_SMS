package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;

import shared.Globales;
import shared.Globales.DeviceType;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.gestionsms.ConversationController;
import com.cnam.al_sms.gestionsms.SynchroController;
import com.cnam.al_sms.modeles.SMS;

@SuppressLint("ValidFragment")
public class AccueilFragment extends Fragment {

	private Context context;

	private Button mBTNSync;

	private Button mBTNInit;

	public AccueilFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		context = rootView.getContext();

		mBTNSync = (Button) rootView.findViewById(R.id.btn_start_sync);

		mBTNSync.setOnClickListener(new View.OnClickListener() {
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

		mBTNInit = (Button) rootView.findViewById(R.id.btn_init);

		mBTNInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Ré-initialisation")
						.setMessage(
								"Vous allez ré-initialiser toutes vos données.\nÊtes-vous sûr(e) ?")
						.setPositiveButton("Oui, je le veux.",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ConversationController
												.clearAllData(context);
										getActivity().recreate();
									}

								}).setNegativeButton("Non, merci.", null)
						.show();
			}
		});

		return rootView;
	}

}
