package animation;



public interface IBAnimation {
	void stepAnimation(long millis);
	IBAnimable[] animables();
	IBAnimation abortAnimation();
	boolean aborted();
	void applyAnimation();
	boolean endReached();
}
