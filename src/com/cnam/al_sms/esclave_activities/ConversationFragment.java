package com.cnam.al_sms.esclave_activities;

import java.util.ArrayList;
import java.util.List;

import shared.ConversationArrayAdapter;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.gestionsms.ContactController;
import com.cnam.al_sms.gestionsms.MessagerieController;
import com.cnam.al_sms.modeles.SMS;

@SuppressLint("ValidFragment")
public class ConversationFragment extends Fragment {

	private long mThreadId;

	private List<SMS> smsList = new ArrayList<SMS>();

	private ListView mLVConversation;

	public ConversationFragment() {
	}

	public ConversationFragment(long _threadId) {
		mThreadId = _threadId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_conversation,
				container, false);

		mLVConversation = (ListView) rootView
				.findViewById(R.id.LV_conversation);

		if (mThreadId != 0) {
			loadSMS(mThreadId);
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
