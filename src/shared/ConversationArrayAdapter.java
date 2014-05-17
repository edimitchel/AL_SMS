package shared;

import java.util.List;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cnam.al_sms.modeles.SMS;

public class ConversationArrayAdapter extends ArrayAdapter<SMS> {

	Context context;
	List<SMS> SMSs;

	private static class ViewHolder {
		TextView message;
	}

	public ConversationArrayAdapter(Context context, int resource, List<SMS> objects) {
		super(context, resource, objects);
		this.context = context;
		SMSs = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SMS sms = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.simple_list_item_1, null);
			viewHolder.message = (TextView) convertView
					.findViewById(R.id.text1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.message.setText(sms.getMessage());
		return convertView;
	}
}
