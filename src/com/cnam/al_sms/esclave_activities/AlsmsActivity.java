package com.cnam.al_sms.esclave_activities;

import shared.Globales;
import android.app.Activity;
import android.os.Bundle;

public abstract class AlsmsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		saveIntoGlobales();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		saveIntoGlobales();
	}
	
	private void saveIntoGlobales(){
		Globales.curActivity = this;
	}
	
	public abstract void onConnected();
	
	public abstract void onFailedConnection();
	
}
