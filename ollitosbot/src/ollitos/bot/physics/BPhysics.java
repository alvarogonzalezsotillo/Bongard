package ollitos.bot.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.behaviour.IBMovementBehaviour;
import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.displacement.IBPushDisplacement;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.items.BBall;
import ollitos.bot.view.BPhysicsView;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;


public class BPhysics extends BAbstractPhysics{

	private static final int STEP = 1;


	public BPhysics( final BPhysicsView view ){
		super(view, STEP);
	}


	private final List<IBImpulse> _impulses = new ArrayList<IBImpulse>();
	private final List<IBPhysicalItem> _movedItems = new ArrayList<IBPhysicalItem>();
	private final Map<IBPhysicalItem, IBRegion> _regions = new HashMap<IBPhysicalItem, IBRegion>();
	
	
	@Override
	public void step(){
		checkCollisions();

		_regions.clear();
		fillCurrentRegions( _regions, movableItems() );
		
		_impulses.clear();
		computeImpulsesOfBehaviours(_impulses);
		
		List<IBDisplacement> inducedDisplacements = new ArrayList<IBDisplacement>();
		for( IBImpulse i: _impulses ){
			for( IBDisplacement d : i.toDisplacements() ){
				inducedDisplacements.clear();
				d.fillAllInducedDisplacements(inducedDisplacements);
				
				for( IBDisplacement id: inducedDisplacements ){
					if( !(id instanceof IBPushDisplacement) ){
						continue;
					}
					
					IBPushDisplacement pd = (IBPushDisplacement) id;
					IBCollision collision = pd.causeCollision();
					notifyCollisions(collision);
				}
				
				d.apply();
			}
		}
		

		notifyItemsMoved( _regions );
		notifyStepFinished();
	}

	private void notifyItemsMoved(Map<IBPhysicalItem, IBRegion> regions) {
		for( IBPhysicalItem i: regions.keySet() ){
			IBRegion r = regions.get(i);
			if( !i.region().equals(r) ){
				notifyItemMoved(i, r);
			}
		}
	}

	private void fillCurrentRegions(Map<IBPhysicalItem, IBRegion> regions, IBPhysicalItem ... items ) {
		for( IBPhysicalItem i: items ){
			regions.put(i, new BRegion(i.region()) );
		}
	}

	private IBLogger logger() {
		return BPlatform.instance().logger();
	}



	private void computeImpulsesOfBehaviours(final List<IBImpulse> ret){
		computeImpulsesOfMovementBehaviours(ret);
	}

	private void computeImpulsesOfMovementBehaviours(final List<IBImpulse> ret) {
		final ArrayList<IBMovementBehaviour> list = new ArrayList<IBMovementBehaviour>();
		for( final IBPhysicalItem t: items() ){
			list.clear();
			t.behaviours(IBMovementBehaviour.class, list);
			for( final IBMovementBehaviour b: list ){
				b.nextImpulses(ret);
			}
		}
	}

}
