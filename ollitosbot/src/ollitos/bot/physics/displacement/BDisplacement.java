package ollitos.bot.physics.displacement;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.BAbstractPhysics;
import ollitos.bot.physics.BCollision;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.platform.BPlatform;

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
		if( delta() == BDirection.up || delta() == BDirection.down ){
			return;
		}
		
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
		
		computeCollisions(collisions, p.items() );
		
		for( IBCollision c : collisions ){
			BDisplacement d = new BPushDisplacement( this, delta(), c );
			ret.add(d);
		}
	}
	
	private void computeCollisions(List<IBCollision> ret, final IBPhysicalItem ... items) {
		if( ret == null ){
			throw new IllegalArgumentException();
		}
		
		IBPhysicalItem item = item();
		IBLocation delta = delta().vector();
		
		final IBRegion region = IBRegion.Util.traslate(item.region(), delta, null);

		for( final IBPhysicalItem i: items ){
			if( i == item ){
				continue;
			}
			final IBRegion intersection = IBRegion.Util.intersection(region, i.region(), null);
			if( intersection != null ){
				final BCollision c = new BCollision(intersection,item,i,this);
				ret.add( c );
			}
		}
	}
	

	@Override
	public final void fillAllInducedDisplacements(List<IBDisplacement> displacements){
		
		log( "Induced displacements for:" + this );
		for( BDisplacement d: directlyInducedDisplacements() ){
			log( " --> " + d );
		}
		
		for( BDisplacement d: directlyInducedDisplacements() ){
			displacements.add(d);
			d.fillAllInducedDisplacements(displacements);
		}
	}
	
	private void log(String msg) {
		//BPlatform.instance().logger().log(msg);
	}

	@Override
	public boolean apply() {
		log( "apply:" + this );
		if( !canBeApplied() ){
			log( "  can't be applied" );
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
			log( "  can't apply " + this + ": item is fixed" );
			return false;
		}
		
		for(final BDisplacement i: directlyInducedDisplacements() ){
			if( !i.canBeApplied() && i instanceof BPushDisplacement ){
				return false;
			}
		}
		boolean intersects = physics().intersects(item(), item().region(), physics().fixedItems() );
		if( !intersects ){
			log( "  can't apply " + this + ": intersects with fixed items" );
		}
		return !intersects;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + item() + ", " + delta() + ")";
	}

}
