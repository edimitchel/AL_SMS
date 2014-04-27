package com.cnam.al_sms.Connectivite;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.cnam.al_sms.BuildConfig;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for incoming
 * connections, a thread for connecting with a device, and a thread for
 * performing data transmissions when connected.
 */
public class BluetoothService {
	// Debugging
	private static final String TAG = "BluetoothService";

	// Name for the SDP record when creating server socket
	private static final String NAME = "BluetoothALSMS";

	// Unique UUID for this application
	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	// Member fields
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;
	private Context mContext;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
													// device

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 * 
	 * @param context
	 *            The UI Activity Context
	 * @param handler
	 *            A Handler to send messages back to the UI Activity
	 */
	public BluetoothService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mHandler = handler;
		mContext = context;
	}

	/**
	 * Set the current state of the chat connection
	 * 
	 * @param state
	 *            An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
	public synchronized void start() {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "start");

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to listen on a BluetoothServerSocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		setState(STATE_LISTEN);
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * 
	 * @param device
	 *            The BluetoothDevice to connect
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "connect to: " + device);

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 * 
	 * @param socket
	 *            The BluetoothSocket on which the connection was made
	 * @param device
	 *            The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "connected");

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Cancel the accept thread because we only want to connect to one
		// device
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		setState(STATE_CONNECTED);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "stop");
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out
	 *            The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		setState(STATE_LISTEN);
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		setState(STATE_LISTEN);
	}
	
	/* thread permettant d'accepter une demande */

	 class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
						ConnecBluetooth.APPNAME, ConnecBluetooth.APPUUID);
			} catch (IOException e) {
			}
			mmServerSocket = tmp;
		}

		public void run() {
			BluetoothSocket socket = null;
			while (true) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					break;
				}

				if (socket != null) {
					synchronized (BluetoothService.this) {
	                    switch (mState) {
	                    case BluetoothService.STATE_LISTEN:
	                    case BluetoothService.STATE_CONNECTING:
	                        // Situation normal. Start the connected thread.
	                    	/*if(ConnecBluetooth.askAcceptBluetooth(socket.getRemoteDevice())){
	                    		connected(socket, socket.getRemoteDevice());
	                    	}
	                    	else{
	                    		try {
									socket.close();
								} catch (IOException e) {
									
									e.printStackTrace();
								}
	                    	}*/
	                    	
	                        break;
	                    case BluetoothService.STATE_NONE:
	                    case BluetoothService.STATE_CONNECTED:
	                        // Either not ready or already connected. Terminate new socket.
	                        try {
	                            socket.close();
	                        } catch (IOException e) {
	                            Log.e(TAG, "Could not close unwanted socket", e);
	                        }
	                        break;
	                    }
				}
				}
			}
		}

		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
			}
		}

	}
		/* thread permettant de communiquer entre les 2 appareils */
		class ConnectedThread extends Thread {
			private final BluetoothSocket mmSocket;
			private final InputStream mmInStream;
			private final OutputStream mmOutStream;

			public ConnectedThread(BluetoothSocket socket) {
				mmSocket = socket;
				InputStream tmpIn = null;
				OutputStream tmpOut = null;

				// Get the input and output streams, using temp objects because
				// member streams are final
				try {
					tmpIn = socket.getInputStream();
					tmpOut = socket.getOutputStream();
				} catch (IOException e) {
				}

				mmInStream = tmpIn;
				mmOutStream = tmpOut;
			}

			public void run() {
				byte[] buffer = new byte[1024]; // buffer store for the stream
				int bytes; // bytes returned from read()

				// Keep listening to the InputStream until an exception occurs
				while (true) {
					try {
						// Read from the InputStream
						bytes = mmInStream.read(buffer);
						// Send the obtained bytes to the UI activity
						// mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
						// .sendToTarget();
					} catch (IOException e) {
						break;
					}
				}
			}

			/* Call this from the main activity to send data to the remote device */
			public void write(byte[] bytes) {
				try {
					mmOutStream.write(bytes);
				} catch (IOException e) {
				}
			}

			/* Call this from the main activity to shutdown the connection */
			public void cancel() {
				try {
					mmSocket.close();
				} catch (IOException e) {
				}
			}
		}
		
		/* thread permettant de demander � se connecter � un appareil */
		class ConnectThread extends Thread {
			private final BluetoothSocket mmSocket;
			private final BluetoothDevice mmDevice;
			private final BluetoothAdapter mBluetoothAdapter;

			public ConnectThread(BluetoothDevice device) {
				// Use a temporary object that is later assigned to mmSocket,
				// because mmSocket is final
				BluetoothSocket tmp = null;
				mmDevice = device;
				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

				// Get a BluetoothSocket to connect with the given BluetoothDevice
				try {
					// MY_UUID is the app's UUID string, also used by the server code
					tmp = device
							.createRfcommSocketToServiceRecord(ConnecBluetooth.APPUUID);
				} catch (IOException e) {
				}
				mmSocket = tmp;
				
				
			}

			public void run() {
				// Cancel discovery because it will slow down the connection
				mBluetoothAdapter.cancelDiscovery();

				try {
					// Connect the device through the socket. This will block
					// until it succeeds or throws an exception
					mmSocket.connect();
				} catch (IOException connectException) {
					// Unable to connect; close the socket and get out
					try {
						mmSocket.close();
					} catch (IOException closeException) {
					}
					return;
				}

				// Do work to manage the connection (in a separate thread)

			}

			/** Will cancel an in-progress connection, and close the socket */
			public void cancel() {
				try {
					mmSocket.close();
				} catch (IOException e) {
				}
			}
		}

}
