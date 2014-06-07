package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import shared.ConversationArrayAdapter;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.modeles.Contact;
import com.cnam.al_sms.modeles.SMS;

@SuppressLint("ValidFragment")
public class ConversationFragment extends Fragment {

	private Contact mContact;

	private List<SMS> smsList = new ArrayList<SMS>();

	private ListView mLVConversation;

	private EditText mETMessage;

	private ImageButton mBTNEnvoi;

	private View rootView;

	private ArrayAdapter<SMS> adapter;

	public ConversationFragment() {
	}

	public ConversationFragment(Contact contact) {
		mContact = contact;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_conversation, container,
				false);

		mLVConversation = (ListView) rootView
				.findViewById(R.id.LV_conversation);
		
		mETMessage = (EditText) rootView.findViewById(R.id.editText1);

		mBTNEnvoi = (ImageButton) rootView.findViewById(R.id.button1);

		if (mContact.getThreadId() != 0) {
			loadSMS(mContact.getThreadId());
			

			mBTNEnvoi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String message = mETMessage.getText().toString();
					if (message.isEmpty())
						return;

					mETMessage.setText("");
					MessagerieController.sendSMS(rootView.getContext(),
							"0605116117", message);
					showMessage(new SMS(message, Calendar.getInstance()
							.getTime()));
				}
			});
		}

		return rootView;
	}

	private void loadSMS(Long threadId) {
		smsList = MessagerieController.getSMS(threadId, this.getActivity()
				.getApplicationContext());

		adapter = new ConversationArrayAdapter(this.getActivity()
				.getApplicationContext(), R.id.LV_conversation, smsList, mContact);

		mLVConversation.setAdapter(adapter);
	}

	private void showMessage(SMS sms) {
		adapter.add(sms);
		adapter.notifyDataSetChanged();
		scrollDown();
	}

	private void scrollDown() {
		mLVConversation.setSelection(mLVConversation.getCount() - 1);
	}

}
