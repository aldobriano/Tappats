package tappem.marguerite.threads;

public interface DownloadThreadListener {

	

	void handleDownloadThreadUpdate(DownloadTask dt);
	void handleUpdateUI(String text);
}
