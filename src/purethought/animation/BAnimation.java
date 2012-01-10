package purethought.animation;

public abstract class BAnimation implements IBAnimation{

	private IBAnimable[] _a;
	private boolean _aborted = false;

	public BAnimation(IBAnimable[] a){
		_a = a;
	}
	
	@Override
	public final IBAnimable[] animables() {
		return _a.clone();
	}

	@Override
	public final IBAnimation abortAnimation() {
		_aborted = true;
		for( IBAnimable a: animables() ){
			a.abortAnimation(this);
		}
		return null;
	}
	
	@Override
	public boolean aborted() {
		return _aborted;
	}



	@Override
	public final void applyAnimation() {
		for( IBAnimable a: animables() ){
			a.applyAnimation(this);
		}
	}
}
