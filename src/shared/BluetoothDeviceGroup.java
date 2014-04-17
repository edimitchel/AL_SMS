package shared;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceGroup {
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

	public ArrayList<BluetoothDevice> getDevices() {
		return devices;
	}

	public String groupName;

	public String getGroupName() {
		return groupName;
	}

	public BluetoothDeviceGroup(String _name) {
		groupName = _name;
	}

	public void add(BluetoothDevice bd) {
		devices.add(bd);
	}

	public void addAll(List bd) {
		devices.addAll(bd);
	}

	public int size() {
		return devices.size();
	}
}
