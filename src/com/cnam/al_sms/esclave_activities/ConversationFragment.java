package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.gestionsms.ConversationController;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.modeles.SMS;

@SuppressLint("ValidFragment")
public class ConversationFragment extends Fragment {

	private long mThreadId;

	private List<SMS> smsList = new ArrayList<SMS>();

	private ListView mLVConversation;

	private EditText mETMessage;

	private Button mBTNEnvoi;
	
	private View rootView;

	public ConversationFragment() {
	}

	public ConversationFragment(long _threadId) {
		mThreadId = _threadId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_conversation,
				container, false);

		mLVConversation = (ListView) rootView
				.findViewById(R.id.LV_conversation);

		mETMessage = (EditText) rootView
				.findViewById(R.id.editText1);

		mBTNEnvoi = (Button) rootView
				.findViewById(R.id.button1);

		if (mThreadId != 0) {
			loadSMS(mThreadId);
			
			mBTNEnvoi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String message = mETMessage.getText().toString();
					MessagerieController.sendSMS(rootView.getContext(), "0605116117", message);
				}
			});
		}

		return rootView;
	}

	private void loadSMS(Long threadId) {
		smsList = MessagerieController.getSMS(threadId, this.getActivity()
				.getApplicationContext());

		ArrayAdapter<SMS> adapter = new ConversationArrayAdapter(this
				.getActivity().getApplicationContext(), R.id.LV_conversation,
				smsList);

		mLVConversation.setAdapter(adapter);
	}

}
