package ollitos.bot.physics;

import ollitos.bot.geom.IBRegion;

public interface IBPhysicalListener {
	void itemAdded(IBPhysicalItem i);
	void itemRemoved(IBPhysicalItem i);
	void collision( IBCollision collision );
	void itemMoved(IBPhysicalItem i, IBRegion oldRegion );
	void stepFinished();
	void playerAction(BPlayerAction pa);
	
	public static final IBPhysicalListener[] EMPTY = {};
	
	public class Default implements IBPhysicalListener{
		@Override
		public void itemAdded(IBPhysicalItem i) {
		}

		@Override
		public void itemRemoved(IBPhysicalItem i) {
		}

		@Override
		public void collision(IBCollision collision) {
		}

		@Override
		public void stepFinished() {
		}

		@Override
		public void itemMoved(IBPhysicalItem i, IBRegion oldRegion) {
		}

		@Override
		public void playerAction(BPlayerAction pa) {
		}
	}
}
