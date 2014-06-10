package shared;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnam.al_sms.R;
import com.cnam.al_sms.gestionsms.ContactController;
import com.cnam.al_sms.gestionsms.ConversationController;
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

		// Numero de la ressource correspondant à la ligne message
		int r = sms.getType() == 2 ? R.layout.message_view_me
				: R.layout.message_view;

		if (convertView == null) {
			viewHolder = new ViewHolder();

			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(r, null);
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
		if (uImage != null) {
			try {
				viewHolder.image.setImageBitmap(ContactController.getRoundImageContact(mContext, uImage));
			} catch (IOException e) {
				viewHolder.image.setImageResource(R.drawable.ic_contact);
			}
		} else {
			viewHolder.image.setImageResource(R.drawable.ic_contact);
		}

		viewHolder.message.setText(sms.getMessage());
		viewHolder.date.setText(ConversationController.getDateTime((Date) sms
				.getDate()));
		return convertView;
	}
}
