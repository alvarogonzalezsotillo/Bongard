package ollitos.bot.physics.displacement;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;
import ollitos.bot.physics.impulse.IBImpulse;

public abstract class BDisplacement implements IBDisplacement{

	private final BDirection _delta;
	private final IBDisplacement _cause;
	private final IBImpulse _rootCause;
	private final IBPhysicalItem _item;
	private List<BDisplacement> _directlyInducedDisplacements;

	protected BDisplacement(final IBPhysicalItem item, final IBImpulse rootCause, final BDirection delta ){
		this( item, rootCause, null, delta );
	}

	protected BDisplacement(final IBPhysicalItem item, final IBDisplacement cause, final BDirection delta ){
		this( item, null, cause, delta );
	}

	protected BDisplacement(final IBPhysicalItem item, final IBImpulse rootCause, final IBDisplacement cause, final BDirection delta ){
		_item = item;
		_rootCause = rootCause;
		_cause = cause;
		_delta = delta;
	}

	public IBPhysics physics(){
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

	private List<BDisplacement> directlyInducedDisplacements(){
		if (_directlyInducedDisplacements == null) {
			_directlyInducedDisplacements = new ArrayList<BDisplacement>();
			computeDirectlyInducedPushDisplacements( _directlyInducedDisplacements);
			computeDirectlyInducedSupportDisplacements( _directlyInducedDisplacements);
		}

		return _directlyInducedDisplacements;
	}

	private void computeDirectlyInducedSupportDisplacements(List<BDisplacement> ret){
		IBPhysics p = physics();
		List<IBPhysicalContact> contacts = new ArrayList<IBPhysicalContact>();
		p.contacts(item(), item().region(), BDirection.up, contacts, p.movableItems() );
		for (IBPhysicalContact c : contacts) {
			for( IBPhysicalItem i : c.items() ){
				if( i != item() ){
					BDisplacement d = new BSupportDisplacement( i, this, delta(), item() );
					ret.add( d );
				}
			}
		}
	}
	
	private void computeDirectlyInducedPushDisplacements(List<BDisplacement> ret){
		IBPhysics p = physics();
		List<IBCollision> collisions = new ArrayList<IBCollision>();
		
		p.computeCollisions(item(), delta().vector(), collisions, p.items() );
		
		for( IBCollision c : collisions ){
			BDisplacement d = new BPushDisplacement( this, delta(), c );
			ret.add(d);
		}
	}

	@Override
	public final void fillAllInducedDisplacements(List<IBDisplacement> displacements){
		for( BDisplacement d: directlyInducedDisplacements() ){
			displacements.add(d);
			d.fillAllInducedDisplacements(displacements);
		}
	}
	
	@Override
	public boolean apply() {
		if( !canBeApplied() ){
			return false;
		}
		for(final IBDisplacement i: directlyInducedDisplacements() ){
			i.apply();
		}
		final IBPhysicalItem item = item();
		item.traslateRegion(delta().vector());
		return true;
	}


	private boolean canBeApplied() {
		if( BFixedThingBehaviour.is( item() ) ){
			return false;
		}
		
		for(final BDisplacement i: directlyInducedDisplacements() ){
			if( !i.canBeApplied() && i instanceof BPushDisplacement ){
				return false;
			}
		}
		return physics().intersects(item(), item().region(), physics().fixedItems() );
	}


}
