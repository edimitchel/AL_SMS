package shared;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.FilDataSource;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.gestionsms.SynchroController;

public class TacheSMSFromMaster extends AsyncTask<String, Integer, Boolean> {
	private static final String TAG = "ALSMS";

	private ProgressDialog dialog;
	private Context context;
	private int progress;

	private static final Uri URI_SMS = Uri.parse("content://sms");
	private SMSDataSource sds;
	private ContentResolver cr;

	private int nombreSMS;

	// private List<Message> messages;
	public TacheSMSFromMaster(Context _context) {
		context = _context;
		cr = context.getContentResolver();
		sds = new SMSDataSource(context);

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setIndeterminate(false);
		
		if(!dialog.isShowing())
			dialog.show();

		Cursor c_nb = context.getContentResolver().query(URI_SMS, null, null,
				null, null);
		nombreSMS = c_nb.getCount();
		c_nb.close();
	}

	protected void onPreExecute() {
		dialog.setTitle("Récupération des SMS de la base officiel.");
		dialog.setMessage("En cours de récupération..");
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		Toast.makeText(context, "Messages rappatriés sur la base maître.", Toast.LENGTH_LONG);
		SynchroController.updateFils(context);
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

		while (!c.isAfterLast()) {
			ContentValues vals = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(c, vals);
			long idsms = sds.creerSMS(vals);
			Log.i(TAG, "SMS id " + idsms + " copié.");
			progress = Math.round((num_sms / nombreSMS) * 100);
			
			/*dialog.setProgress(progress);
			dialog.setMessage(num_sms+" / " + nombreSMS);*/
			
			num_sms++;
			c.moveToNext();
		}
		c.close();
		sds.close();

		return true;
	}
}