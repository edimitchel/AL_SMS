package com.cnam.al_sms.esclave_activities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import shared.ConversationArrayAdapter;
import shared.Globales;
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
import com.cnam.al_sms.gestionsms.ConversationController;
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

		if (mContact != null && mContact.getThreadId() != 0) {
			loadSMS(mContact.getThreadId());

			mBTNEnvoi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String message = mETMessage.getText().toString();
					if (message.isEmpty())
						return;

					mETMessage.setText("");
					if (Globales.isPhone()) {
						MessagerieController.sendSMS(rootView.getContext(),
								"0605116117", message);
						showMessage(new SMS(message, new Date(Calendar
								.getInstance().getTimeInMillis()), 2));
					} else {
						byte[] listbytes;
						listbytes = SMS.getBytes(new SMS(1, 1, "0605116117", 0,
								new Date(0), new Date(0), 1, 1, 2, "", message,
								1));

						Globales.BTService.send(listbytes);
					}

				}
			});
		}

		return rootView;
	}

	private void loadSMS(Long threadId) {
		smsList = MessagerieController.getSMS(threadId, this.getActivity()
				.getApplicationContext());

		adapter = new ConversationArrayAdapter(this.getActivity()
				.getApplicationContext(), R.id.LV_conversation, smsList,
				mContact);

		mLVConversation.setAdapter(adapter);

		ConversationController.readSMS(rootView.getContext(),
				mContact.getThreadId());
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
