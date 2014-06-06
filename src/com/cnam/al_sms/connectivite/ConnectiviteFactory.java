package com.cnam.al_sms.connectivite;

import android.os.Handler;

public abstract class ConnectiviteFactory {
	protected int mState;
	protected final Handler mHandler;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
													// device

	public ConnectiviteFactory(Handler hand) {
		mHandler = hand;
	}

	/**
	 * Send to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out
	 *            The bytes to send
	 * @see ConnectedThread#send(byte[])
	 */
	public abstract void send(byte[] out);

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	protected abstract void connectionFailed();

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	protected abstract void connectionLost();
}
