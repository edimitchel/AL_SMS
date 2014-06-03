package com.cnam.al_sms.esclave_activities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.HashMap;

import shared.Globales;
import shared.Globales.DeviceType;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cnam.al_sms.ConversationActivity;
import com.cnam.al_sms.R;
import com.cnam.al_sms.connectivite.BluetoothService;
import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.gestionsms.ContactController;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.gestionsms.SynchroController;
import com.cnam.al_sms.maitre_activities.ConnexionMaitreActivity;
import com.cnam.al_sms.maitre_activities.SynchronisationActivity;
import com.cnam.al_sms.modeles.SMS;

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

		
		Cursor cFil = MessagerieController.getConversations(this);
		  
		  Log.i(TAG, String.valueOf(cFil.getCount())); while
		  (cFil.moveToNext()) { mapConversation = new HashMap<String,
		  String>();
		  
		  long thread_id = cFil.getLong(cFil
		  .getColumnIndex(DataBaseHelper.COLUMN_ID)); String lst_msg =
		  cFil.getString(cFil .getColumnIndex(DataBaseHelper.COLUMN_SNIPPET));
		  int cMsg = cFil.getInt(cFil
		  .getColumnIndex(DataBaseHelper.COLUMN_MESSAGECOUNT));
		  
		  mapConversation.put("thread_id", thread_id+"");
		  if(Globales.getDeviceType(getApplicationContext())==Globales.typeAppareil.phone){
			  mapConversation.put("nom_contact",ContactController.getContactByThread(thread_id, this));
		  }
		
		  mapConversation.put("nb_msg", Integer.toString(cMsg));
		  mapConversation.put("last_msg", lst_msg);
		  conversations.add(mapConversation); } cFil.close();
		  
		  SimpleAdapter adapteur = new SimpleAdapter(this, conversations,
		  R.layout.liste_conversation_layout, new String[] { "nom_contact",
		  "nb_msg", "last_msg" }, new int[] { R.id.nomcontact, R.id.nb_msg,
		  R.id.lst_msg }); m_LVconvstream = (ListView)
		  findViewById(R.id.conversations);
		  m_LVconvstream.setAdapter(adapteur);
		  
		  OnItemClickListener listener = new OnItemClickListener() {
		  
		  @Override public void onItemClick(AdapterView<?> parent, View view,
		  int position, long id) { HashMap<String, String> item_conversation =
		  conversations .get(position); Intent intentConversation = new
		  Intent(MainActivity.this, ConversationActivity.class);
		  intentConversation.putExtra("threadID",
		  item_conversation.get("thread_id"));
		  
		  startActivity(intentConversation); } };
		  
		  m_LVconvstream.setOnItemClickListener(listener);
		 m_BTNSync = (Button) findViewById(R.id.btn_start_sync);
		
		 m_BTNSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Globales.BTService.getState()==BluetoothService.STATE_CONNECTED){
					if(Globales.getDeviceType(getApplicationContext())==DeviceType.phone){
					SMSDataSource dataSMS = new SMSDataSource(Globales.curActivity);
					dataSMS.open();
					ArrayList<SMS> list = (ArrayList<SMS>) dataSMS.getNsms(10);
					
					byte[] listbytes;
					for(SMS sms:list){
						listbytes = SMS.getBytes(sms);
						
						Globales.BTService.send(listbytes);
						
					}
					
					dataSMS.close();
				}else if(Globales.getDeviceType(getApplicationContext())==DeviceType.tablette)
					{
					byte[] listbytes;
					listbytes = SMS.getBytes(new SMS(1,1,"0687974971",0,new Date(0),new Date(0),1,1,1,"","test",1));
					
					Globales.BTService.send(listbytes);
					}
				}
					else{
						Toast.makeText(getApplicationContext(), "Connexion nécéssaire", Toast.LENGTH_LONG).show();
					}
				
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
			SynchroController.getAllSmsFromMasterBase(this);
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
