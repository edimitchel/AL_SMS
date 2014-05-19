package shared;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.cnam.al_sms.data.datasource.FilDataSource;

public class TacheUpdate extends AsyncTask<String, Integer, Boolean> {

	private Activity activity;
	private Context context;

	// private List<Message> messages;
	public TacheUpdate(Activity _activity) {
		activity = _activity;
		context = _activity.getApplicationContext();
	}

	protected void onPreExecute() {
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		/*
		 * if (dialog.isShowing()) { dialog.dismiss(); }
		 */
		activity.recreate();
	}

	protected Boolean doInBackground(final String... args) {
		FilDataSource fds = new FilDataSource(context);

		fds.open();
		fds.updateFils();
		fds.close();

		return true;
	}
}