package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;

public interface IBMovementBehaviour extends IBPhysicalBehaviour, IBImpulseCause{
	void nextImpulses(List<IBImpulse> ret);
	
	public static class Util{
		private static class BSkipStepMovementBehaviour implements IBMovementBehaviour{

			private IBMovementBehaviour _decorated;
			private int _skip;
			private int _counter;
			
			public BSkipStepMovementBehaviour(IBMovementBehaviour decorated, int skip ){
				_decorated = decorated;
				_skip = skip;
				_counter = skip;
			}
			
			@Override
			public void nextImpulses(List<IBImpulse> ret) {
				_counter -= 1;
				if( _counter <= 0 ){
					_counter = _skip;
					_decorated.nextImpulses(ret);
				}
			}
		}
		
		public static IBMovementBehaviour withSkip(IBMovementBehaviour b, int skip ){
			return new BSkipStepMovementBehaviour(b, skip);
		}
	}
}
