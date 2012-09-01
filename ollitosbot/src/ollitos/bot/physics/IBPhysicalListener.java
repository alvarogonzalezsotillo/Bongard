package ollitos.bot.physics;

public interface IBPhysicalListener {
	void itemAdded(IBPhysicalItem i);
	void itemRemoved(IBPhysicalItem i);
	void collision( IBCollision collision );
	void stepFinished();
	
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
	}
}
