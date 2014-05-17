package com.cnam.al_sms;

import java.util.ArrayList;
import java.util.List;

import shared.ConversationArrayAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.modeles.SMS;

public class ConversationActivity extends Activity {

	private ArrayList<SMS> smsList = new ArrayList<SMS>();

	private ListView mLVConversation; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		
		mLVConversation = (ListView) findViewById(R.id.LV_conversation);

		Long threadId = savedInstanceState.getLong("threadID");
		if (threadId != null) {
			//loadSMS(threadId);
			Toast.makeText(this, "Conversation à charger : "+threadId, Toast.LENGTH_LONG).show();
		} else {
			finishActivity(-1);
		}
	}

	private void loadSMS(Long threadId) {
		List<SMS> sms = MessagerieController.getSMS(threadId, this);
		
		ArrayAdapter<SMS> adapter = new ConversationArrayAdapter(this, R.id.LV_conversation, sms);
		
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
