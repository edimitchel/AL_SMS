package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.HashMap;

import shared.Globales;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony.Sms;
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

		ContentResolver cr = getContentResolver();

		HashMap<String, String> mapConversation;

		Cursor curConversations;
		Uri uConversations = Uri
				.parse("content://sms/conversations?simple=true");
		curConversations = cr.query(uConversations, null, null, null,
				"date DESC");

		//Log.i(TAG, Arrays.toString(curConversations.getColumnNames()));
		while (curConversations.moveToNext()) {
			/*
			 * For debbuging ArrayList<String> values = new ArrayList<String>();
			 * for (int i = 0; i < curConversations.getColumnCount(); i++) {
			 * values.add(curConversations.getString(i)); } Log.i(TAG,
			 * values.toString());
			 */
			//Log.i(TAG, Arrays.toString(curConversations.getColumnNames()));

			mapConversation = new HashMap<String, String>();
			String thread_id = curConversations.getString(0);
			String lst_msg = curConversations.getString(curConversations
					.getColumnIndexOrThrow(Sms.Conversations.SNIPPET));
			int cMsg = curConversations.getInt(curConversations
					.getColumnIndexOrThrow(Sms.Conversations.MESSAGE_COUNT));
			

			ArrayList<String> nomsContact = new ArrayList<String>();

			// Curseur pour récupérer le contact
			/*
			 * Cursor curContact =
			 * cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] {
			 * ContactsContract.Contacts.DISPLAY_NAME},
			 * ContactsContract.Contacts._ID + " = ?", recipients_id,
			 * ContactsContract.Contacts.DISPLAY_NAME + " ASC"); while
			 * (curContact.moveToNext()) { nomsContact
			 * .add(curContact.getString(curContact
			 * .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))); }
			 * curContact.close();
			 * 
			 * String nomContactJoined = "";
			 * 
			 * for (int n = 0; n < nomsContact.size(); n++) {
			 * nomContactJoined.concat(nomsContact.get(n)); if (n <
			 * nomsContact.size() - 1) nomContactJoined.concat(", "); }
			 */

			mapConversation.put("thread_id", thread_id);
			mapConversation.put("nom_contact",
					String.valueOf(curConversations.getPosition()));
			mapConversation.put("nb_msg", Integer.toString(cMsg));
			mapConversation.put("last_msg", lst_msg);
			conversations.add(mapConversation);
		}
		curConversations.close();

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
				Log.d("ALSMS", item_conversation.toString());
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

}
