package tappem.marguerite.threads;




import tappem.marguerite.SystemTools;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


public class UIThread extends Thread{	

	private static final String TAG = UIThread.class.getSimpleName();

	private Handler handler;

	private int totalQueued;

	private int totalCompleted;

	private UIThreadListener listener;

	public UIThread(UIThreadListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			// preparing a looper on current thread			
			// the current thread is being detected implicitly
			Looper.prepare();

			Log.i(TAG, "UIThread entering the loop");

			// now, the handler will automatically bind to the
			// Looper that is attached to the current thread
			// You don't need to specify the Looper explicitly
			handler = new Handler();

			// After the following line the thread will start
			// running the message loop and will not normally
			// exit the loop unless a problem happens or you
			// quit() the looper (see below)
			Looper.loop();

			Log.i(TAG, "UIThread exiting gracefully");
		} catch (Throwable t) {
			Log.e(TAG, "UIThread halted due to an error", t);
		} 
	}

	// This method is allowed to be called from any thread
	public synchronized void requestStop() {
		// using the handler, post a Runnable that will quit()
		// the Looper attached to our UIThread
		// obviously, all previously queued tasks will be executed
		// before the loop gets the quit Runnable
		handler.post(new Runnable() {
			@Override
			public void run() {
				// This is guaranteed to run on the UIThread
				// so we can use myLooper() to get its looper
				Log.i(TAG, "UIThread loop quitting by request");

				Looper.myLooper().quit();
			}
		});
	}

	public synchronized void enqueueUI(final UITask task, Context context) {
		// Wrap UITask into another Runnable to track the statistics

		if(!SystemTools.checkInternet(context))
		{
			String text = "Please verify that you have Internet Connection";

			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					task.run();
				} finally {					
					// register task completion
					synchronized (UIThread.this) {
						totalCompleted++;


					}
					// tell the listener something has happened
					signalUpdate(task);
				}				
			}
		});

		totalQueued++;
		// tell the listeners the queue is now longer
		//signalUpdate();
	}

	public synchronized int getTotalQueued() {
		return totalQueued;
	}

	public synchronized int getTotalCompleted() {
		return totalCompleted;
	}

	// Please note! This method will normally be called from the download thread.
	// Thus, it is up for the listener to deal with that (in case it is a UI component,
	// it has to execute the signal handling code in the UI thread using Handler - see
	// UIQueueActivity for example).
	private void signalUpdate(UITask dt) {
		if (listener != null) {
			listener.handleUIThreadUpdate(dt);
		}
	}

}
