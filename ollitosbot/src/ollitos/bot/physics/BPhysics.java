package ollitos.bot.physics;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.physics.behaviour.IBMovementBehaviour;
import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.displacement.IBPushDisplacement;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.view.BPhysicsView;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;


public class BPhysics extends BAbstractPhysics{

	private static final int STEP = 50;


	public BPhysics( final BPhysicsView view ){
		super(view, STEP);
	}


	private final ArrayList<IBImpulse> _impulses = new ArrayList<IBImpulse>();

	@Override
	public void step(){
		logger().log( "********  STEP **********" );
		checkCollisions();

		// TODO: store current positions and trigger itemMoved messages at end of step
		
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



		notifyStepFinished();
	}

	private void storePositions() {
		// TODO Auto-generated method stub
		
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
