package shared;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.modeles.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	private ImageView imgIcon;
	private TextView txtTitle;
	private TextView txtCount;
	private TextView txtCountUnread;

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

		imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		txtTitle = (TextView) convertView.findViewById(R.id.title);
		txtCount = (TextView) convertView.findViewById(R.id.counter);
		txtCountUnread = (TextView) convertView
				.findViewById(R.id.counterUnread);

		NavDrawerItem ndi = navDrawerItems.get(position);

		TypedArray navMenuIcons = this.context.getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		if (ndi.isImageBitmap()) {
			if (ndi.getBitmap() == null) {
				imgIcon.setImageResource(navMenuIcons.getResourceId(2, -1));
			} else {
				imgIcon.setImageBitmap(ndi.getBitmap());
			}
		} else if (navDrawerItems.get(position).getIcon() == 0) {
			imgIcon.setImageResource(navMenuIcons.getResourceId(2, -1));
		} else {
			imgIcon.setImageResource(ndi.getIcon());
		}
		txtTitle.setText(ndi.getTitle());

		if (ndi.getCounterVisibility()) {
			txtCount.setText(ndi.getCount());
			if (Integer.valueOf(ndi.getCountUnread()) > 0) {
				txtCountUnread.setVisibility(View.VISIBLE);
				txtCount.setVisibility(View.INVISIBLE);
			} else {
				txtCount.setVisibility(View.VISIBLE);
				txtCountUnread.setVisibility(View.INVISIBLE);
			}
		}

		navMenuIcons.recycle();

		return convertView;
	}

}
