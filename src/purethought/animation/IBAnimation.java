package purethought.animation;



public interface IBAnimation {
	void stepAnimation(long millis);
	IBAnimable[] animables();
	void abortAnimation();
	boolean aborted();
	void applyAnimation();
	boolean endReached();
}
