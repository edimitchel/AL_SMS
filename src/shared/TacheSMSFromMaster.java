package shared;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.gestionsms.SynchroController;

public class TacheSMSFromMaster extends AsyncTask<String, Integer, Boolean> {
	private static final String TAG = "ALSMS";

	private ProgressDialog dialog;
	private Activity activity;
	private Context context;

	private static final Uri URI_SMS = Uri.parse("content://sms");
	private SMSDataSource sds;
	private ContentResolver cr;

	private int nombreSMS;

	// private List<Message> messages;
	public TacheSMSFromMaster(Activity _activity) {
		activity = _activity;
		context = _activity.getApplicationContext();
		cr = context.getContentResolver();
		sds = new SMSDataSource(context);

		dialog = new ProgressDialog(activity);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setIndeterminate(false);
		dialog.setMax(100);

		if (!dialog.isShowing())
			dialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		dialog.setMessage("En cours de récupération .. (" + values[0] + " / "
				+ nombreSMS + ")");
		dialog.setProgress((values[0] / nombreSMS) * 100);
		super.onProgressUpdate(values);
	}

	protected void onPreExecute() {
		dialog.setTitle("Récupération des SMS de la base officielle.");
		dialog.setMessage("En cours de récupération..");
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		SynchroController.updateFils(activity);
	}

	protected Boolean doInBackground(final String... args) {
		sds.open();

		long last_id = sds.getLastSMSId();
		final String[] whereArgs = new String[] { last_id + "" };

		int num_sms = 0;

		Cursor c = cr.query(URI_SMS, SMSDataSource.allColumns,
				DataBaseHelper.COLUMN_ID + " > ?", whereArgs,
				DataBaseHelper.COLUMN_ID);
		c.moveToFirst();
		nombreSMS = c.getCount();

		while (!c.isAfterLast()) {
			ContentValues vals = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(c, vals);
			long idsms = sds.creerSMS(vals);
			Log.i(TAG, "SMS id " + idsms + " copié.");

			num_sms++;
			publishProgress(num_sms);
			c.moveToNext();
		}
		c.close();
		sds.close();

		return true;
	}
}