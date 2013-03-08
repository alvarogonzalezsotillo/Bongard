package ollitos.bot.physics;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.physics.behaviour.IBMovementBehaviour;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.view.BPhysicsView;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;


public class BPhysics extends BAbstractPhysics{

	private static final int STEP = 1;


	public BPhysics( final BPhysicsView view ){
		super(view, STEP);
	}


	private final ArrayList<IBImpulse> _impulses = new ArrayList<IBImpulse>();

	@Override
	public void step(){
		logger().log( "********  STEP **********" );
		checkCollisions();
		_impulses.clear();
		computeImpulsesOfBehaviours(_impulses);




		notifyStepFinished();
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
				b.nextMovement(ret);
			}
		}
	}





}
