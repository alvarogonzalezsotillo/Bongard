package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.BSupportDisplacement;
import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.displacement.IBDisplacementCause;

public class BConveyorBeltBehaviour implements IBConveyorBeltBehaviour, IBDisplacementCause{

	private IBPhysicalItem _item;

	public BConveyorBeltBehaviour(IBPhysicalItem item) {
		_item = item;
	}

	List<IBPhysicalContact> _contacts = new ArrayList<IBPhysicalContact>();
	@Override
	public void nextMovement(List<IBDisplacement> ret) {
		BPhysics p = _item.physics();
		_contacts.clear();
		p.contacts(_item, _item.region(), BDirection.up, _contacts, p.movableItems() );
		for (IBPhysicalContact c : _contacts) {
			for( IBPhysicalItem i : c.items() ){
				if( i != _item ){
					ret.add( new BSupportDisplacement(i, _item, _item.direction().vector(), this) );
				}
			}
		}
	}

}
