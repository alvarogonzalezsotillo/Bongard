package ollitos.bot.physics;

import java.util.List;

import ollitos.bot.control.IBPhysicsControl;
import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.items.BRoomWall;
import ollitos.bot.view.IBPhysicsView;

public interface IBPhysics {
	void start();
	IBPhysicalItem[] movableItems();
	public IBPhysicalItem[] items();	
	public IBPhysicalItem[] fixedItems();
	List<IBPhysicalContact> contacts(IBPhysicalItem i, IBRegion regionOfItem, BDirection d, List<IBPhysicalContact> ret, final IBPhysicalItem ... items);
	boolean intersects(IBPhysicalItem item, IBRegion regionOfItem, IBPhysicalItem ...items );
	IBPhysicsView view();
    IBPhysicsControl control();
	
	public void itemsOfType( BItemType type, List<IBPhysicalItem> ret ); 
	
	void add( IBPhysicalItem t );	
	void remove( IBPhysicalItem t );
	void clearButListeners();
	
    IBPhysicalItem item(BItemType type);

    BRoomWall[] roomWalls();

    void itemsOfMap(BMapItem mi, List<IBPhysicalItem> ret);
}
