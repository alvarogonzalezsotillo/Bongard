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

	@Override
	public void nextMovement(List<IBDisplacement> ret) {
		computeSupportDisplacements( _item, _item.direction(), this, ret );
	}

	private static List<IBPhysicalContact> _contacts = new ArrayList<IBPhysicalContact>();

	public static void computeSupportDisplacements(IBPhysicalItem item, BDirection d, IBDisplacementCause cause, List<? super IBDisplacement> ret ){
		BPhysics p = item.physics();
		_contacts.clear();
		p.contacts(item, item.region(), BDirection.up, _contacts, p.movableItems() );
		for (IBPhysicalContact c : _contacts) {
			for( IBPhysicalItem i : c.items() ){
				if( i != item ){
					ret.add( (IBDisplacement)(new BSupportDisplacement(i, item, d.vector(), cause)) );
				}
			}
		}
	}

}
