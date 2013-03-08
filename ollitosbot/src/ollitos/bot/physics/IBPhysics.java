package ollitos.bot.physics;

import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.view.BPhysicsView;

public interface IBPhysics {
	void start();
	IBPhysicalItem[] movableItems();
	public IBPhysicalItem[] items();	
	public IBPhysicalItem[] fixedItems();
	List<IBPhysicalContact> contacts(IBPhysicalItem i, IBRegion regionOfItem, BDirection d, List<IBPhysicalContact> ret, final IBPhysicalItem ... items);
	void computeCollisions(IBPhysicalItem item, IBLocation vector, List<IBCollision> collisions, IBPhysicalItem ... items);
	boolean intersects(IBPhysicalItem item, IBRegion regionOfItem, IBPhysicalItem ...items );
	BPhysicsView view();
	
	void add( IBPhysicalItem t );	
	void remove( IBPhysicalItem t );
	
}
