package com.cnam.al_sms;

import java.util.ArrayList;
import java.util.List;

import shared.ConversationArrayAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cnam.al_sms.gestionsms.ContactController;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.modeles.SMS;

public class ConversationActivity extends Activity {

	private static final String TAG = "ALSMS";

	private List<SMS> smsList = new ArrayList<SMS>();

	private ListView mLVConversation; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		
		Bundle bundle = this.getIntent().getExtras();
		
		mLVConversation = (ListView) findViewById(R.id.LV_conversation);

		Long threadId = Long.valueOf(bundle.getString("threadID","0"));
		if (threadId != 0) {
			loadSMS(threadId);
			
			String contactName = ContactController.getContactByThread(threadId, this);
			this.setTitle(contactName);
		} else {
			finishActivity(-1);
		}
	}

	private void loadSMS(Long threadId) {
		smsList = MessagerieController.getSMS(threadId, this);
		Log.i(TAG,"Nombre SMS: "+smsList.size());
		
		ArrayAdapter<SMS> adapter = new ConversationArrayAdapter(this, R.id.LV_conversation, smsList);
		
		mLVConversation.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversation, menu);
		return true;
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
