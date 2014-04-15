package shared;

import java.util.List;

import android.R;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

	private static class ViewHolder {
		TextView name;
		TextView mac_adress;
	}

	public BluetoothDeviceAdapter(Context context,
			List<BluetoothDevice> bluetoothdevices) {
		super(context, R.layout.two_line_list_item, bluetoothdevices);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		BluetoothDevice bd = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.two_line_list_item, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.text1);
			viewHolder.mac_adress = (TextView) convertView
					.findViewById(R.id.text2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		viewHolder.name.setText(bd.getName());
		viewHolder.mac_adress.setText(bd.getAddress());
		// Return the completed view to render on screen
		return convertView;
	}

}
