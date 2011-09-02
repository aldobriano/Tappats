package tappem.marguerite.threads;

public abstract class UITask implements Runnable{
	public abstract int getStatus();
	
	public abstract Object getResult();
}
