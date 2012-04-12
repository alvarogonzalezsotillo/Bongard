package ollitos.animation;

public abstract class BAnimation implements IBAnimation{

	private IBAnimable[] _a;

	public BAnimation(IBAnimable ... a){
		_a = a;
	}
	
	@Override
	public boolean needsUpdate() {
		return !endReached();
	}

	
	@Override
	public final IBAnimable[] animables() {
		return _a.clone();
	}
	
	@Override
	public void setAnimables(IBAnimable...a){
		_a = a.clone();
	}
}
