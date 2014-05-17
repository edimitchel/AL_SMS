package com.cnam.al_sms.maitre_activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cnam.al_sms.R;
import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.data.datasource.SyncDataSource;
import com.cnam.al_sms.gestionsms.SynchroController;

public class SynchronisationActivity extends Activity {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchroniser);

		SynchroController.getAllSmsFromMasterBase(SynchronisationActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Only show items in the action bar relevant to this screen
		// if the drawer is not showing. Otherwise, let the drawer
		// decide what to show in the action bar.
		getMenuInflater().inflate(R.menu.synchronisation, menu);

		return super.onCreateOptionsMenu(menu);
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
}
