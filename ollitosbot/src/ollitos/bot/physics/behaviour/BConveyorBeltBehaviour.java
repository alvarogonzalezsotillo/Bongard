package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;

public class BConveyorBeltBehaviour implements IBConveyorBeltBehaviour, IBImpulseCause{

	private IBPhysicalItem _item;

	public BConveyorBeltBehaviour(IBPhysicalItem item) {
		_item = item;
	}

	@Override
	public void nextMovement(List<IBImpulse> ret) {
		computeSupportDisplacements( _item, _item.direction(), this, ret, true );
	}


	private static List<IBPhysicalContact> _contacts = new ArrayList<IBPhysicalContact>();
	private static void computeSupportDisplacements(IBPhysicalItem item, BDirection d, IBImpulseCause cause, List<? super IBImpulse> ret, boolean conveyorBelt ){
		// TODO: How to not duplicate code with BDisplacement.computeDirectlyInducedSupportDisplacements?
		BPhysics p = item.physics();
		_contacts.clear();
		p.contacts(item, item.region(), BDirection.up, _contacts, p.movableItems() );
		for (IBPhysicalContact c : _contacts) {
			for( IBPhysicalItem i : c.items() ){
				if( i != item ){
					IBImpulse imp = new BImpulse(i, d.vector(), cause);
					ret.add( imp );
				}
			}
		}
	}
}
