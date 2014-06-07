package shared;

import android.content.Context;
import android.os.AsyncTask;

import com.cnam.al_sms.data.datasource.FilDataSource;

public class TacheUpdate extends AsyncTask<String, Integer, Boolean> {

	private Context context;

	private boolean dialogShow = true;

	private FilDataSource fds;

	// private List<Message> messages;
	public TacheUpdate(Context _context, boolean _dialogShow) {
		context = _context;
		dialogShow = _dialogShow;
		fds = new FilDataSource(context);
	}

	protected void onPreExecute() {
	}

	@Override
	protected void onPostExecute(final Boolean success) {
	}

	protected Boolean doInBackground(final String... args) {
		fds.open();
		fds.updateFils();
		fds.close();

		return true;
	}
}