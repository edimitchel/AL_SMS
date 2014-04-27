package com.cnam.al_sms.Esclave_Activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Conversations;
import android.telephony.TelephonyManager;
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

import com.cnam.al_sms.BuildConfig;
import com.cnam.al_sms.R;
import com.cnam.al_sms.Connectivite.BluetoothService;
import com.cnam.al_sms.Connectivite.ConnecBluetooth;

public class MainActivity extends Activity {
	private static final int CODE_APP = 98651;
	private ListView m_LVconvstream;
	private Button m_BTNSync;
	private BluetoothService bTService = new BluetoothService(this,
			new Handler());
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;

	/* variable liaison Bluetooth */
	// Debugging
	private static final String TAG = "ALSMS";
	private static final boolean D = true;

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

		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String type = null;
		if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			type = "une tablette";
		} else {
			type = "un mobile";
		}

		Toast.makeText(this, "Je suis " + type, Toast.LENGTH_LONG).show();

		ContentResolver cr = getContentResolver();

		HashMap<String, String> map;

		Cursor curConversations;
		Uri uConversations = Uri.parse("content://sms/conversations?simple=true");
		curConversations = cr.query(
				uConversations, null,
				null, null, "date DESC");
		
		Log.i(BuildConfig.TAG, Arrays.toString(curConversations.getColumnNames()));
		while (curConversations.moveToNext()) {
			/*ArrayList<String> values = new ArrayList<String>();
			for (int i = 0; i < curConversations.getColumnCount(); i++) {
				values.add(curConversations.getString(i));
			}
			Log.i(BuildConfig.TAG, values.toString());*/

			Log.i(BuildConfig.TAG, Arrays.toString(curConversations.getColumnNames()));
			map = new HashMap<String, String>();
			String thread_id = curConversations.getString(0);
			String lst_msg = curConversations.getString(curConversations
					.getColumnIndexOrThrow(Sms.Conversations.SNIPPET));
			int cMsg = curConversations.getInt(curConversations
					.getColumnIndexOrThrow(Sms.Conversations.MESSAGE_COUNT));
			// String recip =
			// curConversations.getString(curConversations.getColumnIndexOrThrow(Telephony.Threads.RECIPIENT_IDS));

			// Log.i("ALSMS",recip);
			/*
			 * Uri uriContact = Sms.CONTENT_URI; Cursor curContact =
			 * cr.q(uriContact, new String[]{Sms.PERSON,
			 * ContactsContract.Contacts.DISPLAY_NAME}, null, new
			 * String[]{thread_id}, null); curContact.moveToFirst(); String
			 * id_contact = curContact.getString(0); String nom_contact =
			 * curContact.getString(1); // String thumbnail_photo_uri =
			 * curContact.getString(2);
			 * 
			 * Log.i("smsview", Arrays.toString(curContact.getColumnNames()));
			 * Log.i("smsview", "Data : " + id_contact + " " + nom_contact);
			 * 
			 * // String nom_contact =
			 * curConversations.getString(curConversations
			 * .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			 */
			/*
			 * Log.i("testsms", Arrays.toString(curContact.getColumnNames()));
			 * String nom_contact = ""; Log.i("testsms", id_contact);
			 */

			/*
			 * byte[] photoBlob = photo.getBlob(photo
			 * .getColumnIndex(Photo.PHOTO)); final Bitmap photoBitmap =
			 * BitmapFactory .decodeByteArray(photoBlob, 0, photoBlob.length);
			 * QuickContactBadge qcb = (QuickContactBadge)
			 * findViewById(R.id.imageContact); qcb.setImageBitmap(photoBitmap);
			 */

			// String body = cur.getString(cur.getColumnIndex(Sms.BODY));

			map.put("thread_id", thread_id);
			map.put("nom_contact", "nope");
			map.put("nb_msg", Integer.toString(cMsg));
			map.put("last_msg", lst_msg);
			conversations.add(map);
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
				Intent i_sync = new Intent(MainActivity.this,
						ConfigurationConnexionActivity.class);
				startActivityForResult(i_sync, CODE_APP);
			}
		});
		try {
			ConnecBluetooth.checkBluetoothConnection(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		}
		return super.onOptionsItemSelected(item);
	}

}
