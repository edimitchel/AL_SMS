package shared;

import android.content.Context;
import android.os.AsyncTask;

import com.cnam.al_sms.data.datasource.FilDataSource;

public class TacheUpdate extends AsyncTask<String, Integer, Boolean> {

	private Context mContext;

	private boolean mDialogShow = true;

	private FilDataSource mFds;
	
	private Long mThreadId = null;

	// private List<Message> messages;
	public TacheUpdate(Context _context, boolean _dialogShow) {
		mContext = _context;
		mDialogShow = _dialogShow;
		mFds = new FilDataSource(mContext);
	}
	
	public TacheUpdate(Context _context, long _threadId, boolean _dialogShow) {
		mContext = _context;
		mDialogShow = _dialogShow;
		mFds = new FilDataSource(mContext);
		mThreadId = _threadId;
	}

	protected void onPreExecute() {
	}

	@Override
	protected void onPostExecute(final Boolean success) {
	}

	protected Boolean doInBackground(final String... args) {
		mFds.open();
		mFds.updateFils(mThreadId);
		mFds.close();

		return true;
	}
}