package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import shared.Globales;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.data.datasource.SyncDataSource;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.maitre_activities.ConnexionMaitreActivity;
import com.cnam.al_sms.maitre_activities.SynchronisationActivity;

public class MainActivity extends Activity {
	private static final String TAG = "ALSMS";

	private static final int CODE_APP = 98651;
	private ListView m_LVconvstream;
	private Button m_BTNSync;
	private BluetoothService bTService = new BluetoothService(this,
			new Handler());
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	/* fin variable liaison Bluetooth */
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	private ArrayList<HashMap<String, String>> conversations = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Globales.getDeviceType(this);

		Toast.makeText(this, "Je suis " + Globales.typeAppareil,
				Toast.LENGTH_LONG).show();

		HashMap<String, String> mapConversation;

		Cursor cFil = MessagerieController.getConversations(this);

		Log.i(TAG, String.valueOf(cFil.getCount()));
		while (cFil.moveToNext()) {
			mapConversation = new HashMap<String, String>();

			long thread_id = cFil.getLong(cFil
					.getColumnIndex(DataBaseHelper.COLUMN_ID));
			String lst_msg = cFil.getString(cFil
					.getColumnIndex(DataBaseHelper.COLUMN_SNIPPET));
			int cMsg = cFil.getInt(cFil
					.getColumnIndex(DataBaseHelper.COLUMN_MESSAGECOUNT));

			// mapConversation.put("thread_id", String.valueOf(thread_id));
			mapConversation.put("nom_contact",
					String.valueOf(cFil.getPosition()));
			mapConversation.put("nb_msg", Integer.toString(cMsg));
			mapConversation.put("last_msg", lst_msg);
			conversations.add(mapConversation);
		}
		cFil.close();

		SimpleAdapter adapteur = new SimpleAdapter(this, conversations,
				R.layout.liste_conversation_layout, new String[] {
						"nom_contact", "nb_msg", "last_msg" }, new int[] {
						R.id.nomcontact, R.id.nb_msg, R.id.lst_msg });
		m_LVconvstream = (ListView) findViewById(R.id.conversations);
		m_LVconvstream.setAdapter(adapteur);

		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> item_conversation = conversations
						.get(position);
			}
		};

		m_LVconvstream.setOnItemClickListener(listener);

		m_BTNSync = (Button) findViewById(R.id.btn_start_sync);
		m_BTNSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i_sync = null;
				if (Globales.typeAppareil == Globales.DeviceType.phone) {
					i_sync = new Intent(MainActivity.this,
							ConnexionMaitreActivity.class);

				} else if (Globales.typeAppareil == Globales.DeviceType.tablette) {
					i_sync = new Intent(MainActivity.this,
							ConfigurationConnexionActivity.class);
				}
				startActivityForResult(i_sync, CODE_APP);
			}
		});
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
			Intent intent = new Intent(getBaseContext(),
					SynchronisationActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	// Fonction qui permet d'initialiser toutes les bases de données.
	public static void initDatabase(Context context) {
		FilDataSource fds = new FilDataSource(context);
		SMSDataSource smsds = new SMSDataSource(context);
		SyncDataSource sds = new SyncDataSource(context);

		fds.open();
		smsds.open();
		sds.open();

		fds.close();
		smsds.close();
		sds.close();
	}
}
