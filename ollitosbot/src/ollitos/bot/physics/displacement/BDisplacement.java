package ollitos.bot.physics.displacement;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;

public abstract class BDisplacement implements IBDisplacement{

	private List<IBDisplacement> _inducedImpulses;
	private BDirection _delta;
	private IBDisplacement _cause;
	private IBImpulse _rootCause;
	private IBPhysicalItem _item;

	protected BDisplacement(IBPhysicalItem item, IBImpulse rootCause, BDirection delta ){
		this( item, rootCause, null, delta );
	}

	
	protected BDisplacement(IBPhysicalItem item, IBDisplacement cause, BDirection delta ){
		this( item, null, cause, delta );
	}
	
	protected BDisplacement(IBPhysicalItem item, IBImpulse rootCause, IBDisplacement cause, BDirection delta ){
		_item = item;
		_rootCause = rootCause;
		_cause = cause;
		_delta = delta;
	}
	
	public BPhysics physics(){
		return item().physics();
	}
	
	@Override
	public IBPhysicalItem item() {
		return _item;
	}

	@Override
	public IBImpulse rootCause() {
		if( _rootCause != null ){
			return _rootCause;
		}
		if( cause() != null ){
			return cause().rootCause();
		}
		return null;
	}

	@Override
	public IBDisplacement cause() {
		return _cause;
	}

	@Override
	public BDirection delta() {
		return _delta;
	}

	@Override
	public void apply() {
		for(IBDisplacement i: directlyInducedDisplacements() ){
			i.apply();
		}
		IBPhysicalItem item = item();
		item.traslateRegion(delta().vector());
	}

	@Override
	public List<IBDisplacement> directlyInducedDisplacements(){
		if( _inducedImpulses == null ){
			_inducedImpulses = computeDirectlyInducedDisplacements();
		}
		return _inducedImpulses;
	}
	
	@Override
	public final boolean canBeApplied() {
		for(IBDisplacement i: directlyInducedDisplacements() ){
			if( !i.canBeApplied() ){
				return false;
			}
		}
		return physics().intersects(item(), item().region(), physics().fixedItems() );
	}

	private List<IBDisplacement> computeDirectlyInducedDisplacements(){
		ArrayList<IBCollision> collisions = new ArrayList<IBCollision>();
		physics().computeCollisions(item(), delta().vector(), collisions, physics().items() );
		ArrayList<IBDisplacement> ret = new ArrayList<IBDisplacement>();
		for( IBCollision c: collisions ){
			BDisplacement d = new BPushDisplacement( c.pushed(), this, delta(), item() );
			ret.add(d);
		}
		return ret;
	}
}
