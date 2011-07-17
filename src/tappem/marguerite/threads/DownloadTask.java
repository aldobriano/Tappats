package tappem.marguerite.threads;

import java.util.Random;

import android.util.Log;

/**
 * 
 * 
 */
public abstract class DownloadTask implements Runnable {


	public abstract int getStatus();
	
	public abstract Object getResult();
	
	
	
}
