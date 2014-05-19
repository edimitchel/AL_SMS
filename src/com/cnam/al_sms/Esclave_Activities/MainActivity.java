package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.HashMap;

import shared.Globales;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.maitre_activities.ConnexionMaitreActivity;
import com.cnam.al_sms.maitre_activities.SynchronisationActivity;

public class MainActivity extends AlsmsActivity {
	private static final String TAG = "ALSMS";

	private static final int CODE_APP = 98651;
	private ListView m_LVconvstream;
	private Button m_BTNSync;

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
		Globales.BTService = new BluetoothService(Globales.mHandler);
		Toast.makeText(this, "Je suis " + Globales.typeAppareil,
				Toast.LENGTH_LONG).show();

		HashMap<String, String> mapConversation;

		/*
		 * Cursor cFil = MessagerieController.getConversations(this);
		 * 
		 * Log.i(TAG, String.valueOf(cFil.getCount())); while
		 * (cFil.moveToNext()) { mapConversation = new HashMap<String,
		 * String>();
		 * 
		 * long thread_id = cFil.getLong(cFil
		 * .getColumnIndex(DataBaseHelper.COLUMN_ID)); String lst_msg =
		 * cFil.getString(cFil .getColumnIndex(DataBaseHelper.COLUMN_SNIPPET));
		 * int cMsg = cFil.getInt(cFil
		 * .getColumnIndex(DataBaseHelper.COLUMN_MESSAGECOUNT));
		 * 
		 * mapConversation.put("thread_id", thread_id+"");
		 * mapConversation.put("nom_contact"
		 * ,ContactController.getContactByThread(thread_id, this));
		 * mapConversation.put("nb_msg", Integer.toString(cMsg));
		 * mapConversation.put("last_msg", lst_msg);
		 * conversations.add(mapConversation); } cFil.close();
		 * 
		 * SimpleAdapter adapteur = new SimpleAdapter(this, conversations,
		 * R.layout.liste_conversation_layout, new String[] { "nom_contact",
		 * "nb_msg", "last_msg" }, new int[] { R.id.nomcontact, R.id.nb_msg,
		 * R.id.lst_msg }); m_LVconvstream = (ListView)
		 * findViewById(R.id.conversations);
		 * m_LVconvstream.setAdapter(adapteur);
		 * 
		 * OnItemClickListener listener = new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { HashMap<String, String> item_conversation =
		 * conversations .get(position); Intent intentConversation = new
		 * Intent(MainActivity.this, ConversationActivity.class);
		 * intentConversation.putExtra("threadID",
		 * item_conversation.get("thread_id"));
		 * 
		 * startActivity(intentConversation); } };
		 * 
		 * m_LVconvstream.setOnItemClickListener(listener);
		 */
		m_BTNSync = (Button) findViewById(R.id.btn_start_sync);
		m_BTNSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		// Affichage des contacts
		// ContactController.getContactFromMasterBase(this);
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

}
