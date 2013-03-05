package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.BSupportDisplacement;
import ollitos.bot.physics.displacement.IBImpulse;
import ollitos.bot.physics.displacement.IBImpulseCause;

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

	public static void computeSupportDisplacements(IBPhysicalItem item, BDirection d, IBImpulseCause cause, List<? super IBImpulse> ret ){
		computeSupportDisplacements(item,d,cause,ret,false);
	}

	
	private static void computeSupportDisplacements(IBPhysicalItem item, BDirection d, IBImpulseCause cause, List<? super IBImpulse> ret, boolean conveyorBelt ){
		if( d == BDirection.up || d == BDirection.down ){
			return;
		}
		BPhysics p = item.physics();
		_contacts.clear();
		p.contacts(item, item.region(), BDirection.up, _contacts, p.movableItems() );
		for (IBPhysicalContact c : _contacts) {
			for( IBPhysicalItem i : c.items() ){
				if( i != item ){
					IBImpulse dis = null;
					if( conveyorBelt ){
						dis = new ConveyorBeltDisplacement(i, item, d.vector(), cause);
					}
					else{
						dis = new BSupportDisplacement(i, item, d.vector(), cause);
					}
					ret.add( dis );
				}
			}
		}
	}
	
	private static class ConveyorBeltDisplacement extends BSupportDisplacement{

		public ConveyorBeltDisplacement(IBPhysicalItem item,
				IBPhysicalItem support, IBLocation delta,
				IBImpulseCause cause) {
			super(item, support, delta, cause);
		}
		
	}

}
