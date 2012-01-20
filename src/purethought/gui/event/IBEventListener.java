package purethought.gui.event;


public interface IBEventListener {
	boolean handle(IBEvent e);
	IBEventSource source();
}
