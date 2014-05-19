package shared;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.esclave_activities.AlsmsActivity;
import com.cnam.al_sms.esclave_activities.ConnexionEsclaveActivity;

public class BluetoothDeviceAdapter extends BaseExpandableListAdapter {

	private AlsmsActivity contexte;
	private ArrayList<BluetoothDeviceGroup> groupes_peripherique;
	private LayoutInflater inflater;

	public BluetoothDeviceAdapter(AlsmsActivity context,
			ArrayList<BluetoothDeviceGroup> devices) {
		this.contexte = context;
		this.groupes_peripherique = devices;
		inflater = LayoutInflater.from(contexte);
	}

	@Override
	public int getGroupCount() {
		return groupes_peripherique.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((BluetoothDeviceGroup) getGroup(groupPosition)).size();
	}

	@Override
	public BluetoothDeviceGroup getGroup(int groupPosition) {
		return groupes_peripherique.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition).getDevices().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder gholder;

		BluetoothDeviceGroup groupe_peripherique = getGroup(groupPosition);

		if (convertView == null) {
			gholder = new GroupViewHolder();

			convertView = inflater.inflate(R.layout.el_titre_group, null);

			gholder.titreGroupExpanList = (TextView) convertView
					.findViewById(R.id.titreGroupExpanList);

			convertView.setTag(gholder);
		} else {
			gholder = (GroupViewHolder) convertView.getTag();
		}

		gholder.titreGroupExpanList.setText(groupe_peripherique.getGroupName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final BluetoothDevice objet = (BluetoothDevice) getChild(groupPosition,
				childPosition);

		ChildViewHolder childViewHolder;

		if (convertView == null) {
			childViewHolder = new ChildViewHolder();

			convertView = inflater.inflate(R.layout.el_enfant, null);

			childViewHolder.nomPeripherique = (TextView) convertView
					.findViewById(R.id.nomPeripherique);
			childViewHolder.adresseMacPeripherique = (TextView) convertView
					.findViewById(R.id.adresseMacPeripherique);

			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}

		childViewHolder.nomPeripherique.setText(objet.getName());

		childViewHolder.adresseMacPeripherique.setText(objet.getAddress());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent DeviceToConnect = new Intent(contexte,
						ConnexionEsclaveActivity.class);
				DeviceToConnect.putExtra("Adresse_MAC", objet.getAddress());
				DeviceToConnect.putExtra("Device_Name", objet.getName());
				contexte.startActivityForResult(DeviceToConnect, 0);
			}
		});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class GroupViewHolder {
		public TextView titreGroupExpanList;
	}

	class ChildViewHolder {
		public ImageView icone_peripherique;
		public TextView nomPeripherique;
		public TextView adresseMacPeripherique;
	}
}
