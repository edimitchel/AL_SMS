package shared;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cnam.al_sms.data.datasource.FilDataSource;

public class TacheUpdate extends AsyncTask<String, Integer, Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private int progress;

	// private List<Message> messages;
	public TacheUpdate(Context _context) {
		context = _context;
		/*
		 * dialog = new ProgressDialog(context); dialog.setCancelable(false);
		 * dialog.setInverseBackgroundForced(false);
		 */
	}

	protected void onPreExecute() {
		/*
		 * dialog.setTitle("Mise à jour des conversations..");
		 * dialog.setMessage("En cours .. (" + "/" + ")"); dialog.show();
		 */
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		/*
		 * if (dialog.isShowing()) { dialog.dismiss(); }
		 */
		Toast.makeText(context, "FINISH", Toast.LENGTH_SHORT);
	}

	protected Boolean doInBackground(final String... args) {
		FilDataSource fds = new FilDataSource(context);

		fds.open();
		fds.updateFils();
		fds.close();

		return true;
	}
}