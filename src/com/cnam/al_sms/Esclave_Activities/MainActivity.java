package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shared.Globales;
import shared.NavDrawerListAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.gestionsms.ContactController;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.gestionsms.SynchroController;
import com.cnam.al_sms.maitre_activities.ConnexionMaitreActivity;
import com.cnam.al_sms.modeles.NavDrawerItem;

public class MainActivity extends AlsmsActivity {
	private static final String TAG = "ALSMS";

	private static final int CODE_APP = 98651;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	/* fin variable liaison Bluetooth */
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	private static ArrayList<HashMap<String, String>> conversations = new ArrayList<HashMap<String, String>>();

	/*
	 * Navigation
	 */

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private static ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Globales.getDeviceType(this);
		Globales.BTService = new BluetoothService(Globales.mHandler);
		Toast.makeText(this, "Je suis " + Globales.typeAppareil,
				Toast.LENGTH_LONG).show();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		if (savedInstanceState == null) {
			displayView(0);
		}

		if (navDrawerItems.size() == 0) {
			navDrawerItems.add(new NavDrawerItem("Accueil", navMenuIcons
					.getResourceId(0, -1)));
		}

		HashMap<String, String> mapConversation;

		Cursor cFil = MessagerieController.getConversations(this);

		while (cFil.moveToNext() && conversations.size() != cFil.getCount()) {
			mapConversation = new HashMap<String, String>();

			long thread_id = cFil.getLong(cFil
					.getColumnIndex(DataBaseHelper.COLUMN_ID));
			String lst_msg = cFil.getString(cFil
					.getColumnIndex(DataBaseHelper.COLUMN_SNIPPET));
			int cMsg = cFil.getInt(cFil
					.getColumnIndex(DataBaseHelper.COLUMN_MESSAGECOUNT));

			mapConversation.put("thread_id", thread_id + "");
			mapConversation.put("nom_contact",
					ContactController.getContactByThread(thread_id, this));
			mapConversation.put("nb_msg", Integer.toString(cMsg));
			mapConversation.put("last_msg", lst_msg);

			navDrawerItems.add(new NavDrawerItem(mapConversation
					.get("nom_contact"), ContactController
					.getContactImageUriByThread(
							Long.valueOf(mapConversation.get("thread_id")),
							this.getApplicationContext()), true,
					mapConversation.get("nb_msg")));
			conversations.add(mapConversation);
		}
		cFil.close();

		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
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
			Intent intent = new Intent(getBaseContext(),
					ConfigurationConnexionActivity.class);
			startActivityForResult(intent, CODE_APP);
			return true;
		} else if (id == R.id.synchroniser) {
			if (Globales.typeAppareil == Globales.DeviceType.phone) {
				SynchroController.getAllSmsFromMasterBase(this);
			} else if (Globales.typeAppareil == Globales.DeviceType.tablette) {
				SynchroController.synchroPeriode(this.getApplicationContext());
			}
		} else if (id == R.id.connexion) {
			Intent i_connec = null;
			if (Globales.typeAppareil == Globales.DeviceType.phone) {
				i_connec = new Intent(MainActivity.this,
						ConnexionMaitreActivity.class);

			} else if (Globales.typeAppareil == Globales.DeviceType.tablette) {
				i_connec = new Intent(MainActivity.this,
						ConfigurationConnexionActivity.class);
			}
			startActivityForResult(i_connec, CODE_APP);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnected() {
		// Do Nothing
	}

	@Override
	public void onFailedConnection() {
		// Do Nothing

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		Map map = null;

		CharSequence title = mTitle;

		// update the main content by replacing fragments
		Fragment fragment = null;

		if (position == 0) {
			// ACCUEIL
			title = "Accueil";
			fragment = new AccueilFragment();
		} else {
			map = conversations.get(position);
			title = map.get("nom_contact").toString();
			fragment = new ConversationFragment(Long.valueOf(map.get(
					"thread_id").toString()));
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(title);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
