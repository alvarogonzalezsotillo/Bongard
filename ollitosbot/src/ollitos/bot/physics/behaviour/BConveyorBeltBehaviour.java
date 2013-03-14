package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;

public class BConveyorBeltBehaviour implements IBConveyorBeltBehaviour, IBImpulseCause{

	private IBPhysicalItem _item;

	public BConveyorBeltBehaviour(IBPhysicalItem item) {
		_item = item;
	}

	@Override
	public void nextImpulses(List<IBImpulse> ret) {
		computeSupportDisplacements( _item, _item.direction(), ret );
	}


	private static List<IBPhysicalContact> _contacts = new ArrayList<IBPhysicalContact>();
	private void computeSupportDisplacements(IBPhysicalItem item, BDirection d, List<? super IBImpulse> ret ){
		
		// TODO: How to not duplicate code with BDisplacement.computeDirectlyInducedSupportDisplacements?
		IBPhysics p = item.physics();
		_contacts.clear();
		p.contacts(item, item.region(), BDirection.up, _contacts, p.movableItems() );
		for (IBPhysicalContact c : _contacts) {
			for( IBPhysicalItem i : c.items() ){
				if( i != item ){
					IBImpulse imp = new ConveyorBeltImpulse(i);
					ret.add( imp );
				}
			}
		}
	}
	
	private class ConveyorBeltImpulse extends BImpulse{
		public ConveyorBeltImpulse(IBPhysicalItem i ) {
			super(i, _item.direction().vector(), BConveyorBeltBehaviour.this);
		}
	}
}
