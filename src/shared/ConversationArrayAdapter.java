package shared;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.modeles.Contact;
import com.cnam.al_sms.modeles.SMS;

public class ConversationArrayAdapter extends ArrayAdapter<SMS> {

	private Context mContext;
	private Contact mContact;
	private List<SMS> SMSList;

	private static class ViewHolder {
		TextView message;
		TextView date;
		ImageView image;
	}

	public ConversationArrayAdapter(Context context, int resource,
			List<SMS> objects, Contact contact) {
		super(context, resource, objects);
		this.mContext = context;
		SMSList = objects;
		mContact = contact;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SMS sms = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.message_view, null);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.imageContact);
			viewHolder.message = (TextView) convertView
					.findViewById(R.id.temp_message);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.dateMessage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Uri uImage = mContact.getImageURI();
		if (uImage != null)
			viewHolder.image.setImageURI(uImage);
		else
			viewHolder.image.setImageResource(R.drawable.ic_contact);
		viewHolder.message.setText(sms.getMessage());
		viewHolder.date.setText(Globales.dateFormat.format(sms.getDate()));
		return convertView;
	}
}
