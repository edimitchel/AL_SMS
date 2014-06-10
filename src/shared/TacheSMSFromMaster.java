package shared;

import java.sql.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cnam.al_sms.data.DataBaseHelper;
import com.cnam.al_sms.data.datasource.SMSDataSource;
import com.cnam.al_sms.gestionsms.ConversationController;
import com.cnam.al_sms.gestionsms.SynchroController;

public class TacheSMSFromMaster extends AsyncTask<String, Integer, Boolean> {
	private static final String TAG = "ALSMS";

	private ProgressDialog dialog = null;
	private Activity activity;
	private Context context;

	private static final Uri URI_SMS = Uri.parse("content://sms");
	private SMSDataSource sds;
	private ContentResolver cr;

	private boolean dialogShow = true;
	private int nombreSMS;

	public TacheSMSFromMaster(Context _context, boolean dialogShow) {
		this.dialogShow = dialogShow;
		this.context = _context;
		if (dialogShow) {
			dialog = new ProgressDialog(activity);
			dialog.setCancelable(false);
			dialog.setInverseBackgroundForced(false);
			dialog.setIndeterminate(false);
			dialog.setMax(100);

			if (!dialog.isShowing())
				dialog.show();
		}
		cr = context.getContentResolver();
		sds = new SMSDataSource(context);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (dialogShow) {
			dialog.setMessage("En cours de récupération .. (" + values[0]
					+ " / " + nombreSMS + ")");
			dialog.setProgress((values[0] / nombreSMS) * 100);
			super.onProgressUpdate(values);
		}
	}

	protected void onPreExecute() {
		if (dialogShow) {
			dialog.setTitle("Récupération des SMS de la base officielle.");
			dialog.setMessage("En cours de récupération..");
		}
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (dialogShow && dialog.isShowing()) {
			dialog.dismiss();
		}
		ConversationController.updateFils(context, false);
	}

	protected Boolean doInBackground(final String... args) {
		sds.open();

		long lastID = sds.getLastSMSId();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Long intervalle = Long.valueOf(prefs.getString("intervalle_sync",
				Globales.INTERVALLE_TEMPS_SYNC + "")) * 1000;

		final String[] whereArgs = new String[] {
				lastID + "",
				intervalle < 0 ? "0" : System.currentTimeMillis() - intervalle
						+ "" };
		int num_sms = 0;

		Cursor c = cr.query(URI_SMS, SMSDataSource.allColumns,
				DataBaseHelper.COLUMN_ID + " > ? AND "
						+ DataBaseHelper.COLUMN_DATE + " > ?", whereArgs,
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