package com.cnam.al_sms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ConfigurationConnexionView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration_connexion_view);

		Log.d("ALSMS", savedInstanceState.toString());
	}

}
