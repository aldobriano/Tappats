package tappem.marguerite.threads;



/**
 * 
 * 
 */
public abstract class DownloadTask implements Runnable {


	public abstract int getStatus();
	
	public abstract Object getResult();
	
	
	
}
