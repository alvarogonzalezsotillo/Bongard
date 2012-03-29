package bongard.gui.container;

public interface IBDisposable {
	public void setUp();
	public void dispose();
	public boolean disposed();
}
