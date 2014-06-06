package shared;

import java.util.ArrayList;

import com.cnam.al_sms.R;
import com.cnam.al_sms.modeles.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		NavDrawerItem ndi = navDrawerItems.get(position);

		TypedArray navMenuIcons = this.context.getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		if (ndi.isImageUri()) {
			if (ndi.getIconUri() == null) {
				imgIcon.setImageResource(navMenuIcons.getResourceId(2, -1));
			} else {
				imgIcon.setImageURI(ndi.getIconUri());
			}
		} else if (navDrawerItems.get(position).getIcon() == 0) {
			imgIcon.setImageResource(navMenuIcons.getResourceId(2, -1));
		} else {
			imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		}
		navMenuIcons.recycle();
		txtTitle.setText(navDrawerItems.get(position).getTitle());

		// displaying count
		// check whether it set visible or not
		if (navDrawerItems.get(position).getCounterVisibility()) {
			txtCount.setText(navDrawerItems.get(position).getCount());
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
	}

}
